package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.dto.ReviewRequest;
import sumcoda.boardbuddy.dto.ReviewResponse;
import sumcoda.boardbuddy.entity.GatherArticle;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.MemberGatherArticle;
import sumcoda.boardbuddy.entity.Review;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;
import sumcoda.boardbuddy.enumerate.ReviewType;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleNotCompletedException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleNotFoundException;
import sumcoda.boardbuddy.exception.memberGatherArticle.MemberNotJoinedGatherArticleException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.exception.review.ReviewAlreadyExistsException;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.repository.memberGatherArticle.MemberGatherArticleRepository;
import sumcoda.boardbuddy.repository.review.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final GatherArticleRepository gatherArticleRepository;

    private final MemberRepository memberRepository;

    private final MemberGatherArticleRepository memberGatherArticleRepository;

    private final ReviewRepository reviewRepository;

    /**
     * 모집글에 참가한 유저 리스트 조회 요청 캐치
     *
     * @param gatherArticleId 모집글 Id
     * @param username 로그인 사용자 아이디
     **/
    public List<ReviewResponse.ReviewDTO> getParticipatedList(Long gatherArticleId, String username) {
        GatherArticleResponse.StatusDTO statusDTO = gatherArticleRepository.findStatusDTOById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("해당 모집글을 찾을 수 없습니다."));

        if (statusDTO.getStatus() != GatherArticleStatus.COMPLETED) {
            throw new GatherArticleNotCompletedException("모임이 종료된 모집글만 조회할 수 있습니다.");
        }

        return memberGatherArticleRepository.findParticipantsExcludingUsername(gatherArticleId, username);
    }

    /**
     * 리뷰 보내기 요청 캐치
     *
     * @param gatherArticleId 모집글 Id
     * @param reviewDTO 리뷰를 받는 유저 닉네임과 리뷰 타입을 담은 dto
     * @param username 로그인 사용자 아이디
     **/
    @Transactional
    public void sendReview(Long gatherArticleId, ReviewRequest.ReviewDTO reviewDTO, String username) {
        MemberGatherArticle memberGatherArticle = memberGatherArticleRepository.findByGatherArticleIdAndMemberUsername(gatherArticleId, username)
                .orElseThrow(() -> new MemberNotJoinedGatherArticleException("해당 유저는 해당 모집글에 참여하지 않았습니다."));

        GatherArticle gatherArticle = gatherArticleRepository.findById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("해당 모집글을 찾을 수 없습니다."));

        // 해당 모집글의 상태가 completed 인지 확인
        if (gatherArticle.getGatherArticleStatus() != GatherArticleStatus.COMPLETED) {
            throw new GatherArticleNotCompletedException("모임이 종료된 모집글만 리뷰를 보낼 수 있습니다.");
        }

        //리뷰 보내는 유저 조회
        Member reviewer = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("리뷰를 보내는 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        // 리뷰를 보내는 유저가 해당 모집글에 참가했는지 (Role이 있는지) 확인
        if (!memberGatherArticleRepository.isHasRole(gatherArticleId, username)) {
            throw new MemberNotJoinedGatherArticleException("리뷰를 보낼 권한이 없습니다.");
        }

        // 리뷰 받는 유저 조회
        Member reviewee = memberRepository.findByNickname(reviewDTO.getNickname())
                .orElseThrow(() -> new MemberRetrievalException("리뷰를 받는 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        // 이미 리뷰를 보냈는지 확인
        if (reviewRepository.existsByReviewerAndRevieweeAndGatherArticle(reviewer, reviewee, gatherArticle)) {
            throw new ReviewAlreadyExistsException("이미 해당 유저에게 리뷰를 보냈습니다.");
        }
        ReviewType reviewType = ReviewType.valueOf(String.valueOf(reviewDTO.getReview()));

        Review review = Review.buildReview(
                reviewer,
                reviewee,
                gatherArticle,
                true
        );

        reviewRepository.save(review);

        incrementReviewCounts(reviewee, reviewType, gatherArticleId);
        incrementSendReviewCount(reviewer);
        updateBuddyScore(reviewee, reviewType);
    }

    /**
     * 각 리뷰 카운트 증가 메서드
     *
     * @param reviewee 리뷰를 받는 유저
     * @param reviewType 리뷰 타입
     * @param gatherArticleId 모집글 Id
     **/
    private void incrementReviewCounts(Member reviewee, ReviewType reviewType, Long gatherArticleId) {
        Integer newMonthlyExcellentCount = reviewee.getMonthlyExcellentCount();
        Integer newTotalExcellentCount = reviewee.getTotalExcellentCount();
        Integer newMonthlyGoodCount = reviewee.getMonthlyGoodCount();
        Integer newTotalGoodCount = reviewee.getTotalGoodCount();
        Integer newMonthlyBadCount = reviewee.getMonthlyBadCount();
        Integer newTotalBadCount = reviewee.getTotalBadCount();
        Integer newMonthlyNoShowCount = reviewee.getMonthlyNoShowCount();

        switch (reviewType) {
            case EXCELLENT:
                newMonthlyExcellentCount++;
                newTotalExcellentCount++;
                break;
            case GOOD:
                newMonthlyGoodCount++;
                newTotalGoodCount++;
                break;
            case BAD:
                newMonthlyBadCount++;
                newTotalBadCount++;
                break;
            case NOSHOW:
                newMonthlyNoShowCount++;
                adjustReceiveNoShowCount(gatherArticleId, reviewee);
                break;
        }

        reviewee.assignReviewCount(newMonthlyExcellentCount, newTotalExcellentCount, newMonthlyGoodCount, newTotalGoodCount, newMonthlyBadCount, newTotalBadCount, newMonthlyNoShowCount);
    }

    /**
     * 리뷰 보낸 횟수 증가 메서드
     *
     * @param reviewer 리뷰를 보낸 유저
     **/
    private void incrementSendReviewCount(Member reviewer) {
        Integer newSendReviewCount = reviewer.getMonthlySendReviewCount() + 1;

        reviewer.assignSendReviewCount(newSendReviewCount);
    }

    /**
     * 받은 리뷰가 노쇼예요면 해당 유저의 노쇼 카운트를 확인하고 증감하는 메서드
     *
     * @param gatherArticleId 모집글 Id
     * @param reviewee 리뷰를 받은 유저
     **/
    private void adjustReceiveNoShowCount(Long gatherArticleId, Member reviewee) {
        MemberGatherArticle memberGatherArticle = memberGatherArticleRepository.findByGatherArticleIdAndMemberUsername(gatherArticleId, reviewee.getUsername())
                .orElseThrow(() -> new MemberNotJoinedGatherArticleException("해당 유저는 해당 모집글에 참여하지 않았습니다."));

        GatherArticle gatherArticle = gatherArticleRepository.findById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("해당 모집글을 찾을 수 없습니다."));

        memberGatherArticle.assignReceiveNoShowCount(memberGatherArticle.getReceiveNoShowCount() + 1);

        // 노쇼예요 횟수가 모집글 참가인원의 절반 이상(본인 제외)이 되면 참가 횟수 -1
        if (memberGatherArticle.getReceiveNoShowCount() >= (gatherArticle.getCurrentParticipants() - 1) / 2) {
            reviewee.assignJoinCount(reviewee.getJoinCount() - 1);
            memberGatherArticle.assignReceiveNoShowCount(-1);
        }
    }

    /**
     * 버디지수 업데이트 메서드
     *
     * @param reviewee 리뷰를 받는 유저
     * @param reviewType 리뷰 타입
     **/
    private void updateBuddyScore(Member reviewee, ReviewType reviewType) {
        // 리뷰 타입에 따라 얻는 버디 지수
        double gettingBuddyScore = reviewType.getScore();

        // 새로 계산된 버디 지수
        double newBuddyScore = reviewee.getBuddyScore() + gettingBuddyScore;

        // 버디 지수 업데이트
        reviewee.assignBuddyScore(newBuddyScore);
    }
}
