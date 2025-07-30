package sumcoda.boardbuddy.dto.fetch;

public record GatherArticleSimpleInfoProjection(

        Long gatherArticleId,

        String title,

        String meetingLocation,

        Integer currentParticipants
) {}
