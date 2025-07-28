package sumcoda.boardbuddy.dto;

import lombok.Builder;

@Builder
public record GatherArticleAuthorDTO(

        String nickname,

        Integer rank,

        String profileImageSignedURL,

        String description
) {
}
