package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.comment.*;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildFailureResponse;

@RestControllerAdvice
public class CommentExceptionHandler {

    // 댓글을 찾을 수 없는 경우의 예외 처리 핸들러
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentNotFoundException(CommentNotFoundException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 대댓글의 대댓글 작성 방지 예외 처리 핸들러
    @ExceptionHandler(CommentLevelException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentLevelException(CommentLevelException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 댓글 저장 예외 처리 핸들러
    @ExceptionHandler(CommentSaveException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentSaveException(CommentSaveException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 댓글 업데이트 예외 처리 핸들러
    @ExceptionHandler(CommentUpdateException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentUpdateException(CommentUpdateException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 모집글에 속하지 않는 댓글 예외 처리 핸들러
    @ExceptionHandler(CommentNotInGatherArticleException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentNotInGatherArticleException(CommentNotInGatherArticleException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 댓글 접근 권한 예외 처리 핸들러
    @ExceptionHandler(CommentAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentAccessException(CommentAccessException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
