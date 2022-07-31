package scs.comp5903.cucumber.builder;

import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * An object provider that creates the instance using empty constructor and cache it in the map for reuse
 *
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public class EasyCachingObjectProvider implements BaseObjectProvider {
  private final Map<Class<?>, Object> objects;

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
   * Prefer to use {@link #EasyCachingObjectProvider(Object...)}, instead of hand writing the map to pass in <br/>
   * This should only be used for maybe like transferring from a DI framework
   */
  public EasyCachingObjectProvider(Map<Class<?>, Object> objects) {
    for (var entry : objects.entrySet()) {
      if (!entry.getClass().isAssignableFrom(entry.getValue().getClass())) {
        throw new EasyCucumberException(ErrorCode.EZCU032, "The class " + entry.getValue().getClass() + " is not the same, or the superclass or interface of the class of instance " + entry.getKey());
      }
    }
    // create new mutable map to avoid modifying the original map
    this.objects = new HashMap<>(objects);
  }


  /**
   * get the instance of this class or its subclasses.
   */
  @Override
  public <T> T get(Class<T> clazz) {
    if (!objects.containsKey(clazz)) {
      // also check if the input class is a superclass of a class in the map
      var classes = objects.keySet();
      for (var classItem : classes) {
        if (clazz.isAssignableFrom(classItem)) {
          return (T) objects.get(classItem);
        }
      }
      // else, create new instance and cache it in the map
      try {
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
      }
    } else {
      return (T) objects.get(clazz);
    }
  }

  /**
   * for testing purpose
   */
  Map<Class<?>, Object> getObjects() {
    return objects;
  }
}
