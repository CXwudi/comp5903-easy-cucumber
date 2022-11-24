package scs.comp5903.cucumber.util;

import org.reflections.Reflections;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;

import java.lang.reflect.Modifier;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-28
 */
public class ReflectionUtil {

  private ReflectionUtil() {
  }

  public static Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
    Reflections reflections = new Reflections(packageName, SubTypes.filterResultsBy(c -> true));
    return reflections.get(SubTypes.of(Object.class).asClass());
  }

  public static void requireClassIsPublic(Class<?> clazz) {
    if (!Modifier.isPublic(clazz.getModifiers())) {
      throw new EasyCucumberException(ErrorCode.EZCU038, "Class " + clazz.getName() + " is not public");
    }
  }

}
