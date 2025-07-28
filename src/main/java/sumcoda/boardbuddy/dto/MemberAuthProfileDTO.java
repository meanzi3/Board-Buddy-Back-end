package sumcoda.boardbuddy.dto;

import lombok.Builder;
import sumcoda.boardbuddy.enumerate.MemberType;

@Builder
public record MemberAuthProfileDTO(

        String nickname,

        Boolean isPhoneNumberVerified,

        MemberType memberType,

        String profileImageSignedURL
) {
}
