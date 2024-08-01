package sumcoda.boardbuddy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.entity.GatherArticle;
import sumcoda.boardbuddy.entity.MemberGatherArticle;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;
import sumcoda.boardbuddy.exception.gatherArticle.job.GatherArticleScheduleExecuteException;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;

@RequiredArgsConstructor
@Slf4j
@Component
public class GatherArticleStatusUpdateJob implements Job{

    private final GatherArticleRepository gatherArticleRepository;

    // 지정된 시간에 실행되는 메서드
    @Override
    @Transactional
    public void execute(JobExecutionContext context) {

        // gatherArticleId 찾기
        Long gatherArticleId = context.getJobDetail().getJobDataMap().getLong("gatherArticleId");

        // gatherArticle 찾기
        GatherArticle gatherArticle = gatherArticleRepository.findById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleScheduleExecuteException("스케줄링 작업 실행 오류: 해당하는 id의 모집글을 찾을 수 없습니다. 작업 요청한 gatherArticleId - " + gatherArticleId));

        // 상태 변경 -> completed
        gatherArticle.assignGatherArticleStatus(GatherArticleStatus.COMPLETED);

        log.info("Status updated to COMPLETED for gather article ID: {} ", gatherArticleId);

        // 해당 모집글의 모든 참가자들의 참가 횟수 1 증가
        gatherArticle.getMemberGatherArticles().stream()
                .map(MemberGatherArticle::getMember)
                .forEach(member -> member.assignJoinCount(member.getJoinCount() + 1));
    }
}
