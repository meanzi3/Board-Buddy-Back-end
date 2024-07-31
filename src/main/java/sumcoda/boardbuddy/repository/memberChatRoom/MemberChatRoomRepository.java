package sumcoda.boardbuddy.repository.memberChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.MemberChatRoom;

@Repository
public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long>, MemberChatRoomRepositoryCustom {

    Boolean existsByChatRoomIdAndMemberUsername(Long chatRoomId, String username);

    Boolean existsByChatRoomIdAndMemberNickname(Long chatRoomId, String nickname);
}
