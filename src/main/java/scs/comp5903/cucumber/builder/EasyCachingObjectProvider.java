package scs.comp5903.cucumber.builder;

import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An object provider that creates the instance using empty constructor and cache it in the map for reuse
 *
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public class EasyCachingObjectProvider implements BaseObjectProvider {
  /**
   * the internal map to store the step definition class and its instance <br/>
   * the class in the key must not be the superclass or interface of the step definition class
   */
  private Map<Class<?>, Object> objects;
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  public EasyCachingObjectProvider() {
    objects = new HashMap<>();
  }

  public EasyCachingObjectProvider(Object... objects) {
    this();
    for (Object object : objects) {
      this.objects.put(object.getClass(), object);
    }
  }

  /**
   * get the instance of this class or its subclasses.
   */
  @Override
  public <T> T get(Class<T> clazz) {
    lock.readLock().lock();
    try {
      var result = (Optional<T>) tryGet(clazz);
      if (result.isPresent()) {
        return result.get();
      }
    } finally {
      lock.readLock().unlock();
    }

    lock.writeLock().lock();
    // else, create new instance and cache it in the map
    try {
      // do the read again in write block because in extreme race condition,
      // we can have multiple thread waiting on write for the same class
      var result = (Optional<T>) tryGet(clazz);
      if (result.isPresent()) {
        return result.get();
      }
      // by here, there should be only one thread calling the put() for a same class
      var newInstance = clazz.getConstructor().newInstance();
      objects.put(clazz, newInstance);
      return newInstance;
    } catch (InvocationTargetException e) {
      throw new EasyCucumberException(ErrorCode.EZCU007, "Failed to create object of class " + clazz.getName() + ". " +
          "Your public empty constructor throws this exception:", e);
    } catch (InstantiationException e) {
      throw new EasyCucumberException(ErrorCode.EZCU029, "Failed to create object of class " + clazz.getName() + ". " +
          "I can't create an instance of an abstract class or an interface, you know", e);
    } catch (IllegalAccessException e) {
      throw new EasyCucumberException(ErrorCode.EZCU030, "Failed to create object of class " + clazz.getName() + ". " +
          "You sure I have the access to the empty constructor", e);
    } catch (NoSuchMethodException e) {
      throw new EasyCucumberException(ErrorCode.EZCU031, "Failed to create object of class " + clazz.getName() + ". " +
          "Maybe you forgot to create a public empty constructor", e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * for testing purpose
   */
  Map<Class<?>, Object> getObjects() {
    return objects;
  }

  /**
   * for testing purpose
   */
  void setObjects(Map<Class<?>, Object> objects) {
    this.objects = objects;
  }

  private Optional<Object> tryGet(Class<?> clazz) {
    // not calling containKey because internally it is calling get() == null
    var firstResult = objects.get(clazz);
    if (firstResult != null) {
      return Optional.of(firstResult);
    }
    // then check if the input class is a superclass of a class in the map
    var classes = objects.keySet();
    for (var classItem : classes) {
      if (clazz.isAssignableFrom(classItem)) {
        return Optional.of(objects.get(classItem));
      }
    }
    return Optional.empty();
  }
}
