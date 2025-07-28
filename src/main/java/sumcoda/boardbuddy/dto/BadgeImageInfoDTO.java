package sumcoda.boardbuddy.dto;

import lombok.Builder;

@Builder
public record BadgeImageInfoDTO(

        String badgeImageSignedURL,

        String badgeYearMonth
) {}
