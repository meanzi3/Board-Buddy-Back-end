package sumcoda.boardbuddy.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.PublicDistrictResponse;
import sumcoda.boardbuddy.repository.publicDistrict.PublicDistrictRepository;
import sumcoda.boardbuddy.service.PublicDistrictRedisService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublicDistrictInitializer implements CommandLineRunner {

    private final PublicDistrictRepository publicDistrictRepository;
    private final PublicDistrictRedisService publicDistrictRedisService;

    @Override
    public void run(String... args) throws Exception {
        // 데이터베이스에서 모든 행정구역 데이터를 조회
        List<PublicDistrictResponse.InfoWithIdDTO> allDistricts = publicDistrictRepository.findAllInfoWithIdDTOs();

        // 조회한 데이터를 레디스에 저장
        allDistricts.forEach(publicDistrictRedisService::save);
        log.info("[PublicDistrictInitializer save() success]");
    }
}
