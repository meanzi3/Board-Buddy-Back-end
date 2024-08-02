package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림 메세지 내용
    @Column(nullable = false)
    private String message;

    // 알림이 생성된 시점
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 연관관계 주인
    // 단방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Notification(String message, LocalDateTime createdAt, Member member) {
        this.message = message;
        this.createdAt = createdAt;
        this.assignMember(member);
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static Notification buildNotification(String message, LocalDateTime createdAt, Member member) {
        return Notification.builder()
                .message(message)
                .createdAt(createdAt)
                .member(member)
                .build();
    }

    // Notification N -> 1 Member
    // 단방향 연관관계 편의 메서드
    public void assignMember(Member member) {
        this.member = member;
    }
}

