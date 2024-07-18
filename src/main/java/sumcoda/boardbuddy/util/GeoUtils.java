package sumcoda.boardbuddy.util;

public class GeoUtils {

    // 지구의 반경 (단위: km)
    private static final int EARTH_RADIUS = 6371;
    // 계산에 사용되는 숫자
    private static final int ONE = 1;
    private static final int TWO = 2;

    /**
     * 하버사인 공식을 사용하여 두 위치 간의 거리를 계산하는 메서드
     * @param lat1 첫 번째 위치의 위도
     * @param lon1 첫 번째 위치의 경도
     * @param lat2 두 번째 위치의 위도
     * @param lon2 두 번째 위치의 경도
     * @return 두 위치 간의 거리 (단위: km)
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / TWO) * Math.sin(latDistance / TWO)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / TWO) * Math.sin(lonDistance / TWO);
        double c = TWO * Math.atan2(Math.sqrt(a), Math.sqrt(ONE - a));
        return EARTH_RADIUS * c;
    }
}