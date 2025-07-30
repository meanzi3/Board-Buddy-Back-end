package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

@Builder
public record CommentAuthorDTO(

        String nickname,

        Integer rank,

        String profileImageSignedURL
) {}
