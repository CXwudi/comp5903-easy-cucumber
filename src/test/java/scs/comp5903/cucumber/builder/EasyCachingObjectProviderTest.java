package scs.comp5903.cucumber.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-19
 */
class EasyCachingObjectProviderTest {

  private EasyCachingObjectProvider easyCachingObjectProvider;

  @BeforeEach
  void setUp() {
    easyCachingObjectProvider = new EasyCachingObjectProvider();
  }

  @Test
  void createIfNotExist() {
    assertEquals(0, easyCachingObjectProvider.getObjects().size());
    var instance = easyCachingObjectProvider.get(TestClass.class);
    assertEquals(1, easyCachingObjectProvider.getObjects().size());
    assertEquals(instance, easyCachingObjectProvider.getObjects().get(TestClass.class));
  }

  @Test
  void doNotCreateIfExist() {
    var instance = new TestClass();
    easyCachingObjectProvider = new EasyCachingObjectProvider(instance);
    assertEquals(1, easyCachingObjectProvider.getObjects().size());
    assertEquals(instance, easyCachingObjectProvider.getObjects().get(TestClass.class));
    var cachedInstance = easyCachingObjectProvider.get(TestClass.class);
    assertEquals(1, easyCachingObjectProvider.getObjects().size());
    assertEquals(instance, cachedInstance);
  }

  @Test
  void doNotCreateWhenRequestedByInterface() {
    var instance = new TestClass2();
    easyCachingObjectProvider = new EasyCachingObjectProvider(instance);
    assertEquals(1, easyCachingObjectProvider.getObjects().size());
    assertEquals(instance, easyCachingObjectProvider.getObjects().get(TestClass2.class));
    var cachedInstance = easyCachingObjectProvider.get(TestInterface.class);
    assertEquals(1, easyCachingObjectProvider.getObjects().size());
    assertEquals(instance, cachedInstance);
  }

}

class TestClass {
  public TestClass() {
  }
}

interface TestInterface {
}

class TestClass2 implements TestInterface {
  public TestClass2() {
  }
}
