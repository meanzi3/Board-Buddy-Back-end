package sumcoda.boardbuddy.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import sumcoda.boardbuddy.enumerate.MessageType;

import java.time.LocalDateTime;

@Builder
public record ChatMessageItemInfoDTO(

        Long id,

        String content,

        String nickname,

        String profileImageSignedURL,

        Integer rank,

        MessageType messageType,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        LocalDateTime sentAt
) {}
