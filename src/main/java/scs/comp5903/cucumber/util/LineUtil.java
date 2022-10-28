package scs.comp5903.cucumber.util;

import scs.comp5903.cucumber.model.annotation.step.JStepKeyword;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-06
 */
public class LineUtil {
  private LineUtil() {
  }

  public static boolean isFeatureTitle(String line) {
    return line.startsWith(JFeatureKeyword.FEATURE);
  }

  public static boolean isScenarioTitle(String line) {
    return line.startsWith(JFeatureKeyword.SCENARIO);
  }

  public static boolean isScenarioOutlineTitle(String line) {
    return line.startsWith(JFeatureKeyword.SCENARIO_OUTLINE) || line.startsWith(JFeatureKeyword.SCENARIO_TEMPLATE);
  }

  public static boolean isStep(String line) {
    var firstWord = line.split(" ")[0];
    for (JStepKeyword keyword : JStepKeyword.values()) {
      if (firstWord.equals(keyword.getKeyword())) {
        return true;
      }
    }
    return false;
  }

  public static boolean isExampleKeyword(String line) {
    return line.startsWith(JFeatureKeyword.EXAMPLE) || line.startsWith(JFeatureKeyword.EXAMPLES);
  }

  public static boolean isExampleContent(String line) {
    return line.startsWith(JFeatureKeyword.EXAMPLE_SEPARATOR) && line.endsWith(JFeatureKeyword.EXAMPLE_SEPARATOR);
  }

  public static boolean isComment(String line) {
    return line.startsWith(JFeatureKeyword.COMMENT);
  }

  public static boolean isTag(String line) {
    return line.startsWith(JFeatureKeyword.TAG);
  }
}
