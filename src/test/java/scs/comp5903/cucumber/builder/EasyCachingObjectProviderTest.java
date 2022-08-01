package scs.comp5903.cucumber.builder;

import org.jooq.lambda.Unchecked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-19
 */
class EasyCachingObjectProviderTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(EasyCachingObjectProviderTest.class);

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

  @Test
  void raceCondition() throws InterruptedException {
    // given
    var getCount = new AtomicInteger();
    var putCount = new AtomicInteger();
    // create a map to capture the number of get and put operations
    var methodCallCountMap = new HashMap<Class<?>, Object>() {
      @Override
      public Object get(Object key) {
        getCount.incrementAndGet();
        return super.get(key);
      }

      @Override
      public Object put(Class<?> key, Object value) {
        putCount.incrementAndGet();
        return super.put(key, value);
      }
    };
    easyCachingObjectProvider.setObjects(methodCallCountMap);
    var countDownLatch = new CountDownLatch(100);
    var threadPoolExecutor = new ThreadPoolExecutor(100, 100, 0,
        TimeUnit.MILLISECONDS, new java.util.concurrent.LinkedBlockingQueue<>());

    // when
    for (int i = 0; i < 100; i++) {
      int finalI = i;
      threadPoolExecutor.submit(Unchecked.runnable(() -> {
        countDownLatch.countDown();
        // here we should be able to archive that all 100 threads are waiting each other so that
        // all 100 threads can start getting the TestClass instance at the same time
        countDownLatch.await();
        var testClass = easyCachingObjectProvider.get(TestClass.class);
        log.debug("testClass: {} {}", testClass, finalI);
      }));
    }

    threadPoolExecutor.shutdown();
    assertTrue(threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES), "this should not take more than 1 minute");

    // then
    assertEquals(1, putCount.get());
    assertEquals(99, getCount.get());

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
