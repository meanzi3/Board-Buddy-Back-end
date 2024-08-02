package sumcoda.boardbuddy.repository.notification;

import sumcoda.boardbuddy.entity.Notification;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> findNotificationByMemberUsername(String username);
}
