package sumcoda.boardbuddy.dto;


public class RegionResponse {

    public record ProvinceDTO(String code, String name, String officialName) {}

    public record DistrictDTO(String name) {}
}
