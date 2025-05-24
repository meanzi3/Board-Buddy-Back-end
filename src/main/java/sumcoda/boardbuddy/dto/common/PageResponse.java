package sumcoda.boardbuddy.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> dataList;

    private Boolean hasMore;

    private String nextCursor;

    @Builder
    public PageResponse(List<T> dataList, Boolean hasMore, String nextCursor) {
        this.dataList = dataList;
        this.hasMore = hasMore;
        this.nextCursor = nextCursor;
    }
}
