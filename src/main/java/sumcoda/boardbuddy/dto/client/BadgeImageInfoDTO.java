package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

@Builder
public record BadgeImageInfoDTO(

        String badgeImageSignedURL,

        String badgeYearMonth
) {}
