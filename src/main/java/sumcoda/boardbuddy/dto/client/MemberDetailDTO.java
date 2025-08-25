package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

import java.util.List;

@Builder
public record MemberDetailDTO(

        String profileImageSignedURL,

        String description,

        Integer rank,

        Double buddyScore,

        List<BadgeImageInfoDTO> badges,

        Integer joinCount,

        Integer totalExcellentCount,

        Integer totalGoodCount,

        Integer totalBadCount
) {
}
