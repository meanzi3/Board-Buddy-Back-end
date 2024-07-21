package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.MessageType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    // 연관관계 주인
    // 단방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 연관관계 주인
    // 양방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(String content, MessageType messageType, Member member, ChatRoom chatRoom) {
        this.content = content;
        this.messageType = messageType;
        this.assignMember(member);
        this.assignChatRoom(chatRoom);
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static ChatMessage buildChatMessage(String content, MessageType messageType, Member member, ChatRoom chatRoom) {
        return ChatMessage.builder()
                .content(content)
                .messageType(messageType)
                .member(member)
                .chatRoom(chatRoom)
                .build();
    }

    // ChatMessage N -> 1 Member
    // 단방향 연관관계 메서드
    public void assignMember(Member member) {
        this.member = member;
    }

    // ChatMessage N <-> 1 ChatRoom
    // 양방향 연관관계 편의 메서드
    public void assignChatRoom(ChatRoom chatRoom) {
        if (this.chatRoom != null) {
            this.chatRoom.getChatMessages().remove(this);
        }
        this.chatRoom = chatRoom;

        if (!chatRoom.getChatMessages().contains(this)) {
            chatRoom.addChatMessage(this);
        }
    }
}
