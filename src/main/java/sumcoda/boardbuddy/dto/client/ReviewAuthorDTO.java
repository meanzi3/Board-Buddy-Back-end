package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

@Builder
public record ReviewAuthorDTO(

        String profileImageSignedURL,

        Integer rank,

        String nickname,

        Boolean hasReviewed
) {}
