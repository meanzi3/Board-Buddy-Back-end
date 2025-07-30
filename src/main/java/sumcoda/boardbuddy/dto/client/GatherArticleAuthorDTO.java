package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

@Builder
public record GatherArticleAuthorDTO(

        String nickname,

        Integer rank,

        String profileImageSignedURL,

        String description
) {
}
