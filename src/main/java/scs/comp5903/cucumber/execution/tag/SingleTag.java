package scs.comp5903.cucumber.execution.tag;

import scs.comp5903.cucumber.execution.TagsContainer;

/**
 * @author CX无敌
 * @date 2022-08-10
 */
public class SingleTag implements BaseFilteringTag {

  private final String tag;

  public SingleTag(String tag) {
    this.tag = tag;
  }

  @Override
  public boolean isTagMatch(TagsContainer tagsContainer) {
    return tagsContainer.getTags().contains(tag);
  }
}
