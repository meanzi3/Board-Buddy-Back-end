package sumcoda.boardbuddy.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatherArticleStatus {

  OPEN("모집중"),
  CLOSED("모집마감");

  @JsonValue
  private final String value;

  @Override
  public String toString() {
    return this.value;
  }

}
