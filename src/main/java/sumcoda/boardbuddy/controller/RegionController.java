package sumcoda.boardbuddy.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sumcoda.boardbuddy.dto.RegionResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.RegionService;

import java.util.List;
import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithPairKeyData;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    /**
     * 전체 시/도 목록을 반환하는 API
     *
     * @return List<ProvinceDto> 시도 코드(code)와 한글명(name) 리스트
     */
    @GetMapping("/provinces")
    public ResponseEntity<ApiResponse<Map<String, List<RegionResponse.ProvinceDTO>>>> getProvinces() {

        List<RegionResponse.ProvinceDTO> provinceList = regionService.getProvinceList();

        return buildSuccessResponseWithPairKeyData("dataList", provinceList, "시/도 목록을 성공적으로 조회 했습니다.", HttpStatus.OK);
    }

    /**
     * 특정 시/도의 시/군/구 목록을 반환하는 API
     *
     * @param provinceCode 형태의 시도 코드 (예: "SEOUL")
     * @return List<DistrictDto> 해당 시도의 시·군·구 한글명(name) 리스트
     */
    @GetMapping("/provinces/{provinceCode}/districts")
    public ResponseEntity<ApiResponse<Map<String, List<RegionResponse.DistrictDTO>>>> getDistricts(@PathVariable String provinceCode) {

        List<RegionResponse.DistrictDTO> districtList = regionService.getDistrictList(provinceCode);

        return buildSuccessResponseWithPairKeyData("dataList", districtList, provinceCode + " Province Code 매핑된 시/군/구 목록을 성공적으로 조회 했습니다.", HttpStatus.OK);
    }
}
