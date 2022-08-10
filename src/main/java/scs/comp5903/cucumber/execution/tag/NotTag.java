package scs.comp5903.cucumber.execution.tag;

import scs.comp5903.cucumber.execution.TagsContainer;

/**
 * @author CX无敌
 * @date 2022-08-10
 */
public class NotTag implements BaseFilteringTag {

  private final BaseFilteringTag tagToNegate;

  public NotTag(BaseFilteringTag tagToNegate) {
    this.tagToNegate = tagToNegate;
  }

  @Override
  public boolean isTagMatch(TagsContainer tagsContainer) {
    return !tagToNegate.isTagMatch(tagsContainer);
  }

  @Override
  public String toString() {
    return "not " + tagToNegate;
  }
}
