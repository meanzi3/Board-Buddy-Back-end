package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatherArticleStatus {

  OPEN("모집중"),
  CLOSED("모집마감");

  private final String value;

}
