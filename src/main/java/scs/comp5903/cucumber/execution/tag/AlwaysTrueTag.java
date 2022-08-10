package scs.comp5903.cucumber.execution.tag;

import scs.comp5903.cucumber.execution.TagsContainer;

/**
 * @author CX无敌
 * @date 2022-08-10
 */
public class AlwaysTrueTag implements BaseFilteringTag {

  public static final AlwaysTrueTag INSTANCE = new AlwaysTrueTag();

  private AlwaysTrueTag() {
  }

  @Override
  public boolean isTagMatch(TagsContainer tagsContainer) {
    return true;
  }

  @Override
  public String toString() {
    return "always true";
  }
}
