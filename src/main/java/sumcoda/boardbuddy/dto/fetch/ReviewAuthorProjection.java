package sumcoda.boardbuddy.dto.fetch;

public record ReviewAuthorProjection(

        String s3SavedObjectName,

        Integer rank,

        String nickname,

        Boolean hasReviewed
) {}
