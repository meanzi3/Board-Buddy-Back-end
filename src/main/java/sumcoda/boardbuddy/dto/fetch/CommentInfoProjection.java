package sumcoda.boardbuddy.dto.fetch;

import java.time.LocalDateTime;

public record CommentInfoProjection(
        Long id,

        Long parentId,          // 최상위 댓글이면 null

        String content,

        LocalDateTime createdAt,

        String nickname,

        Integer rank,

        String s3SavedObjectName
) {
}
