package sumcoda.boardbuddy.repository.chatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import sumcoda.boardbuddy.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    Optional<ChatRoom> findByGatherArticleId(Long gatherArticleId);

    Optional<ChatRoom> findById(Long chatRoomId);

    boolean existsById(Long chatRoomId);
}
