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

    // 해당 모집글 현재 참가한 인원
    @Column(nullable = false)
    private Integer currentParticipants;

    // 해당 모집글 제한 인원
    @Column(nullable = false)
    private Integer maxParticipants;

    // 모집 상태 (모집중, 모집마감)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GatherArticleStatus gatherArticleStatus;

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

    // 필터링 하기 위한 oo시, oo군, oo구
    @Column(nullable = false)
    private String sgg;

    // 필터링 하기 위한 oo읍, oo면, oo동
    @Column(nullable = false)
    private String emd;

    // 해당 방의 만나는 장소
    @Column(nullable = false)
    private String meetingLocation;

    // 경도
    @Column(nullable = false)
    private Double x;

    // 위도
    @Column(nullable = false)
    private Double y;

    // 양방향 연관관계
    @OneToOne(mappedBy = "gatherArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom chatRoom;

    // 양방향 연관관계
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gatherArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGatherArticle> memberGatherArticles = new ArrayList<>();

    // 양방향 연관관계
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gatherArticle", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    // 양방향 연관관계
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gatherArticle", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public GatherArticle(String title, Integer currentParticipants, Integer maxParticipants, GatherArticleStatus gatherArticleStatus, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, String sido, String sgg, String emd, String meetingLocation, Double x, Double y) {
        this.title = title;
        this.currentParticipants = currentParticipants;
        this.maxParticipants = maxParticipants;
        this.gatherArticleStatus = gatherArticleStatus;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.sido = sido;
        this.emd = emd;
        this.sgg = sgg;
        this.meetingLocation = meetingLocation;
        this.x = x;
        this.y = y;
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static GatherArticle buildGatherArticle(String title, Integer currentParticipants, Integer maxParticipants, GatherArticleStatus gatherArticleStatus, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, String sido, String sgg, String emd, String meetingLocation, Double x, Double y) {
        return GatherArticle.builder()
                .title(title)
                .currentParticipants(currentParticipants)
                .maxParticipants(maxParticipants)
                .gatherArticleStatus(gatherArticleStatus)
                .description(description)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .sido(sido)
                .sgg(sgg)
                .emd(emd)
                .meetingLocation(meetingLocation)
                .x(x)
                .y(y)
                .build();
    }

    // 수정 메서드
    public void update(String title, String description, String location, String sido, String sgg, String emd, Double x, Double y, Integer maxParticipants, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (location != null) this.meetingLocation = location;
        if (sido != null) this.sido = sido;
        if (sgg != null) this.sgg = sgg;
        if (emd != null) this.emd = emd;
        if (x != null) this.x = x;
        if (y != null) this.y = y;
        if (maxParticipants != null) this.maxParticipants = maxParticipants;
        if (startDateTime != null) this.startDateTime = startDateTime;
        if (endDateTime != null) this.endDateTime = endDateTime;
    }

    // GatherArticle 1 <-> 1 ChatRoom
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

    // GatherArticle 1 <-> N Comment
    // 양방향 연관관계 편의 메서드
    public void addReview(Review review) {
        this.reviews.add(review);

        if (review.getGatherArticle() != this) {
            review.assignGatherArticle(this);
        }
    }

    // 모집글 현재 참가 인원 업데이트
    public void assignCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public void assignGatherArticleStatus(GatherArticleStatus gatherArticleStatus) {
        this.gatherArticleStatus = gatherArticleStatus;
    }
}
