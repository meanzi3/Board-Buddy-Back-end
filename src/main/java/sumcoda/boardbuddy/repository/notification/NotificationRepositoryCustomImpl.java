package sumcoda.boardbuddy.repository.notification;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.NotificationResponse;

import java.util.List;

import static sumcoda.boardbuddy.entity.QMember.member;
import static sumcoda.boardbuddy.entity.QNotification.notification;

@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 유저의 알림 내역을 최신순으로 조회
     *
     * @param username 사용자 아이디
     * @return 최신순으로 정렬된 알림 내역
     **/
    @Override
    public List< NotificationResponse.NotificationDTO> findNotificationByMemberUsername(String username) {
        return jpaQueryFactory.select(Projections.fields(NotificationResponse.NotificationDTO.class,
                        notification.message,
                        notification.createdAt))
                .from(notification)
                .join(notification.member, member)
                .where(member.username.eq(username))
                .orderBy(notification.createdAt.desc())
                .fetch();
    }
}
