package sumcoda.boardbuddy.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.Status;

@NoArgsConstructor
@Getter
public class ApiResponse<T> {

  private String status;

  private T data;

  private String message;

  public ApiResponse(T data, String message) {
    this.data = data;
    this.message = message;
  }

  @Builder
  public ApiResponse(String status, T data, String message) {
    this.status = status;
    this.data = data;
    this.message = message;
  }

  public static <T> ApiResponse<T> createApiResponse(Status status, T data, String message) {
    return ApiResponse.<T>builder()
            .status(status.getValue())
            .data(data)
            .message(message)
            .build();
  }
}
