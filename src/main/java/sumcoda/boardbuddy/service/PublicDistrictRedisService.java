package sumcoda.boardbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sumcoda.boardbuddy.dto.PublicDistrictResponse;
import sumcoda.boardbuddy.entity.PublicDistrict;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicDistrictRedisService {

    // 캐시 키 값을 행정구역으로 선언
    private static final String CACHE_KEY = "PUBLIC_DISTRICT";

    // Redis 와 상호작용하고 자료 구조를 쉽게 다루기 위해 RedisTemplate 선언
    private final RedisTemplate<String, Object> redisTemplate;

    // 행정구역 데이터를 조회하거나 저장할 때 Serialization, Deserialization 하기 위해 의존성 주입
    private final ObjectMapper objectMapper;

    // 해쉬 자료구조 필드 생성(key : 캐시 키, field : pk, value : dto)
    private HashOperations<String, String, String> hashOperations;

    // 생성자 주입 이후 호출
    @PostConstruct
    public void init() {
        // 해시 자료 구조를 다루기 위한 HashOperations 객체 생성
        this.hashOperations = redisTemplate.opsForHash();
    }

    /**
     * 행정구역 정보를 레디스에 저장
     *
     * @param infoWithIdDTO 행정구역 정보 DTO
     */
    public void save(PublicDistrictResponse.InfoWithIdDTO infoWithIdDTO) {
        try {
            // DTO 객체를 JSON 문자열로 직렬화하여 Redis 해시 자료구조에 저장
            hashOperations.put(CACHE_KEY, infoWithIdDTO.getId().toString(), serializeInfoWithIdDTO(infoWithIdDTO));
        } catch (JsonProcessingException e) {
            log.error("[PublicDistrictRedisService save() error]: {}", e.getMessage());
        }
    }

    /**
     * 행정구역 정보 리스트를 레디스에서 조회
     * querydsl 쿼리 대체
     *
     * @return 모든 행정구역 정보 리스트
     */
    public List<PublicDistrictResponse.InfoDTO> findAllInfoDTOs() {
        // 모든 엔트리를 조회하고, JSON 문자열을 DTO 객체로 역직렬화하여 리스트로 반환
        return hashOperations.entries(CACHE_KEY).values().stream()
                .map(value -> {
                    try {
                        PublicDistrictResponse.InfoWithIdDTO infoWithIdDTO = deserializeInfoWithIdDTO(value);
                        return PublicDistrictResponse.InfoDTO.builder()
                                .sido(infoWithIdDTO.getSido())
                                .sgg(infoWithIdDTO.getSgg())
                                .emd(infoWithIdDTO.getEmd())
                                .latitude(infoWithIdDTO.getLatitude())
                                .longitude(infoWithIdDTO.getLongitude())
                                .build();
                    } catch (JsonProcessingException e) {
                        log.error("[PublicDistrictRedisService findAll() error]: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 행정구역 정보 리스트를 레디스에서 검색
     * querydsl 쿼리 대체
     *
     * @param emd 읍면동
     * @return 행정구역 정보 리스트
     */
    public List<PublicDistrictResponse.InfoDTO> findInfoDTOsByEmd(String emd) {
        // 모든 엔트리를 조회하고, JSON 문자열을 DTO 객체로 역직렬화한 후 읍면동 이름으로 필터링하여 리스트로 반환
        try {
            return hashOperations.entries(CACHE_KEY).values().stream()
                    .map(value -> {
                        try {
                            PublicDistrictResponse.InfoWithIdDTO infoWithIdDTO = deserializeInfoWithIdDTO(value);
                            if (infoWithIdDTO.getEmd().contains(emd)) {
                                return PublicDistrictResponse.InfoDTO.builder()
                                        .sido(infoWithIdDTO.getSido())
                                        .sgg(infoWithIdDTO.getSgg())
                                        .emd(infoWithIdDTO.getEmd())
                                        .latitude(infoWithIdDTO.getLatitude())
                                        .longitude(infoWithIdDTO.getLongitude())
                                        .build();
                            }
                        } catch (JsonProcessingException e) {
                            log.error("[PublicDistrictRedisService findInfoDTOsByEmd() error]: {}", e.getMessage());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // 레디스 장애 발생 시 null 반환
            log.error("[PublicDistrictRedisService findInfoDTOsByEmd() error]: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 행정구역 ID 정보를 레디스에서 조회
     * querydsl 쿼리 대체
     *
     * @param sido 시도
     * @param sgg 시군구
     * @param emd 읍면동
     * @return 행정구역 ID 정보 DTO
     */
    public Optional<PublicDistrictResponse.IdDTO> findIdDTOBySidoAndSggAndEmd(String sido, String sgg, String emd) {
        // 모든 엔트리를 조회하고, JSON 문자열을 DTO 객체로 역직렬화한 후 시도, 시군구, 읍면동으로 필터링하여 IdDTO 반환
        return hashOperations.entries(CACHE_KEY).values().stream()
                .map(value -> {
                    try {
                        PublicDistrictResponse.InfoWithIdDTO infoWithIdDTO = deserializeInfoWithIdDTO(value);
                        if (infoWithIdDTO.getSido().equals(sido) && infoWithIdDTO.getSgg().equals(sgg) && infoWithIdDTO.getEmd().equals(emd)) {
                            return PublicDistrictResponse.IdDTO.builder()
                                    .id(infoWithIdDTO.getId())
                                    .build();
                        }
                    } catch (JsonProcessingException e) {
                        log.error("[PublicDistrictRedisService findIdDTOBySidoAndSggAndEmd() error]: {}", e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * 행정구역 엔티티를 레디스에서 조회
     * spring data jpa 쿼리 대체
     *
     * @param sido 시도
     * @param sgg 시군구
     * @param emd 읍면동
     * @return 행정구역 엔티티
     */
    public Optional<PublicDistrict> findBySidoAndSggAndEmd(String sido, String sgg, String emd) {
        // 모든 엔트리를 조회하고, JSON 문자열을 DTO 객체로 역직렬화한 후 시도, 시군구, 읍면동으로 필터링하여 엔티티 반환
        return hashOperations.entries(CACHE_KEY).values().stream()
                .map(value -> {
                    try {
                        PublicDistrictResponse.InfoWithIdDTO infoWithIdDTO = deserializeInfoWithIdDTO(value);
                        if (infoWithIdDTO.getSido().equals(sido) && infoWithIdDTO.getSgg().equals(sgg) && infoWithIdDTO.getEmd().equals(emd)) {
                            return PublicDistrict.buildIdWithPublicDistrict(
                                    infoWithIdDTO.getId(),
                                    infoWithIdDTO.getSido(),
                                    infoWithIdDTO.getSgg(),
                                    infoWithIdDTO.getEmd(),
                                    infoWithIdDTO.getLongitude(),
                                    infoWithIdDTO.getLatitude()
                            );
                        }
                    } catch (JsonProcessingException e) {
                        log.error("[PublicDistrictRedisService findBySidoAndSggAndEmd() error]: {}", e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst();
    }

    // DTO 를 JSON 으로 직렬화하는 메서드
    private String serializeInfoWithIdDTO(PublicDistrictResponse.InfoWithIdDTO infoWithIdDTO) throws JsonProcessingException {
        return objectMapper.writeValueAsString(infoWithIdDTO);
    }

    // JSON 을 DTO 로 역직렬화하는 메서드
    private PublicDistrictResponse.InfoWithIdDTO deserializeInfoWithIdDTO(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, PublicDistrictResponse.InfoWithIdDTO.class);
    }
}
