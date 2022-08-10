package scs.comp5903.cucumber.execution.tag;

import scs.comp5903.cucumber.execution.TagsContainer;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

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

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(" and ", "(", ")");
    for (BaseFilteringTag tag : tags) {
      String s = Objects.toString(tag);
      joiner.add(s);
    }
    return joiner.toString();
  }
}
