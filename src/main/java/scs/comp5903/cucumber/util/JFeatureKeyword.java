package scs.comp5903.cucumber.util;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-06
 */
public class JFeatureKeyword {
  private JFeatureKeyword() {
  }

  public static final String FEATURE = "Feature:";
  public static final String SCENARIO = "Scenario:";
  public static final String SCENARIO_OUTLINE = "Scenario Outline:";
  public static final String SCENARIO_TEMPLATE = "Scenario Template:";
  public static final String EXAMPLE = "Example:";
  public static final String EXAMPLES = "Examples:";
  public static final String EXAMPLE_SEPARATOR = "|";
  public static final String COMMENT = "#";
  public static final String TAG = "@";
}
