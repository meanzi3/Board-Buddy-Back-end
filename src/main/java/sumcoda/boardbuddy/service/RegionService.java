package sumcoda.boardbuddy.service;

import org.springframework.stereotype.Service;
import sumcoda.boardbuddy.dto.RegionResponse;
import sumcoda.boardbuddy.enumerate.Province;
import sumcoda.boardbuddy.exception.RegionNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RegionService {

    /**
     * 전체 시/도 목록을 조회
     *
     * @return List<ProvinceDto> 시도 코드(code)와 한글명(name) 리스트
     */
    public List<RegionResponse.ProvinceDTO> getProvinceList() {
        return Stream.of(Province.values())
                .map(p -> new RegionResponse.ProvinceDTO(p.name(), p.getName()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 시도의 시/군/구 목록을 조회
     *
     * @param provinceCode 시도 코드 (예: "SEOUL")
     * @return List<DistrictDto> 해당 시도의 시/군/구 한글명(name) 리스트
     */
    public List<RegionResponse.DistrictDTO> getDistrictList(String provinceCode) {
        Province province;

        try {
            province = Province.valueOf(provinceCode.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RegionNotFoundException("올바르지 않은 Province Code 입니다: " + provinceCode);
        }
        return province.getDistricts().stream()
                .map(d -> new RegionResponse.DistrictDTO(d.getName()))
                .collect(Collectors.toList());
    }
}
