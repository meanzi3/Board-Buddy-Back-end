package sumcoda.boardbuddy.dto.fetch;

import java.time.Instant;

public record ChatMessageLastSentInfoProjection(

        String content,

        Instant sentAt
) {}
