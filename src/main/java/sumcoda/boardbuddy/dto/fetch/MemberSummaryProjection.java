package sumcoda.boardbuddy.dto.fetch;

import sumcoda.boardbuddy.enumerate.MemberType;

public record MemberSummaryProjection(

        String nickname,

        String phoneNumber,

        MemberType memberType,

        String s3SavedObjectName
) {}
