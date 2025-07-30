package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

@Builder
public record ParticipationApplicationInfoDTO(

        Long id,

        String nickname,

        String profileImageSignedURL

) {}
