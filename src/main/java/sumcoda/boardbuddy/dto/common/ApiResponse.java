package sumcoda.boardbuddy.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
