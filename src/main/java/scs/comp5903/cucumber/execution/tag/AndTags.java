package scs.comp5903.cucumber.execution.tag;

import scs.comp5903.cucumber.execution.TagsContainer;

import java.util.List;

/**
 * @author CX无敌
 * @date 2022-08-10
 */
public class AndTags implements BaseFilteringTag {

  private final List<BaseFilteringTag> tags;

  public AndTags(List<BaseFilteringTag> tags) {
    this.tags = tags;
  }

  public AndTags(BaseFilteringTag... tags) {
    this.tags = List.of(tags);
  }

  @Override
  public boolean isTagMatch(TagsContainer tagsContainer) {
    for (BaseFilteringTag tag : tags) {
      if (!tag.isTagMatch(tagsContainer)) {
        return false;
      }
    }
    return true;
  }
}
