package sumcoda.boardbuddy.dto.fetch;

public record MemberDetailProjection(

        String s3SavedObjectName,

        String description,

        Integer rank,

        Double buddyScore,

        Integer joinCount,

        Integer totalExcellentCount,

        Integer totalGoodCount,

        Integer totalBadCount
) {}

