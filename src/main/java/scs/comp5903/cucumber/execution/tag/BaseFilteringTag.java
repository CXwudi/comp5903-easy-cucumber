package scs.comp5903.cucumber.execution.tag;

import scs.comp5903.cucumber.execution.TagsContainer;

/**
 * @author CX无敌
 * @date 2022-08-10
 */
public interface BaseFilteringTag {

  boolean isTagMatch(TagsContainer tagsContainer);
}
