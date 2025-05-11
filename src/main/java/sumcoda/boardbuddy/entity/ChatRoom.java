package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 양방향 연관관계
    @OneToOne(mappedBy = "chatRoom")
    private GatherArticle gatherArticle;

    // 양방향 연관관계
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    // 양방향 연관관계
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

    @Builder
    public ChatRoom(GatherArticle gatherArticle) {
        this.assignGatherArticle(gatherArticle);
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static ChatRoom buildChatRoom(GatherArticle gatherArticle) {
        return ChatRoom.builder()
                .gatherArticle(gatherArticle)
                .build();
    }

    // ChatRoom 1 <-> 1 GatherArticle
    // 양방향 연관관계 편의 메서드
    public void assignGatherArticle(GatherArticle gatherArticle) {
        if (this.gatherArticle != null) {
            this.gatherArticle.assignChatRoom(null);
        }
        this.gatherArticle = gatherArticle;
        if (gatherArticle != null && gatherArticle.getChatRoom() != this) {
            gatherArticle.assignChatRoom(this);
        }
    }

    // ChatRoom 1 <-> N ChatMessage
    // 양방향 연관관계 편의 메서드
    public void addChatMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);

        if (chatMessage.getChatRoom() != this) {
            chatMessage.assignChatRoom(this);
        }
    }

    // ChatRoom 1 <-> N MemberChatRoom
    // 양방향 연관관계 편의 메서드
    public void addMemberChatRoom(MemberChatRoom memberChatRoom) {
        this.memberChatRooms.add(memberChatRoom);

        if (memberChatRoom.getChatRoom() != this) {
            memberChatRoom.assignChatRoom(this);
        }
    }
}
