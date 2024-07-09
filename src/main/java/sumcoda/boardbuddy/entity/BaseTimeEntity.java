package sumcoda.boardbuddy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    // 생성 시점
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 수정 시점
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
