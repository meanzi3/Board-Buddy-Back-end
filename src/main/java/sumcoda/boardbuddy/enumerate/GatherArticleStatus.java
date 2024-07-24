package sumcoda.boardbuddy.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatherArticleStatus {

  OPEN("open"),
  CLOSED("closed"),
  SOON("soon");

  @JsonValue
  private final String value;

  @Override
  public String toString() {
    return this.value;
  }

}
