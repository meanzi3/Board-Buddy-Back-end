package sumcoda.boardbuddy.dto;

import lombok.Builder;

@Builder
public record MemberRankingDTO(

        String nickname,

        String profileImageSignedURL
) {
}
