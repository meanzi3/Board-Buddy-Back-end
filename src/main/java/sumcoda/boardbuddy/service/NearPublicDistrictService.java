package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.NearPublicDistrictRequest;
import sumcoda.boardbuddy.dto.NearPublicDistrictResponse;
import sumcoda.boardbuddy.dto.PublicDistrictResponse;
import sumcoda.boardbuddy.entity.NearPublicDistrict;
import sumcoda.boardbuddy.entity.PublicDistrict;
import sumcoda.boardbuddy.enumerate.RadiusRange;
import sumcoda.boardbuddy.exception.nearPublicDistrict.NearPublicDistrictRetrievalException;
import sumcoda.boardbuddy.exception.publicDistrict.PublicDistrictRetrievalException;
import sumcoda.boardbuddy.repository.nearPublicDistric.NearPublicDistrictJdbcRepository;
import sumcoda.boardbuddy.repository.nearPublicDistric.NearPublicDistrictRepository;
import sumcoda.boardbuddy.repository.publicDistrict.PublicDistrictRepository;
import sumcoda.boardbuddy.util.GeoUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NearPublicDistrictService {

    // 행정 구역을 조회하기 위해 PublicDistrictRepository 주입
    private final PublicDistrictRepository publicDistrictRepository;
    // 기존에 저장된 주변 행정 구역 정보 조회하기 위해 NearPublicDistrictRepository 주입
    private final NearPublicDistrictRepository nearPublicDistrictRepository;
    // bulk insert query 를 실행하기 위해 NearPublicDistrictJdbcRepository 주입
    private final NearPublicDistrictJdbcRepository nearPublicDistrictJdbcRepository;
    // 레디스에서 행정 구역을 조회하기 위해 PublicDistrictRedisService 주입
    private final PublicDistrictRedisService publicDistrictRedisService;

    /**
     * 위치 설정 시 주어진 위치를 기준으로 주변 행정 구역을 저장하는 메서드
     * @param baseLocation 기준 위치
     * @return 주변 행정 구역의 정보
     */
    @Transactional
    public Map<Integer, List<MemberResponse.LocationDTO>> saveNearDistrictByUpdateLocation(NearPublicDistrictRequest.LocationDTO baseLocation) {

        // 반환할 멤버의 주변 위치 정보를 담을 맵
        Map<Integer, List<MemberResponse.LocationDTO>> nearbyLocations = new HashMap<>();

        // 기준 위치 선언
        String sido = baseLocation.getSido();
        String sgg = baseLocation.getSgg();
        String emd = baseLocation.getEmd();

        // redis 에서 조회 - 기준 위치에 해당하는 행정 구역을 조회
        PublicDistrict publicDistrict = publicDistrictRedisService.findBySidoAndSggAndEmd(sido, sgg, emd)
                .orElseGet(() -> {
                    // mariadb 에서 조회 - 기준 위치에 해당하는 행정 구역을 조회(redis 장애 발생 시 mariadb 에서 조회)
                    log.error("[redis findBySidoAndSggAndEmd() error]");
                    return publicDistrictRepository.findBySidoAndSggAndEmd(sido, sgg, emd)
                            .orElseThrow(() -> new PublicDistrictRetrievalException("입력한 위치 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));
                });

        // 기존에 저장된 주변 행정 구역 정보 조회
        List<NearPublicDistrictResponse.InfoDTO> existingNearbyDistricts = nearPublicDistrictRepository.findInfoDTOsByPublicDistrictId(publicDistrict.getId());

        // 기존 주변 구역이 있다면 해당 정보를 반환
        if (!existingNearbyDistricts.isEmpty()) {
            return existingNearbyDistricts.stream()
                    .collect(Collectors.groupingBy(
                            NearPublicDistrictResponse.InfoDTO::getRadius,
                            Collectors.mapping(infoDTO -> new MemberResponse
                                    .LocationDTO(infoDTO.getSido(), infoDTO.getSgg(), infoDTO.getEmd()), Collectors.toList())
                    ));
        }

        // redis 에서 조회 - 모든 행정 구역 정보를 조회
        List<PublicDistrictResponse.InfoDTO> allLocations = publicDistrictRedisService.findAllInfoDTOs();

        // mariadb 에서 조회 - 모든 행정 구역 정보를 조회(redis 장애 발생 시 mariadb 에서 조회)
        if (!allLocations.isEmpty()) {
            log.info("[redis findAllInfoDTOs() success]");
        } else {
            log.error("[redis findAllInfoDTOs() error]");
            allLocations = publicDistrictRepository.findAllInfoDTOs();
        }

        // 데이터베이스에 새로 추가할 주변 행정 구역 리스트
        List<NearPublicDistrict> allNearPublicDistricts = new ArrayList<>();

        // 각 반경 범위에 대해 주변 행정 구역을 찾고 맵에 추가
        // 10, 7, 5, 2km 순서로 필터를 통해 최적화해서 찾기
        for (RadiusRange range : RadiusRange.values()) {
            // 반경 범위에 대해 주변 행정 구역 찾기
            List<PublicDistrictResponse.InfoDTO> filteredLocations = allLocations.stream()
                    .filter(infoDTO -> GeoUtil.calculateDistance(
                            publicDistrict.getLongitude(),
                            publicDistrict.getLatitude(),
                            infoDTO.getLongitude(),
                            infoDTO.getLatitude()) <= range.getRadius())
                    .collect(Collectors.toList());

            // 주변 행정 구역 객체 생성
            List<NearPublicDistrict> nearPublicDistricts = filteredLocations.stream()
                    .map(filteredLocation -> NearPublicDistrict.buildNearPublicDistrict(
                            filteredLocation.getSido(),
                            filteredLocation.getSgg(),
                            filteredLocation.getEmd(),
                            range.getRadius(),
                            publicDistrict))
                    .toList();

            // 모든 주변 행정 구역 리스트에 추가
            allNearPublicDistricts.addAll(nearPublicDistricts);

            // 주변 행정 구역 DTO 리스트 생성
            List<MemberResponse.LocationDTO> locationDTOS = filteredLocations.stream()
                    .map(filteredLocation -> new MemberResponse
                            .LocationDTO(filteredLocation.getSido(), filteredLocation.getSgg(), filteredLocation.getEmd()))
                    .collect(Collectors.toList());

            // 응답 맵에 추가
            nearbyLocations.put(range.getRadius(), locationDTOS);

            // 기존에 있던 모든 행정 구역을 필터된 주변 행정 구역으로 값을 대체
            allLocations = filteredLocations;
        }

        // 일반 쿼리(general insert query)로 주변 행정 구역을 저장 - 쿼리 약 1000 개
//        nearPublicDistrictRepository.saveAll(allNearPublicDistricts);

        // 벌크 쿼리(bulk insert query)로 주변 행정 구역을 저장 - 쿼리 1 개
        nearPublicDistrictJdbcRepository.saveAll(allNearPublicDistricts);

        // 응답 맵 반환
        return nearbyLocations;
    }

    /**
     * 회원가입 시 주어진 위치를 기준으로 주변 행정 구역을 저장하는 메서드
     * @param baseLocation 기준 위치
     */
    @Transactional
    public void saveNearDistrictByRegisterLocation(NearPublicDistrictRequest.LocationDTO baseLocation) {

        // 기준 위치 선언
        String sido = baseLocation.getSido();
        String sgg = baseLocation.getSgg();
        String emd = baseLocation.getEmd();

        // redis 에서 조회 - 기준 위치에 해당하는 행정 구역을 조회
        PublicDistrict publicDistrict = publicDistrictRedisService.findBySidoAndSggAndEmd(sido, sgg, emd)
                .orElseGet(() -> {
                    // mariadb 에서 조회 - 기준 위치에 해당하는 행정 구역을 조회(redis 장애 발생 시 mariadb 에서 조회)
                    log.error("[redis findBySidoAndSggAndEmd() error]");
                    return publicDistrictRepository.findBySidoAndSggAndEmd(sido, sgg, emd)
                            .orElseThrow(() -> new PublicDistrictRetrievalException("입력한 위치 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));
                });

        // 기존에 저장된 주변 행정 구역 정보 조회
        List<NearPublicDistrictResponse.InfoDTO> existingNearbyDistricts = nearPublicDistrictRepository.findInfoDTOsByPublicDistrictId(publicDistrict.getId());

        // 기존에 저장된 주변 행정 구역이 있다면 바로 리턴
        if (!existingNearbyDistricts.isEmpty()) {
            return;
        }

        // redis 에서 조회 - redis 를 사용해서 모든 행정 구역 정보를 조회
        List<PublicDistrictResponse.InfoDTO> allLocations = publicDistrictRedisService.findAllInfoDTOs();

        // mariadb 에서 조회 - mariadb 를 사용해서 모든 행정 구역 정보를 조회(redis 장애 발생 시 mariadb 에서 조회)
        if (!allLocations.isEmpty()) {
            log.info("[redis findAllInfoDTOs() success]");
        } else {
            log.error("[redis findAllInfoDTOs() error]");
            allLocations = publicDistrictRepository.findAllInfoDTOs();
        }

        // 데이터베이스에 새로 추가할 주변 행정 구역 리스트
        List<NearPublicDistrict> allNearPublicDistricts = new ArrayList<>();

        // 각 반경 범위에 대해 주변 행정 구역을 찾고 리스트에 추가
        for (RadiusRange range : RadiusRange.values()) {
            // 반경 범위에 대해 주변 행정 구역 찾기
            List<PublicDistrictResponse.InfoDTO> filteredLocations = allLocations.stream()
                    .filter(infoDTO -> GeoUtil.calculateDistance(
                            publicDistrict.getLongitude(),
                            publicDistrict.getLatitude(),
                            infoDTO.getLongitude(),
                            infoDTO.getLatitude()) <= range.getRadius())
                    .collect(Collectors.toList());

            // 주변 행정 구역 객체 생성
            List<NearPublicDistrict> nearPublicDistricts = filteredLocations.stream()
                    .map(filteredLocation -> NearPublicDistrict.buildNearPublicDistrict(
                            filteredLocation.getSido(),
                            filteredLocation.getSgg(),
                            filteredLocation.getEmd(),
                            range.getRadius(),
                            publicDistrict))
                    .toList();

            // 모든 주변 행정 구역 리스트에 추가
            allNearPublicDistricts.addAll(nearPublicDistricts);

            // 기존에 있던 모든 행정 구역을 필터된 주변 행정 구역으로 값을 대체
            allLocations = filteredLocations;
        }

        // 일반 쿼리(general insert query)로 주변 행정 구역을 저장 - 쿼리 약 1000 개
//        nearPublicDistrictRepository.saveAll(allNearPublicDistricts);

        // 벌크 쿼리(bulk insert query)로 주변 행정 구역을 저장 - 쿼리 1 개
        nearPublicDistrictJdbcRepository.saveAll(allNearPublicDistricts);
    }

    /**
     * 주변 행정 구역을 조회하는 메서드
     * @param baseLocation 기준 위치
     * @return 주변 행정 구역의 정보
     */
    public Map<Integer, List<MemberResponse.LocationDTO>> getNearbyLocations(NearPublicDistrictRequest.LocationDTO baseLocation) {

        // 기준 위치 선언
        String sido = baseLocation.getSido();
        String sgg = baseLocation.getSgg();
        String emd = baseLocation.getEmd();

        // redis 에서 조회 - 기준 위치에 해당하는 IdDTO 를 조회
        PublicDistrictResponse.IdDTO idDTO = publicDistrictRedisService.findIdDTOBySidoAndSggAndEmd(sido, sgg, emd)
                .orElseGet(() -> {
                    // mariadb 에서 조회 - 기준 위치에 해당하는 IdDTO 를 조회(redis 장애 발생 시 mariadb 에서 조회)
                    log.error("[redis findIdDTOBySidoAndSggAndEmd() error]");
                    return publicDistrictRepository.findIdDTOBySidoAndSggAndEmd(sido, sgg, emd)
                            .orElseThrow(() -> new PublicDistrictRetrievalException("행정 구역을 찾을 수 없습니다. 관리자에게 문의하세요."));
                });

        // 행정 구역의 id 로 주변 행정 구역 정보 조회
        List<NearPublicDistrictResponse.InfoDTO> existingNearbyDistricts = nearPublicDistrictRepository.findInfoDTOsByPublicDistrictId(idDTO.getId());

        // 주변 행정 구역이 없는 경우 예외 처리
        if (existingNearbyDistricts.isEmpty()) {
            throw new NearPublicDistrictRetrievalException("주변 행정 구역을 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        // 기존 주변 구역이 있다면 해당 정보를 반환
        return existingNearbyDistricts.stream()
                .collect(Collectors.groupingBy(
                        NearPublicDistrictResponse.InfoDTO::getRadius,
                        Collectors.mapping(infoDTO -> new MemberResponse
                                .LocationDTO(infoDTO.getSido(), infoDTO.getSgg(), infoDTO.getEmd()), Collectors.toList())
                ));
    }
}