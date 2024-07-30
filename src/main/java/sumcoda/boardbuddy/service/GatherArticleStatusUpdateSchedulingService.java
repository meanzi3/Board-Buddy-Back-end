package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.exception.gatherArticle.job.GatherArticleReschedulingException;
import sumcoda.boardbuddy.exception.gatherArticle.job.GatherArticleSchedulingException;
import sumcoda.boardbuddy.exception.gatherArticle.job.GatherArticleUnschedulingException;
import sumcoda.boardbuddy.job.GatherArticleStatusUpdateJob;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GatherArticleStatusUpdateSchedulingService {

    private final Scheduler scheduler;

    public void scheduleStatusUpdateJob(Long gatherArticleId, LocalDateTime endDateTime) {
        // JobDetail 생성 : job에 대한 정보 정의
        JobDetail jobDetail = JobBuilder.newJob(GatherArticleStatusUpdateJob.class)
                .withIdentity("job_" + gatherArticleId)
                .usingJobData("gatherArticleId", gatherArticleId)
                .build();

        // Trigger 생성 : job을 endDateTime에 실행하도록 설정
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger_" + gatherArticleId)
                .startAt(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        try {
            // 스케줄러에 JobDetail과 Trigger 등록
            scheduler.scheduleJob(jobDetail, trigger);

            Date scheduledTime = trigger.getStartTime();
            log.info("Job scheduled successfully for gather article ID: {} at {}", gatherArticleId, scheduledTime);
        } catch (SchedulerException e) {
            // 스케줄링 오류 처리
            throw new GatherArticleSchedulingException("스케줄링 작업 중 오류가 발생했습니다.", e);
        }
    }

    public void rescheduleStatusUpdateJob(Long gatherArticleId, LocalDateTime newEndDateTime) {
        try {
            // 트리거 키 찾기
            TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + gatherArticleId);

            // 새로운 트리거 생성
            Trigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(Date.from(newEndDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .build();

            // 기존 트리거를 새로운 트리거로 대체
            scheduler.rescheduleJob(triggerKey, newTrigger);
            log.info("Job rescheduled successfully for gather article ID: {} at {}", gatherArticleId, newEndDateTime);
        } catch (SchedulerException e) {
            // 리스케줄링 오류 처리
            throw new GatherArticleReschedulingException("스케줄링 작업 수정 중 오류가 발생했습니다.", e);
        }
    }

    public void unscheduleStatusUpdateJob(Long gatherArticleId) {
        try {
            JobKey jobKey = new JobKey("job_" + gatherArticleId);
            TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + gatherArticleId);

            // 트리거 제거
            if (scheduler.checkExists(triggerKey)) {
              scheduler.unscheduleJob(triggerKey);
              log.info("Trigger for gather article ID {} successfully unscheduled.", gatherArticleId);
            }

          // 작업 제거
            if (scheduler.checkExists(jobKey)) {
              scheduler.deleteJob(jobKey);
              log.info("Job for gather article ID {} successfully deleted.", gatherArticleId);
            }
        } catch (SchedulerException e) {
            // 언스케줄링 오류 처리
            throw new GatherArticleUnschedulingException("기존 스케줄링 작업을 취소하는 중 오류가 발생했습니다.", e);
        }
      }

}
