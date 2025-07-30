package sumcoda.boardbuddy.util;


import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public final class BadgeImageUtil {

    private static final String BADGE_IMAGE_PREFIX = "prod/images/badges/";

    private static final String BADGE_IMAGE_SUFFIX = "_badge.png";

    private static final DateTimeFormatter IMAGE_NAME_FMT = DateTimeFormatter.ofPattern("yyyyMM");

    private static final DateTimeFormatter DB_YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyy.MM");


    private BadgeImageUtil() {
        /* 유틸 클래스라 인스턴스화 금지 */
    }

    /**
     * 주어진 날짜를 기준으로 뱃지 이미지 이름을 생성
     *
     * @param lastMonth 기준 날짜 (예: LocalDate.now().minusMonths(1))
     * @return "202507_badge.png" 같은 완전한 파일명
     */
    public static String buildBadgeImageS3SavedObjectName(YearMonth lastMonth) {

        String lastMonthStr = lastMonth.format(IMAGE_NAME_FMT);

        return lastMonthStr + BADGE_IMAGE_SUFFIX;
    }

    /**
     * 주어진 날짜를 기준으로 뱃지 이미지 요청 키를 생성
     *
     * @param s3SavedObjectName S3에 저장된 뱃지 이미지 이름
     * @return "prod/images/badges/202507_badge.png" 같은 완전한 파일 경로
     */
    public static String buildBadgeImageS3RequestKey(String s3SavedObjectName) {

        return BADGE_IMAGE_PREFIX + s3SavedObjectName;
    }

    /**
     * 주어진 날짜를 기준으로 DB 저장용 연월 문자열을 생성
     *
     * @param lastMonth 기준 날짜 (예: LocalDate.now().minusMonths(1))
     * @return "2024.07" 같은 연월 문자열
     */
    public static String convertBadgeYearMonthToString(YearMonth lastMonth) {
        return lastMonth.format(DB_YEAR_MONTH_FMT);
    }


}
