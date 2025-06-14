package sumcoda.boardbuddy.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;

@Converter(autoApply = true)
public class InstantToEpochMilliConverter
        implements AttributeConverter<Instant, Long> {

    // 엔티티가 갖고 있는 Instant 타입의 값(애플리케이션 메모리 상의 값)을 데이터베이스에 저장 가능한 형태로 변환
    @Override
    public Long convertToDatabaseColumn(Instant attribute) {
        return attribute != null ? attribute.toEpochMilli() : null;
    }

    // DB 조회 시 꺼내온 컬럼 값(Long)을 다시 엔티티 필드 타입인 Instant로 변환
    @Override
    public Instant convertToEntityAttribute(Long dbData) {
        return dbData != null ? Instant.ofEpochMilli(dbData) : null;
    }
}

