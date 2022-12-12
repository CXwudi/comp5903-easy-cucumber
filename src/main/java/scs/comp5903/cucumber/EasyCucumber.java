package scs.comp5903.cucumber;

import org.slf4j.Logger;
import scs.comp5903.cucumber.builder.BaseObjectProvider;
import scs.comp5903.cucumber.builder.EasyCachingObjectProvider;
import scs.comp5903.cucumber.builder.JFeatureBuilder;
import scs.comp5903.cucumber.builder.params.CucumberExpressionJStepParameterExtractor;
import scs.comp5903.cucumber.execution.JFeature;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.parser.jfeature.GherkinBasedJFeatureFileParser;
import scs.comp5903.cucumber.parser.jstep.JStepDefinitionHookParser;
import scs.comp5903.cucumber.parser.jstep.JStepDefinitionMethodParser;
import scs.comp5903.cucumber.parser.jstep.JStepDefinitionParser;
import scs.comp5903.cucumber.util.ReflectionUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * The facade class of building the runnable {@link JFeature}. <br/>
 * It contains: <br/>
 * 1. {@code build(Path featureFile, Object... stepDefinitionInstances)} <br/>
 * 2. {@code build(Path featureFile, Class<?>... stepDefinitionClasses)} <br/>
 * 3. {@code build(Path featureFile, List<Object> stepDefinitionInstances)} <br/>
 * 4. {@code build(Path featureFile, String packages)} <br/>
 * 5. {@code build(Path featureFile, String packages, BaseObjectProvider objectProvider)} <br/>
 * 6. {@code build(Path featureFile, List<Class<?>> stepDefinitionClasses, BaseObjectProvider objectProvider)} <br/>
 * <p>
 * Where all methods except last one delegate to the last method
 *
 * @author Charles Chen 101035684
 * @date 2022-06-18
 */
public class EasyCucumber {

  private static final Logger log = getLogger(EasyCucumber.class);

  private EasyCucumber() {
  }

  /**
   * Build the runnable {@link JFeature} from the given feature file and one or more instances of step definition classes.
   *
   * @param featureFile             the feature file to be parsed and ran
   * @param stepDefinitionInstances the instances of step definition classes
   * @return the runnable {@link JFeature}
   */
  public static JFeature build(Path featureFile, Object... stepDefinitionInstances) {
    var objectProvider = new EasyCachingObjectProvider(stepDefinitionInstances);
    Class<?>[] stepDefinitionClasses = new Class<?>[stepDefinitionInstances.length];
    for (int i = 0; i < stepDefinitionInstances.length; i++) {
      stepDefinitionClasses[i] = stepDefinitionInstances[i].getClass();
    }
    return build(featureFile, List.of(stepDefinitionClasses), objectProvider);
  }

  /**
   * Build the runnable {@link JFeature} from the given feature file and one or more step definition classes.
   * @param featureFile the feature file to be parsed and ran
   * @param stepDefinitionClasses the step definition classes
   * @return the runnable {@link JFeature}
   */
  public static JFeature build(Path featureFile, Class<?>... stepDefinitionClasses) {
    return build(featureFile, List.of(stepDefinitionClasses), new EasyCachingObjectProvider());
  }


  /**
   * Build the runnable {@link JFeature} from the given feature file and step definitions. <br/>
   * The step definitions can be either a list of instances of step definition classes or a list of step definition classes.
   *
   * @param featureFile     the feature file to be parsed and ran
   * @param stepDefinitions the step definitions, the list can contain either the instances of step definition classes or the step definition classes.
   *                        The list can mix contain both.
   * @return the runnable {@link JFeature}
   */
  public static JFeature build(Path featureFile, List<Object> stepDefinitions) {
    if (stepDefinitions.isEmpty()) {
      // 033 is the one of the error code where it can appear twice
      throw new EasyCucumberException(ErrorCode.EZCU033, "Need at least one step definition class");
    }

    var classes = new ArrayList<Class<?>>(stepDefinitions.size());
    var instances = new ArrayList<>(stepDefinitions.size());
    for (Object o : stepDefinitions) {
      if (o instanceof Class<?>) {
        classes.add((Class<?>) o);
      } else {
        classes.add(o.getClass());
        instances.add(o);
      }
    }
    return build(featureFile, classes, new EasyCachingObjectProvider(instances.toArray()));
  }

  /**
   * Build the runnable {@link JFeature} from the given feature file and step definition classes from a given package. <br/>
   * @param featureFile the feature file to be parsed and ran
   * @param packageName the name of the package (in forms of {@code "package.name"}) where it stores all step definition classes
   * @return the runnable {@link JFeature}
   */
  public static JFeature build(Path featureFile, String packageName) {
    return build(featureFile, packageName, new EasyCachingObjectProvider());
  }

  /**
   * Build the runnable {@link JFeature} from the given feature file and step definition classes from a given package,
   * and use a custom {@link BaseObjectProvider} to provide the step definition instances. <br/>
   * @param featureFile the feature file to be parsed and ran
   * @param packageName the name of the package (in forms of {@code "package.name"}) where it stores all step definition classes
   * @param objectProvider the custom {@link BaseObjectProvider} to provide the step definition instances
   * @return the runnable {@link JFeature}
   */
  public static JFeature build(Path featureFile, String packageName, BaseObjectProvider objectProvider) {
    return build(featureFile, new ArrayList<>(ReflectionUtil.findAllClassesUsingClassLoader(packageName)), objectProvider);
  }

  /**
   * The most abstract way of creating a runnable {@link JFeature}. <br/>
   * Build the runnable {@link JFeature} from the given feature file and a list of step definition classes,
   * and use a custom {@link BaseObjectProvider} to provide the step definition instances. <br/>
   *
   * @param featureFile           the feature file to be parsed and ran
   * @param stepDefinitionClasses the list of step definition classes
   * @param objectProvider        the custom {@link BaseObjectProvider} to provide the step definition instances
   * @return the runnable feature
   */
  public static JFeature build(Path featureFile, List<Class<?>> stepDefinitionClasses, BaseObjectProvider objectProvider) {
    if (stepDefinitionClasses.isEmpty()) {
      throw new EasyCucumberException(ErrorCode.EZCU033, "Need at least one step definition class");
    }
    log.info("Start building the runnable JFeature from feature file: {} with step definition classes: {}", featureFile, stepDefinitionClasses);
    var jFeatureFileParser = new GherkinBasedJFeatureFileParser();
    var jStepDefinitionMethodParser = new JStepDefinitionMethodParser();
    var jStepDefinitionHookParser = new JStepDefinitionHookParser();
    var jStepDefinitionParser = new JStepDefinitionParser(jStepDefinitionMethodParser, jStepDefinitionHookParser);
    var jStepParameterExtractor = new CucumberExpressionJStepParameterExtractor();
    // parse jfeature file to detail object
    var featureDetail = jFeatureFileParser.parse(featureFile);
    // parse step definition class to detail object
    var jStepDefDetail = jStepDefinitionParser.parse(stepDefinitionClasses);
    // build runnable feature
    var jFeature = new JFeatureBuilder(jStepParameterExtractor).build(featureDetail, jStepDefDetail, objectProvider);
    log.info("Successfully built the runnable JFeature: {}", jFeature.getTitle());
    return jFeature;
  }
}
