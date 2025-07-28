package sumcoda.boardbuddy.dto;

import lombok.Builder;

@Builder
public record ParticipationApplicationInfoDTO(

        Long id,

        String nickname,

        String profileImageSignedURL

) {}
