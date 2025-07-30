package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

@Builder
public record GatherArticleSimpleInfoDTO(

        Long gatherArticleId,

        String title,

        String meetingLocation,

        Integer currentParticipants
) {
}
