package scs.comp5903.cucumber.execution.tag;

import scs.comp5903.cucumber.execution.TagsContainer;

/**
 * @author CX无敌
 * @date 2022-08-10
 */
public interface BaseFilteringTag {

  boolean isTagMatch(TagsContainer tagsContainer);

  static BaseFilteringTag tag(String tag) {
    return new SingleTag(tag);
  }

  static BaseFilteringTag or(BaseFilteringTag... tags) {
    return new OrTags(tags);
  }

  static BaseFilteringTag and(BaseFilteringTag... tags) {
    return new AndTags(tags);
  }

  static BaseFilteringTag not(BaseFilteringTag tag) {
    return new NotTag(tag);
  }
}
