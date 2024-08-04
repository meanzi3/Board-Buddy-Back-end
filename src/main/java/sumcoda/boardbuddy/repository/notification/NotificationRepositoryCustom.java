package sumcoda.boardbuddy.repository.notification;

import sumcoda.boardbuddy.dto.NotificationResponse;

import java.util.List;

public interface NotificationRepositoryCustom {

    List< NotificationResponse.NotificationDTO> findNotificationByMemberUsername(String username);
}
