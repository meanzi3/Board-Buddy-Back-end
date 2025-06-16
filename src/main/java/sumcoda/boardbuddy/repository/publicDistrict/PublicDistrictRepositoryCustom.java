//package sumcoda.boardbuddy.repository.publicDistrict;
//
//import sumcoda.boardbuddy.dto.PublicDistrictResponse;
//
//import java.util.List;
//import java.util.Optional;

/**
 * @apiNote 현재는 사용률 저조로 기능이 비활성화된 상태
 *          추후 사용자 요청 또는 트래픽 증가 시 다시 활성화될 수 있음
 */
//public interface PublicDistrictRepositoryCustom {
//
//    // 애플리케이션 실행 시 레디스에 행정구역을 저장하기 위해 조회하는 쿼리
//    List<PublicDistrictResponse.InfoWithIdDTO> findAllInfoWithIdDTOs();
//
//    // 레디스 장애 발생 시 데이터베이스에서 InfoDTO 리스트를 조회하기 위한 쿼리
//    List<PublicDistrictResponse.InfoDTO> findAllInfoDTOs();
//
//    // 레디스 장애 발생 시 데이터베이스에서 emd 로 InfoDTO 리스트를 검색하기 위한 쿼리
//    List<PublicDistrictResponse.InfoDTO> findInfoDTOsByEmd(String emd);
//
//    // 레디스 장애 발생 시 데이터베이스에서 sido, sgg, emd 로 IdDTO 를 조회하기 위한 쿼리
//    Optional<PublicDistrictResponse.IdDTO> findIdDTOBySidoAndSggAndEmd(String sido, String sgg, String emd);
//
//    // 레디스 장애 발생 시 데이터베이스에서 sido, sgg, emd 로 CoordinateDTO 를 조회하기 위한 쿼리
//    Optional<PublicDistrictResponse.CoordinateDTO> findCoordinateDTOBySidoAndSggAndEmd(String sido, String sgg, String emd);
//}
