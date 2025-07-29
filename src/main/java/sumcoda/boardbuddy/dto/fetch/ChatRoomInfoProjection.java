package sumcoda.boardbuddy.dto.fetch;


public record ChatRoomInfoProjection(

        Long chatRoomId,

        GatherArticleSimpleInfoProjection gatherArticleSimpleInfoProjection,

        ChatMessageLastSentInfoProjection chatMessageLastSentInfoProjection
) {}
