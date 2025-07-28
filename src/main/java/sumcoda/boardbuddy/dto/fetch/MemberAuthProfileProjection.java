package sumcoda.boardbuddy.dto.fetch;

import sumcoda.boardbuddy.enumerate.MemberType;

public record MemberAuthProfileProjection(

        String nickname,

        String phoneNumber,

        MemberType memberType,

        String s3SavedObjectName
) {}
