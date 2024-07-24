package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.enumerate.RankScorePoints;
import sumcoda.boardbuddy.repository.MemberJdbcRepository;
import sumcoda.boardbuddy.repository.MemberRepository;
import sumcoda.boardbuddy.repository.comment.CommentRepository;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final MemberRepository memberRepository;

    private final GatherArticleRepository gatherArticleRepository;

    private final CommentRepository commentRepository;

    private final MemberJdbcRepository memberJdbcRepository;


    /**
     * 랭킹 조회
     * @return TOP3 RankingsDTO list
     */
    @Transactional
    public List<MemberResponse.RankingsDTO> getTop3Rankings(){

        return memberRepository.findTop3RankingMembers();
    }

    /**
    * 랭킹 집계 - 매월 1일 00시 스케줄링
    *
    */
    @Transactional
    @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 00시
    public void calculateMonthlyRankings() {

        log.info("Ranking calculation started.");

        LocalDateTime startOfLastMonth = getStartOfLastMonth();
        LocalDateTime endOfLastMonth = getEndOfLastMonth();

        List<Member> members = memberRepository.findAll();
        Map<Long, Double> memberScores = new HashMap<>();

        // 점수 계산
        for (Member member : members) {
            // 지난 달 모집글 갯수
            long gatherArticleCount = gatherArticleRepository.countGatherArticlesByMember(member, startOfLastMonth, endOfLastMonth);
            // 지난 달 댓글 갯수
            long commentCount = commentRepository.countCommentsByMember(member, startOfLastMonth, endOfLastMonth);
            // 후기 카운트, 리뷰 보낸 횟수 합하여 점수 계산
            double rankScore = calculateRankScore(member, gatherArticleCount, commentCount);
            memberScores.put(member.getId(), rankScore);
        }

        // 점수 업데이트
        memberJdbcRepository.updateMemberRankScores(memberScores);

        // 점수 별로 정렬
        List<Member> orderedByScoreMembers = memberRepository.findAllOrderedByRankScore();

        // 랭킹 업데이트
        Map<Long, Integer> rankUpdateMap = new HashMap<>();

        for (int i = 0; i < orderedByScoreMembers.size(); i++) {
            // TODO : 1 ~ 3등 뱃지 부여
            Member member = orderedByScoreMembers.get(i);
            if (i == 0) {
              rankUpdateMap.put(member.getId(), 1);
            } else if (i == 1) {
              rankUpdateMap.put(member.getId(), 2);
            } else if (i == 2) {
              rankUpdateMap.put(member.getId(), 3);
            } else {
              rankUpdateMap.put(member.getId(), null);
            }
        }

        // 랭킹 업데이트
        memberJdbcRepository.updateMemberRanks(rankUpdateMap);

        // 후기 카운트, 보낸 리뷰 카운트 초기화
        memberJdbcRepository.resetMonthlyCounts();

    }

    // 지난 달 시작일
    private LocalDateTime getStartOfLastMonth() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        return lastMonth.atDay(1).atStartOfDay();
    }

    // 지난 달 종료일
    private LocalDateTime getEndOfLastMonth() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        return lastMonth.atEndOfMonth().atTime(23, 59, 59);
    }

    // 점수 계산
    private double calculateRankScore(Member member, long gatherArticleCount, long commentCount) {
        double score = 0.0;
        score += member.getMonthlyExcellentCount() * RankScorePoints.EXCELLENT_REVIEW_SCORE.getScore();
        score += member.getMonthlyGoodCount() * RankScorePoints.GOOD_REVIEW_SCORE.getScore();
        score += member.getMonthlyBadCount() * RankScorePoints.BAD_REVIEW_SCORE.getScore();
        score += member.getMonthlyNoShowCount() * RankScorePoints.NOSHOW_REVIEW_SCORE.getScore();
        score += member.getMonthlySendReviewCount() * RankScorePoints.SEND_REVIEW_SCORE.getScore();
        score += gatherArticleCount * RankScorePoints.GATHER_ARTICLE_SCORE.getScore();
        score += commentCount * RankScorePoints.COMMENT_SCORE.getScore();
        return score;
    }
}
