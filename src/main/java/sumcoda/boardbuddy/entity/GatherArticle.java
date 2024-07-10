package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // 해당 모집글 제한 인원
    @Column(nullable = false)
    private Integer memberCount;

    // 모집글 설명
    @Column(nullable = false)
    private String description;

    // 해당 방의 만나는 시간
    @Column(nullable = false)
    private LocalDateTime meetingDate;

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
    public GatherArticle(String title, Integer memberCount, String description, LocalDateTime meetingDate, String meetingLocation) {
        this.title = title;
        this.memberCount = memberCount;
        this.description = description;
        this.meetingDate = meetingDate;
        this.meetingLocation = meetingLocation;
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static GatherArticle createGatherArticle(String title, Integer memberCount, String description, LocalDateTime meetingDate, String meetingLocation) {
        return GatherArticle.builder()
                .title(title)
                .memberCount(memberCount)
                .description(description)
                .meetingDate(meetingDate)
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
