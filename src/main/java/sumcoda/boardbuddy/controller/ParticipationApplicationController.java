package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumcoda.boardbuddy.dto.ParticipationApplicationResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.enumerate.MessageType;
import sumcoda.boardbuddy.service.ChatMessageService;
import sumcoda.boardbuddy.service.ChatRoomService;
import sumcoda.boardbuddy.service.NotificationService;
import sumcoda.boardbuddy.service.ParticipationApplicationService;

import java.util.List;
import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.*;

@RestController
@RequiredArgsConstructor
public class ParticipationApplicationController {

    private final ParticipationApplicationService participationApplicationService;

    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    private final NotificationService notificationService;

    /**
     * 모집글 참가 신청 요청
     *
     * @param gatherArticleId 모집글 ID
     * @param username 참가신청 요청을 보내는 사용자 아이디
     **/
    @PostMapping("/api/gather-articles/{gatherArticleId}/participation")
    public ResponseEntity<ApiResponse<Void>> applyParticipation(@PathVariable Long gatherArticleId, @RequestAttribute String username) {
        participationApplicationService.applyParticipation(gatherArticleId, username);

        notificationService.notifyApplyParticipation(gatherArticleId, username);

        return buildSuccessResponseWithoutData("해당 모집글에 참가 신청이 완료되었습니다.", HttpStatus.OK);
    }

    /**
     * 모집글 참가 신청 승인 요청
     *
     * @param gatherArticleId 모집글 Id
     * @param participationApplicationId 참가 신청 Id
     * @param username 승인 요청을 보내는 모집글 작성자 아이디
     * @param applicantNickname 참가 신청을 했던 사용자의 아이디
     **/
    @PutMapping("/api/gather-articles/{gatherArticleId}/participation/{participationApplicationId}/approval")
    public ResponseEntity<ApiResponse<Void>> approveParticipationApplication(@PathVariable Long gatherArticleId, @PathVariable Long participationApplicationId, @RequestAttribute String username, @RequestParam String applicantNickname) {

        String applicantUsername = participationApplicationService.approveParticipationApplication(gatherArticleId, participationApplicationId, username, applicantNickname);

        // ChatRoom 입장 처리
        Pair<Long, String> chatRoomIdAndNicknamePair = chatRoomService.enterChatRoom(gatherArticleId, applicantUsername);

        // 채팅방 입장 메세지 발행 및 전송
        chatMessageService.publishEnterOrExitChatMessage(chatRoomIdAndNicknamePair, MessageType.ENTER);

        notificationService.notifyApproveParticipation(applicantNickname, gatherArticleId);

        return buildSuccessResponseWithoutData(applicantNickname + "님의 참가 신청을 승인 했습니다.", HttpStatus.OK);
    }

    /**
     * 모집글 참가 신청 거절 요청
     *
     * @param gatherArticleId 모집글 Id
     * @param participationApplicationId 참가 신청 Id
     * @param username 거절 요청을 보내는 모집글 작성자 아이디
     * @param applicantNickname 참가 신청을 했던 사용자의 아이디
     **/
    @PutMapping("/api/gather-articles/{gatherArticleId}/participation/{participationApplicationId}/rejection")
    public ResponseEntity<ApiResponse<Void>> rejectParticipationApplication(@PathVariable Long gatherArticleId, @PathVariable Long participationApplicationId, @RequestAttribute String username, @RequestParam String applicantNickname) {

        participationApplicationService.rejectParticipationApplication(gatherArticleId, participationApplicationId, username);

        notificationService.notifyRejectParticipation(applicantNickname, gatherArticleId);

        return buildSuccessResponseWithoutData(applicantNickname + "님의 참가 신청을 거절 했습니다.", HttpStatus.OK);
    }

    /**
     * 모집글 참가 신청 취소 요청
     *
     * @param gatherArticleId 모집글 Id
     * @param username 참가신청을 취소하는 사용자 아이디
     **/
    @PutMapping("/api/gather-articles/{gatherArticleId}/participation")
    public ResponseEntity<ApiResponse<Void>> cancelParticipationApplication(@PathVariable Long gatherArticleId, @RequestAttribute String username) {

        Boolean isMemberParticipant = participationApplicationService.cancelParticipationApplication(gatherArticleId, username);

        // 만약 참가 취소하는 사용자가 참가 승인으로 인하여, 모집글에 참여중인 사용자라면,
        if (isMemberParticipant) {
            // ChatRoom 퇴장 처리
            Pair<Long, String> chatRoomIdAndNicknamePair = chatRoomService.leaveChatRoom(gatherArticleId, username);

            // 채팅방 퇴장 메세지 발행 및 전송
            chatMessageService.publishEnterOrExitChatMessage(chatRoomIdAndNicknamePair, MessageType.EXIT);
        }

        notificationService.notifyCancelParticipation(gatherArticleId, username);

        return buildSuccessResponseWithoutData("해당 모집글의 참가 신청을 취소했습니다.", HttpStatus.OK);
    }

    /**
     * 모집글 참가 신청 목록 조회 요청
     *
     * @param gatherArticleId 모집글 Id
     * @param username 참가 신청 목록 조회 요청을 보내는 모집글 작성자 아이디
     * @return 모집글 참가 신청중인 사용자 목록
     **/
    @GetMapping("/api/gather-articles/{gatherArticleId}/participation")
    public ResponseEntity<ApiResponse<Map<String, List<ParticipationApplicationResponse.InfoDTO>>>> getParticipationAppliedMemberList(@PathVariable Long gatherArticleId, @RequestAttribute String username) {

        List<ParticipationApplicationResponse.InfoDTO> participationAppliedMemberList = participationApplicationService.getParticipationAppliedMemberList(gatherArticleId, username);

        return buildSuccessResponseWithPairKeyData("participationAppliedMemberList", participationAppliedMemberList, "해당 모집글의 참가 신청 목록을 성공적으로 조회했습니다.", HttpStatus.OK);
    }
}
