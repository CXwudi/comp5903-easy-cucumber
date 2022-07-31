package scs.comp5903.cucumber.util;

import org.reflections.Reflections;

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

}
