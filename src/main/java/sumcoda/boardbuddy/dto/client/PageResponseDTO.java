package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponseDTO<T>(

        List<T> dataList,

        Boolean hasMore,

        String nextCursor
) {}
