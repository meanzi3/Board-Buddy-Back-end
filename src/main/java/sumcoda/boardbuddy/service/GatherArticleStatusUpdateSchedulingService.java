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
@Transactional
public class GatherArticleStatusUpdateSchedulingService {

    private static final String JOB_GROUP = "gatherArticle";
    private static final String TRIGGER_GROUP = "gatherArticle";
    private final Scheduler scheduler;

    /**
     * 최초 또는 중복 방지 후, 모임 종료 시간에 모집글 상태를 바꾸는 스케줄링 작업
     *
     * @param gatherArticleId   모집글 id
     * @param endDateTime       모임 종료 시간
     */
    public void scheduleStatusUpdateJob(Long gatherArticleId, LocalDateTime endDateTime) {

        JobKey jobKey = JobKey.jobKey("job_" + gatherArticleId, JOB_GROUP);
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + gatherArticleId, TRIGGER_GROUP);

        try {
            // 이미 존재하면 그대로 두거나, 필요에 따라 reschedule 로직 호출
            if (scheduler.checkExists(jobKey)) {
                log.debug("Job {} already scheduled, skip.", jobKey);
                return;
            }

            // JobDetail 생성 : job에 대한 정보 정의
            JobDetail jobDetail = JobBuilder.newJob(GatherArticleStatusUpdateJob.class)
                    .withIdentity(jobKey)
                    .usingJobData("gatherArticleId", gatherArticleId)
                    .storeDurably(true)             // DB에 남김
                    .requestRecovery(true)      // 비정상 종료 시 1회 복구
                    .build();

            // Trigger 생성 : job을 endDateTime에 실행하도록 설정
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withMisfireHandlingInstructionFireNow())  // 미스파이어 시 즉시 1회
                    .build();

            // 스케줄러에 JobDetail과 Trigger 등록
            scheduler.scheduleJob(jobDetail, trigger);

            // 스케줄링된 모집글 id, 시간 확인 로그
            Date scheduledTime = trigger.getStartTime();
            log.info("Job scheduled successfully for gather article ID: {} at {}", gatherArticleId, scheduledTime);

        } catch (SchedulerException e) {
            // 스케줄링 오류 처리
            throw new GatherArticleSchedulingException("스케줄링 작업 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 새로 입력 받은 모임 종료 시간에 모집글 상태를 바꾸도록 리스캐줄링 작업
     *
     * @param gatherArticleId   모집글 id
     * @param newEndDateTime    세로운 모임 종료 시간
     */
    public void rescheduleStatusUpdateJob(Long gatherArticleId, LocalDateTime newEndDateTime) {
        try {
            // 트리거 키 찾기
            TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + gatherArticleId, TRIGGER_GROUP);

            // 새로운 트리거 생성
            Trigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(Date.from(newEndDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withMisfireHandlingInstructionFireNow())
                    .build();

            // 기존 트리거를 새로운 트리거로 대체
            scheduler.rescheduleJob(triggerKey, newTrigger);

            // 리스케줄링된 모집글 id, 시간 확인 로그
            Date newScheduledTime = newTrigger.getStartTime();
            log.info("Job rescheduled successfully for gather article ID: {} at {}", gatherArticleId, newScheduledTime);

        } catch (SchedulerException e) {
            // 리스케줄링 오류 처리
            throw new GatherArticleReschedulingException("스케줄링 작업 수정 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 모집글이 삭제 되었을 때, 스케줄링 되어있던 작업을 언스케줄링
     *
     * @param gatherArticleId   모집글 id
     */
    public void unscheduleStatusUpdateJob(Long gatherArticleId) {
        try {
            JobKey jobKey = new JobKey("job_" + gatherArticleId, JOB_GROUP);
            TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + gatherArticleId, TRIGGER_GROUP);

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
