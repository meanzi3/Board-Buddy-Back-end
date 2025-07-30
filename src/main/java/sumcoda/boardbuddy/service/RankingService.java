package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.client.MemberRankingDTO;
import sumcoda.boardbuddy.dto.fetch.MemberRankingProjection;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.enumerate.RankScorePoints;
import sumcoda.boardbuddy.mapper.RankingMapper;
import sumcoda.boardbuddy.repository.MemberJdbcRepository;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.repository.comment.CommentRepository;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static sumcoda.boardbuddy.util.RankingUtil.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final MemberRepository memberRepository;

    private final GatherArticleRepository gatherArticleRepository;

    private final CommentRepository commentRepository;

    private final MemberJdbcRepository memberJdbcRepository;

    private final BadgeImageService badgeImageService;

    private final RankingMapper rankingMapper;


    /**
     * 랭킹 TOP3 조회
     * @return TOP3 MemberRankingDTO 리스트
     */
    public List<MemberRankingDTO> getTop3Rankings(){
        List<MemberRankingProjection> projections = memberRepository.findTop3RankingMembers();

        return rankingMapper.toMemberRankingDTOList(projections);
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

        // 점수 계산
        Map<Long, Double> memberScores = members.stream()
                .collect(Collectors.toMap(
                        Member::getId,
                        member -> {
                            // 지난 달 모집글 갯수
                            long gatherArticleCount = gatherArticleRepository.countGatherArticlesByMember(member, startOfLastMonth, endOfLastMonth);
                            // 지난 달 댓글 갯수
                            long commentCount = commentRepository.countCommentsByMember(member, startOfLastMonth, endOfLastMonth);
                            // 후기 카운트, 리뷰 보낸 횟수 합하여 점수 계산
                            return calculateRankScore(member, gatherArticleCount, commentCount);
                        }
                ));

        // 점수 업데이트
        memberJdbcRepository.updateMemberRankScores(memberScores);

        // 점수 별로 정렬
        List<Member> orderedByScoreMembers = memberRepository.findAllOrderedByRankScore();

        // 각 member 들의 랭킹 구하기
        Map<Long, Integer> rankUpdateMap = new HashMap<>();
        IntStream.range(0, orderedByScoreMembers.size())
                .forEach(i -> {
                    Long memberId = orderedByScoreMembers.get(i).getId();
                    Integer rank = (i < TOP_RANK_COUNT) ? i + 1 : null;
                    rankUpdateMap.put(memberId, rank);
                });

        // 랭킹 업데이트
        memberJdbcRepository.updateMemberRanks(rankUpdateMap);

        // 후기 카운트, 보낸 리뷰 카운트 초기화
        memberJdbcRepository.resetMonthlyCounts();

        // TOP3 member들의 id 담기
        List<Long> top3MemberIds = new ArrayList<>();

        for (int i = 0; i < Math.min(orderedByScoreMembers.size(), TOP_RANK_COUNT); i++) {
            top3MemberIds.add(orderedByScoreMembers.get(i).getId());
        }

        // TOP3에게 뱃지 부여
        badgeImageService.assignBadgesToTopMembersByIds(top3MemberIds, getLastMonth());
    }

    // 지난 달 시작일
    private LocalDateTime getStartOfLastMonth() {
        YearMonth lastMonth = getLastMonth();
        return lastMonth.atDay(1).atStartOfDay();
    }

    // 지난 달 종료일
    private LocalDateTime getEndOfLastMonth() {
        YearMonth lastMonth = getLastMonth();
        return lastMonth.atEndOfMonth().atTime(23, 59, 59);
    }

    // 지난달 YearMonth 구하기
    private YearMonth getLastMonth() {
        return YearMonth.now().minusMonths(1);
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
