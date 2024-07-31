package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.MemberChatRoomRole;

public class MemberChatRoomResponse {

    @Getter
    @NoArgsConstructor
    public static class ValidateDTO {

        private Long id;

        private MemberChatRoomRole memberChatRoomRole;

        @Builder
        public ValidateDTO(Long id, MemberChatRoomRole memberChatRoomRole) {
            this.id = id;
            this.memberChatRoomRole = memberChatRoomRole;
        }
    }
}
