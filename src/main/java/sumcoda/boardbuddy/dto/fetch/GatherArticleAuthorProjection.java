package sumcoda.boardbuddy.dto.fetch;


public record GatherArticleAuthorProjection(

        String nickname,

        Integer rank,

        String s3SavedObjectName,

        String description
) {
}
