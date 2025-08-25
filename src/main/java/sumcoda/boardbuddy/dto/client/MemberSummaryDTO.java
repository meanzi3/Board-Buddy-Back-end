package sumcoda.boardbuddy.dto.client;

import lombok.Builder;
import sumcoda.boardbuddy.enumerate.MemberType;

@Builder
public record MemberSummaryDTO(

        String nickname,

        Boolean isPhoneNumberVerified,

        MemberType memberType,

        String profileImageSignedURL
) {
}
