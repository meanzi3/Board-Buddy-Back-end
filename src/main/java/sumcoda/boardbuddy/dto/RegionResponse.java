package sumcoda.boardbuddy.dto;


public class RegionResponse {

    public record ProvinceDTO(String code, String name) {}

    public record DistrictDTO(String name) {}
}
