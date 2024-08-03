package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumcoda.boardbuddy.dto.CommentRequest;
import sumcoda.boardbuddy.dto.CommentResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.CommentService;

import java.util.List;
import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithPairKeyData;
import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithoutData;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성 요청
     *
     * @param gatherArticleId 모집글 ID
     * @param parentId        부모 댓글 ID (없을 경우 null)
     * @param createDTO       댓글 작성 요청 DTO
     * @param username        사용자 이름
     * @return 성공 응답
     */
    @PostMapping(value = {"/api/gather-articles/{gatherArticleId}/comments", "/api/gather-articles/{gatherArticleId}/comments/{parentId}"})
    public ResponseEntity<ApiResponse<Void>> createComment(
            @PathVariable Long gatherArticleId,
            @PathVariable(required = false) Long parentId,
            @RequestBody CommentRequest.CreateDTO createDTO,
            @RequestAttribute String username) {
        log.info("createComment is working");

        commentService.createComment(gatherArticleId, parentId, createDTO, username);

        return buildSuccessResponseWithoutData("댓글 작성을 성공하였습니다.", HttpStatus.CREATED);
    }

    /**
     * 댓글 조회 요청
     *
     * @param gatherArticleId 모집글 ID
     * @param username        사용자 이름
     * @return 댓글 리스트 응답
     */
    @GetMapping("/api/gather-articles/{gatherArticleId}/comments")
    public ResponseEntity<ApiResponse<Map<String, List<CommentResponse.InfoDTO>>>> getComments(
            @PathVariable Long gatherArticleId,
            @RequestAttribute String username) {
        log.info("getComments is working");

        List<CommentResponse.InfoDTO> comments = commentService.getComments(gatherArticleId, username);

        return buildSuccessResponseWithPairKeyData("comments", comments, "댓글 조회를 성공하였습니다.", HttpStatus.OK);
    }

    /**
     * 댓글 수정 요청
     *
     * @param gatherArticleId 모집글 ID
     * @param commentId       댓글 ID
     * @param updateDTO       댓글 수정 요청 DTO
     * @param username        사용자 이름
     * @return 성공 응답
     */
    @PutMapping("/api/gather-articles/{gatherArticleId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @PathVariable Long gatherArticleId,
            @PathVariable Long commentId,
            @RequestBody CommentRequest.UpdateDTO updateDTO,
            @RequestAttribute String username) {
        log.info("updateComment is working");

        commentService.updateComment(gatherArticleId, commentId, updateDTO, username);

        return buildSuccessResponseWithoutData("댓글 수정을 성공하였습니다.", HttpStatus.OK);
    }

    /**
     * 댓글 삭제 요청
     *
     * @param gatherArticleId 모집글 ID
     * @param commentId       댓글 ID
     * @param username        사용자 이름
     * @return 성공 응답
     */
    @DeleteMapping("/api/gather-articles/{gatherArticleId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long gatherArticleId,
            @PathVariable Long commentId,
            @RequestAttribute String username) {
        log.info("deleteComment is working");

        commentService.deleteComment(gatherArticleId, commentId, username);

        return buildSuccessResponseWithoutData("댓글 삭제를 성공하였습니다.", HttpStatus.OK);
    }
}
