package sumcoda.boardbuddy.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentInfoDTO(

        Long id,

        CommentAuthorDTO author,

        String content,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<CommentInfoDTO> children
) {
}
