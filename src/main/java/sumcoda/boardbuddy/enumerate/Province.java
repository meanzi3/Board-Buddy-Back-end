package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Province {

    SEOUL("서울", List.of(
            District.SEOUL_ALL,
            District.SEOUL_GANGNAM,
            District.SEOUL_GANGDONG,
            District.SEOUL_GANGBUK,
            District.SEOUL_GANGSEO,
            District.SEOUL_GWANAK,
            District.SEOUL_GWANGJIN,
            District.SEOUL_GURO,
            District.SEOUL_GEUMCHEON,
            District.SEOUL_NOWON,
            District.SEOUL_DOBONG,
            District.SEOUL_DONGDAEMUN,
            District.SEOUL_DONGJAK,
            District.SEOUL_MAPO,
            District.SEOUL_SEODAEMUN,
            District.SEOUL_SEOCHO,
            District.SEOUL_SEONGDONG,
            District.SEOUL_SEONGBUK,
            District.SEOUL_SONGPA,
            District.SEOUL_YANGCHEON,
            District.SEOUL_YEONGDEUNGPO,
            District.SEOUL_YONGSAN,
            District.SEOUL_EUNPYEONG,
            District.SEOUL_JONGNO,
            District.SEOUL_JUNG,
            District.SEOUL_JUNGRANG
    )),

    GYEONGGI("경기", List.of(
            District.GYEONGGI_ALL,
            District.GYEONGGI_GAPYEONG,
            District.GYEONGGI_GOYANG_DEOKYANG,
            District.GYEONGGI_GOYANG_ILSANDONG,
            District.GYEONGGI_GOYANG_ILSANSEO,
            District.GYEONGGI_GWACHEON,
            District.GYEONGGI_GWANGMYEONG,
            District.GYEONGGI_GWANGJU,
            District.GYEONGGI_GURI,
            District.GYEONGGI_GUNPO,
            District.GYEONGGI_GIMPO,
            District.GYEONGGI_NAMYANGJU,
            District.GYEONGGI_DONGDUCHEON,
            District.GYEONGGI_BUCHEON,
            District.GYEONGGI_SEONGNAM_BUNDANG,
            District.GYEONGGI_SEONGNAM_SUJEONG,
            District.GYEONGGI_SEONGNAM_JUNGWON,
            District.GYEONGGI_SUWON_GWONSEON,
            District.GYEONGGI_SUWON_YEONGTONG,
            District.GYEONGGI_SUWON_JANGAN,
            District.GYEONGGI_SUWON_PALDAL,
            District.GYEONGGI_SIHEUNG,
            District.GYEONGGI_ANSAN_DANWON,
            District.GYEONGGI_ANSAN_SANGROK,
            District.GYEONGGI_ANSEONG,
            District.GYEONGGI_ANYANG_DONGAN,
            District.GYEONGGI_ANYANG_MANAN,
            District.GYEONGGI_YANGJU,
            District.GYEONGGI_YANGPYEONG,
            District.GYEONGGI_YEOJU,
            District.GYEONGGI_YEONCHEON,
            District.GYEONGGI_OSAN,
            District.GYEONGGI_YONGIN_GIHEUNG,
            District.GYEONGGI_YONGIN_SUJI,
            District.GYEONGGI_YONGIN_CHEOIN,
            District.GYEONGGI_UIWANG,
            District.GYEONGGI_UIJEONGBU,
            District.GYEONGGI_ICHEON,
            District.GYEONGGI_PAJU,
            District.GYEONGGI_PYEONGTAEK,
            District.GYEONGGI_POCHEON,
            District.GYEONGGI_HANAM,
            District.GYEONGGI_HWASEONG
    )),

    GANGWON("강원", List.of(
            District.GANGWON_ALL,
            District.GANGWON_GANGNEUNG,
            District.GANGWON_GOSEONG,
            District.GANGWON_DONGHAE,
            District.GANGWON_SAMCHEOK,
            District.GANGWON_SOKCHO,
            District.GANGWON_YANGGU,
            District.GANGWON_YANGYANG,
            District.GANGWON_YEONGWOL,
            District.GANGWON_WONJU,
            District.GANGWON_INJE,
            District.GANGWON_JEONGSEON,
            District.GANGWON_CHEORWON,
            District.GANGWON_CHUNCHEON,
            District.GANGWON_TAEBAEK,
            District.GANGWON_PYEONGCHANG,
            District.GANGWON_HONGCHEON,
            District.GANGWON_HWACHEON,
            District.GANGWON_HOENGSEONG
    )),

    BUSAN("부산", List.of(
            District.BUSAN_ALL,
            District.BUSAN_GANGSEO,
            District.BUSAN_GEUMJEONG,
            District.BUSAN_GIJANG,
            District.BUSAN_NAMGU,
            District.BUSAN_DONGGU,
            District.BUSAN_DONGRAE,
            District.BUSAN_BUSANJIN,
            District.BUSAN_BUKGU,
            District.BUSAN_SASANG,
            District.BUSAN_SAHA,
            District.BUSAN_SEOGU,
            District.BUSAN_SUYONG,
            District.BUSAN_YEONJE,
            District.BUSAN_YEONGDO,
            District.BUSAN_JUNG,
            District.BUSAN_HAEUNDAE
    )),

    DAEGU("대구", List.of(
            District.DAEGU_ALL,
            District.DAEGU_NAMGU,
            District.DAEGU_DALSEO,
            District.DAEGU_DALSEONG,
            District.DAEGU_DONGGU,
            District.DAEGU_BUKGU,
            District.DAEGU_SEOGU,
            District.DAEGU_SUSEONG,
            District.DAEGU_JUNG
    )),

    INCHEON("인천", List.of(
            District.INCHEON_ALL,
            District.INCHEON_GANGHWA,
            District.INCHEON_GYEYANG,
            District.INCHEON_MICHUHOL,
            District.INCHEON_NAMDONG,
            District.INCHEON_DONGGU,
            District.INCHEON_BUPYEONG,
            District.INCHEON_SEOGU,
            District.INCHEON_YEONSU,
            District.INCHEON_ONGJIN,
            District.INCHEON_JUNG
    )),

    GWANGJU("광주", List.of(
            District.GWANGJU_ALL,
            District.GWANGJU_GWANGSAN,
            District.GWANGJU_NAMGU,
            District.GWANGJU_DONGGU,
            District.GWANGJU_BUKGU,
            District.GWANGJU_SEOGU
    )),

    DAEJEON("대전", List.of(
            District.DAEJEON_ALL,
            District.DAEJEON_DADEOK,
            District.DAEJEON_DONGGU,
            District.DAEJEON_SEOGU,
            District.DAEJEON_YUSEONG,
            District.DAEJEON_JUNG
    )),

    ULSAN("울산", List.of(
            District.ULSAN_ALL,
            District.ULSAN_NAMGU,
            District.ULSAN_DONGGU,
            District.ULSAN_BUKGU,
            District.ULSAN_ULJUGUN,
            District.ULSAN_JUNG
    )),

    SEJONG("세종", List.of(
            District.SEJONG_ALL
    )),

    CHUNGBUK("충북", List.of(
            District.CHUNGBUK_ALL,
            District.CHUNGBUK_GOESAN,
            District.CHUNGBUK_DANYANG,
            District.CHUNGBUK_BOEUN,
            District.CHUNGBUK_YEONGDONG,
            District.CHUNGBUK_OKCHEON,
            District.CHUNGBUK_EUMSEONG,
            District.CHUNGBUK_JECHEON,
            District.CHUNGBUK_JEUNGPYEONG,
            District.CHUNGBUK_JINCHEON,
            District.CHUNGBUK_CHEONGJU_SANGDANG,
            District.CHUNGBUK_CHEONGJU_SEOWON,
            District.CHUNGBUK_CHEONGJU_CHEONGWON,
            District.CHUNGBUK_CHEONGJU_HEUNGDEOK,
            District.CHUNGBUK_CHUNGJU
    )),

    CHUNGNAM("충남", List.of(
            District.CHUNGNAM_ALL,
            District.CHUNGNAM_GYERYONG,
            District.CHUNGNAM_GONGJU,
            District.CHUNGNAM_GEUMSAN,
            District.CHUNGNAM_NONSAN,
            District.CHUNGNAM_DANGJIN,
            District.CHUNGNAM_BORYEONG,
            District.CHUNGNAM_BUYEO,
            District.CHUNGNAM_SEOSAN,
            District.CHUNGNAM_SEOCHEON,
            District.CHUNGNAM_ASAN,
            District.CHUNGNAM_YESAN,
            District.CHUNGNAM_CHEONAN_DONGNAM,
            District.CHUNGNAM_CHEONAN_SEOBUK,
            District.CHUNGNAM_CHEONGYANG,
            District.CHUNGNAM_TAEAN,
            District.CHUNGNAM_HONGSEONG
    )),

    JEONBUK("전북", List.of(
            District.JEONBUK_ALL,
            District.JEONBUK_GOCHANG,
            District.JEONBUK_GUNSAN,
            District.JEONBUK_GIMJE,
            District.JEONBUK_NAMWON,
            District.JEONBUK_MUJU,
            District.JEONBUK_BUAN,
            District.JEONBUK_SUNCHANG,
            District.JEONBUK_WANJU,
            District.JEONBUK_IKSAN,
            District.JEONBUK_IMSIL,
            District.JEONBUK_JANGSU,
            District.JEONBUK_JEONJU_DEOKJIN,
            District.JEONBUK_JEONJU_WANSAN,
            District.JEONBUK_JEONGEUP,
            District.JEONBUK_JINAN
    )),

    JEONNAM("전남", List.of(
            District.JEONNAM_ALL,
            District.JEONNAM_GANGJIN,
            District.JEONNAM_GOHEUNG,
            District.JEONNAM_GOKSEONG,
            District.JEONNAM_GWANGYANG,
            District.JEONNAM_GURAE,
            District.JEONNAM_NAJU,
            District.JEONNAM_DAMYANG,
            District.JEONNAM_MOKPO,
            District.JEONNAM_MUAN,
            District.JEONNAM_BOSEONG,
            District.JEONNAM_SUNCHEON,
            District.JEONNAM_SINAN,
            District.JEONNAM_YEOSU,
            District.JEONNAM_YEONGGWANG,
            District.JEONNAM_YEONGAM,
            District.JEONNAM_WANDO,
            District.JEONNAM_JANGSEONG,
            District.JEONNAM_JANGHEUNG,
            District.JEONNAM_JINDO,
            District.JEONNAM_HAMPYEONG,
            District.JEONNAM_HAENAM,
            District.JEONNAM_HWASUN
    )),

    GYEONGBUK("경북", List.of(
            District.GYEONGBUK_ALL,
            District.GYEONGBUK_GYEONGSAN,
            District.GYEONGBUK_GYEONGJU,
            District.GYEONGBUK_GORYEONG,
            District.GYEONGBUK_GUMI,
            District.GYEONGBUK_GUNWI,
            District.GYEONGBUK_KIMCHEON,
            District.GYEONGBUK_MUNGYEONG,
            District.GYEONGBUK_BONGHWA,
            District.GYEONGBUK_SANGJU,
            District.GYEONGBUK_SEONGJU,
            District.GYEONGBUK_ANDONG,
            District.GYEONGBUK_YEONGDEOK,
            District.GYEONGBUK_YEONGYANG,
            District.GYEONGBUK_YEONGJU,
            District.GYEONGBUK_YEONGCHEON,
            District.GYEONGBUK_YECHEON,
            District.GYEONGBUK_ULLEUNG,
            District.GYEONGBUK_ULJIN,
            District.GYEONGBUK_UISEONG,
            District.GYEONGBUK_CHEONGDO,
            District.GYEONGBUK_CHEONGSONG,
            District.GYEONGBUK_CHILGOK,
            District.GYEONGBUK_POHANG_NAMGU,
            District.GYEONGBUK_POHANG_BUKGU
    )),

    GYEONGNAM("경남", List.of(
            District.GYEONGNAM_ALL,
            District.GYEONGNAM_GEOJE,
            District.GYEONGNAM_GEACHANG,
            District.GYEONGNAM_GOSEONG,
            District.GYEONGNAM_GIMHAE,
            District.GYEONGNAM_NAMHAE,
            District.GYEONGNAM_MIRYANG,
            District.GYEONGNAM_SACHEON,
            District.GYEONGNAM_SANCHEONG,
            District.GYEONGNAM_YANGSAN,
            District.GYEONGNAM_UIRYEONG,
            District.GYEONGNAM_JINJU,
            District.GYEONGNAM_CHANGNYEONG,
            District.GYEONGNAM_CHANGWON_MASANHAPPO,
            District.GYEONGNAM_CHANGWON_MASANHOE,
            District.GYEONGNAM_CHANGWON_SEONGSAN,
            District.GYEONGNAM_CHANGWON_UICHANG,
            District.GYEONGNAM_CHANGWON_JINHAE,
            District.GYEONGNAM_TONGYEONG,
            District.GYEONGNAM_HADONG,
            District.GYEONGNAM_HAMAN,
            District.GYEONGNAM_HAMYANG,
            District.GYEONGNAM_HAPCHEON
    )),

    JEJU("제주", List.of(
            District.JEJU_ALL,
            District.JEJU_SEOGWIPO,
            District.JEJU_JEJU
    ));

    private final String name;

    private final List<District> districts;
}
