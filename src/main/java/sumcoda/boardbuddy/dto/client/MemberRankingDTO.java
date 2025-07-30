package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

@Builder
public record MemberRankingDTO(

        String nickname,

        String profileImageSignedURL
) {
}
