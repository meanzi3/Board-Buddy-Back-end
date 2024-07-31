package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.MemberChatRoomRole;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자가 채팅방에 참여한 시점
    @Column(nullable = false)
    private LocalDateTime joinedAt;

    // 채팅 참여자의 권한을 나타내기위한 role
    // ex) AUTHOR, PARTICIPANT
    @Column(nullable = false)
    private MemberChatRoomRole memberChatRoomRole; // 역할 추가 (관리자, 일반 사용자 등)

    // 양방향 연관관계
    // 연관관계 주인
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 양방향 연관관계
    // 연관관계 주인
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public MemberChatRoom(LocalDateTime joinedAt, MemberChatRoomRole memberChatRoomRole, Member member, ChatRoom chatRoom) {
        this.joinedAt = joinedAt;
        this.memberChatRoomRole = memberChatRoomRole;
        this.assignMember(member);
        this.assignChatRoom(chatRoom);
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static MemberChatRoom buildMemberChatRoom(LocalDateTime joinedAt, MemberChatRoomRole memberChatRoomRole, Member member, ChatRoom chatRoom) {
        return MemberChatRoom.builder()
                .joinedAt(joinedAt)
                .memberChatRoomRole(memberChatRoomRole)
                .member(member)
                .chatRoom(chatRoom)
                .build();
    }

    // MemberChatRoom N <-> 1 Member
    // 양방향 연관관계 편의 메서드
    public void assignMember(Member member) {
        if (this.member != null) {
            this.member.getMemberChatRooms().remove(this);
        }
        this.member = member;

        if (!member.getMemberChatRooms().contains(this)) {
            member.addMemberChatRoom(this);
        }
    }

    // MemberChatRoom N <-> 1 ChatRoom
    // 양방향 연관관계 편의 메서드
    public void assignChatRoom(ChatRoom chatRoom) {
        if (this.chatRoom != null) {
            this.chatRoom.getMemberChatRooms().remove(this);
        }
        this.chatRoom = chatRoom;

        if (!chatRoom.getMemberChatRooms().contains(this)) {
            chatRoom.addMemberChatRoom(this);
        }
    }

}
