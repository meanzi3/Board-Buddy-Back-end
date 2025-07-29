package sumcoda.boardbuddy.dto.fetch;

import sumcoda.boardbuddy.enumerate.MessageType;

import java.time.Instant;

public record ChatMessageItemInfoProjection(

        Long id,

        String content,

        String nickname,

        String s3SavedObjectName,

        Integer rank,

        MessageType messageType,

        Instant sentAt
) {}
