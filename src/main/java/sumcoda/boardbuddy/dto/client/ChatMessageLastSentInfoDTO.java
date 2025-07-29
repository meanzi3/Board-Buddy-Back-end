package sumcoda.boardbuddy.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageLastSentInfoDTO(

        String content,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime sentAt
) {}
