package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatRoomResponse {

    @Getter
    @NoArgsConstructor
    public static class ValidateDTO {

        private Long id;

        @Builder
        public ValidateDTO(Long id) {
            this.id = id;
        }
    }
}
