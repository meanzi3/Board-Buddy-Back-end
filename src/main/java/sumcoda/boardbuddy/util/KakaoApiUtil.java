package sumcoda.boardbuddy.util;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class KakaoApiUtil {

    // 키워드로 장소 검색 api 기본 uri
    private static final String KAKAO_LOCAL_KEYWORD_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    // 좌표로 행정구역정보 받기 api 기본 uri
    private static final String KAKAO_LOCAL_COORD_TO_REGION_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    // 보드 카페 키워드
    private static final String BOARD_CAFE_KEYWORD = "보드카페";
    // 카페 카테고리 그룹 코드
    private static final String CAFE_CATEGORY_CODE = "CE7";
    // 거리 기준 정렬
    private static final String SORT_BY_DISTANCE = "distance";

    // 인스턴스화를 방지하기 위해 private 생성자 선언
    private KakaoApiUtil() {}

    // 키워드로 장소 검색 api 를 호출하기 위한 전체 uri 를 만드는 메서드
    public static URI buildUriByKeywordSearch(Double x, Double y, Integer radius, Integer page) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_KEYWORD_SEARCH_URL);
        uriBuilder.queryParam("query", BOARD_CAFE_KEYWORD); // 키워드
        uriBuilder.queryParam("category_group_code", CAFE_CATEGORY_CODE); // 카테고리
        uriBuilder.queryParam("x", x); // 경도
        uriBuilder.queryParam("y", y); // 위도
        uriBuilder.queryParam("radius", radius); // 반경
        uriBuilder.queryParam("page", page); // 페이지
        uriBuilder.queryParam("sort",SORT_BY_DISTANCE); // 거리 기준 정렬

        return uriBuilder.build().encode().toUri(); // uri 생성
    }

    // 좌표로 행정구역정보 받기 api 를 호출하기 위한 전체 uri 를 만드는 메서드
    public static URI buildUriByCoordinateToRegion(Double x, Double y) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_COORD_TO_REGION_URL);
        uriBuilder.queryParam("x", x); // 경도
        uriBuilder.queryParam("y", y); // 위도

        return uriBuilder.build().encode().toUri();
    }
}
