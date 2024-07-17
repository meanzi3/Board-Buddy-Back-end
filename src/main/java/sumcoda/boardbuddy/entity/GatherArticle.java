package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GatherArticle extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 모집글 제목
    @Column(nullable = false)
    private String title;

    // 해당 모집글 현재 참가한 인원 (작성자를 포함하여 1로 초기화)
    @Column(nullable = false)
    private Integer currentParticipants = 1;

    // 해당 모집글 제한 인원
    @Column(nullable = false)
    private Integer maxParticipants;

    // 모집 상태 (모집중, 모집마감)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GatherArticleStatus status;

    // 모집글 설명
    @Column(nullable = false)
    private String description;

    // 해당 방의 만나는 시간
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    // 해당 방의 헤어지는 시간(모임 예상 종료 시간)
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    // 필터링 하기 위한 oo시, oo도
    @Column(nullable = false)
    private String sido;

    // 필터링 하기 위한 oo시, oo구
    @Column(nullable = false)
    private String sigu;

    // 필터링 하기 위한 oo동
    @Column(nullable = false)
    private String dong;

    // 해당 방의 만나는 장소
    @Column(nullable = false)
    private String meetingLocation;

    // 양방향 연관관계
    @OneToOne(mappedBy = "gatherArticle")
    private ChatRoom chatRoom;

    // 양방향 연관관계
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gatherArticle", cascade = CascadeType.REMOVE)
    private List<MemberGatherArticle> memberGatherArticles = new ArrayList<>();

    // 양방향 연관관계
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gatherArticle", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public GatherArticle(String title, Integer maxParticipants, GatherArticleStatus status, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, String sido, String sigu, String dong, String meetingLocation) {
        this.title = title;
        this.maxParticipants = maxParticipants;
        this.status = status;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.sido = sido;
        this.dong = dong;
        this.sigu = sigu;
        this.meetingLocation = meetingLocation;
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static GatherArticle createGatherArticle(String title, Integer maxParticipants, GatherArticleStatus status, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, String sido, String sigu, String dong, String meetingLocation) {
        return GatherArticle.builder()
                .title(title)
                .maxParticipants(maxParticipants)
                .status(status)
                .description(description)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .sido(sido)
                .sigu(sigu)
                .dong(dong)
                .meetingLocation(meetingLocation)
                .build();
    }

    // GatherArticle 1 <-> 1 Member
    // 양방향 연관관계 편의 메서드
    public void assignChatRoom(ChatRoom chatRoom) {
        if (this.chatRoom != null) {
            this.chatRoom.assignGatherArticle(null);
        }
        this.chatRoom = chatRoom;
        if (chatRoom != null && chatRoom.getGatherArticle() != this) {
            chatRoom.assignGatherArticle(this);
        }
    }

    // GatherArticle 1 <-> N MemberGatherArticle
    // 양방향 연관관계 편의 메서드
    public void addMemberGatherArticle(MemberGatherArticle memberGatherArticle) {
        this.memberGatherArticles.add(memberGatherArticle);

        if (memberGatherArticle.getGatherArticle() != this) {
            memberGatherArticle.assignGatherArticle(this);
        }
    }

    // GatherArticle 1 <-> N Comment
    // 양방향 연관관계 편의 메서드
    public void addComment(Comment comment) {
        this.comments.add(comment);

        if (comment.getGatherArticle() != this) {
            comment.assignGatherArticle(this);
        }
    }
}
