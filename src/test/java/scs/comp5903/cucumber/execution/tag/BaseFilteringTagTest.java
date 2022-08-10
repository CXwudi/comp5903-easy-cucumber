package scs.comp5903.cucumber.execution.tag;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.execution.TagsContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author CX无敌
 * @date 2022-08-10
 */
class BaseFilteringTagTest {

  @Test
  void isTagMatch() {
    BaseFilteringTag tag = new SingleTag("tag");
    TagsContainer tagsContainer = new DummyTagsContainer("tag");
    assertTrue(tag.isTagMatch(tagsContainer));
  }

  @Test
  void andTags() {
    BaseFilteringTag tag = new AndTags(new SingleTag("tag1"), new SingleTag("tag2"));
    TagsContainer tagsContainer = new DummyTagsContainer("tag1", "tag2");
    assertTrue(tag.isTagMatch(tagsContainer));
  }

  @Test
  void orTags() {
    BaseFilteringTag tag = new OrTags(new SingleTag("tag1"), new SingleTag("tag2"));
    TagsContainer tagsContainer = new DummyTagsContainer("tag1");
    assertTrue(tag.isTagMatch(tagsContainer));
    tagsContainer = new DummyTagsContainer("tag2");
    assertTrue(tag.isTagMatch(tagsContainer));
  }

  @Test
  void notTags() {
    BaseFilteringTag tag = new NotTag(new SingleTag("tag1"));
    TagsContainer tagsContainer = new DummyTagsContainer("tag1");
    assertFalse(tag.isTagMatch(tagsContainer));
    tagsContainer = new DummyTagsContainer("tag2");
    assertTrue(tag.isTagMatch(tagsContainer));
  }

  @Test
  void canBuildXorTags() {
    // A xor B = (A and (not B)) or ((not A) and B)
    var tag = new OrTags(new AndTags(new SingleTag("tag1"), new NotTag(new SingleTag("tag2"))), new AndTags(new NotTag(new SingleTag("tag1")), new SingleTag("tag2")));
    var tagsContainer = new DummyTagsContainer("tag1", "tag2");
    assertFalse(tag.isTagMatch(tagsContainer));
    tagsContainer = new DummyTagsContainer("tag1");
    assertTrue(tag.isTagMatch(tagsContainer));
    tagsContainer = new DummyTagsContainer("tag2");
    assertTrue(tag.isTagMatch(tagsContainer));
    tagsContainer = new DummyTagsContainer();
    assertFalse(tag.isTagMatch(tagsContainer));
  }

}

class DummyTagsContainer implements TagsContainer {

  private final List<String> tags;

  public DummyTagsContainer(List<String> tags) {
    this.tags = tags;
  }

  public DummyTagsContainer(String... tags) {
    this.tags = List.of(tags);
  }

  @Override
  public List<String> getTags() {
    return tags;
  }
}