package scs.comp5903.cucumber.builder;

import org.slf4j.Logger;
import scs.comp5903.cucumber.execution.JFeature;
import scs.comp5903.cucumber.execution.JScenario;
import scs.comp5903.cucumber.execution.JScenarioOutline;
import scs.comp5903.cucumber.execution.MethodExecution;
import scs.comp5903.cucumber.model.*;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jstep.AbstractJStep;

import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public class JFeatureBuilder {

  private static final Logger log = getLogger(JFeatureBuilder.class);

  final JStepParameterExtractor jStepParameterExtractor;

  public JFeatureBuilder(JStepParameterExtractor jStepParameterExtractor) {
    this.jStepParameterExtractor = jStepParameterExtractor;
  }

  public class MatchResult {
    private final JStepDefMethodDetail jStepDefMethodDetail;
    private final Object[] parameters;

    public MatchResult(JStepDefMethodDetail jStepDefMethodDetail, Object[] parameters) {
      this.jStepDefMethodDetail = jStepDefMethodDetail;
      this.parameters = parameters;
    }
  }

  public JFeature build(JFeatureDetail featureDetail, JStepDefDetail stepDefDetail, BaseObjectProvider objectProvider) {
    log.info("Start building runnable JFeature using data classes from the feature file and step definitions");
    // extract all AbstractJStep from featureDetail into a linked has set
    var allSteps = extractAllSteps(featureDetail);
    // for each step def method detail, find the matching step in the linked hash set, and remove it from the linked hash set and add it into the HashMap
    HashMap<AbstractJStep, MatchResult> matchingStepToMethodMap = mapAllStepsToStepDefMethods(allSteps, stepDefDetail.getSteps());
    // with the filled map of matching steps to step def method details, create the method execution for each pair
    // lastly, create the JFeature object using all information we computed before
    log.info("Done building runnable JFeature using data classes from the feature file and step definitions");
    return buildJFeature(featureDetail, matchingStepToMethodMap, objectProvider);
  }


  /**
   * extract all AbstractJStep from featureDetail into a linked has set
   */
  private Set<AbstractJStep> extractAllSteps(JFeatureDetail featureDetail) {
    Set<AbstractJStep> allSteps = new LinkedHashSet<>(featureDetail.getScenarioOutlines().size() + featureDetail.getScenarios().size());
    Set<AbstractJStep> set = new LinkedHashSet<>(featureDetail.getScenarios().size());
    for (JScenarioDetail jScenarioDetail : featureDetail.getScenarios()) {
      List<AbstractJStep> steps = jScenarioDetail.getSteps();
      set.addAll(steps);
    }
    allSteps.addAll(set);
    set = new LinkedHashSet<>(featureDetail.getScenarioOrders().size());
    for (JScenarioOutlineDetail jScenarioOutlineDetail : featureDetail.getScenarioOutlines()) {
      List<JScenarioDetail> scenarios = jScenarioOutlineDetail.getScenarios();
      for (JScenarioDetail jScenarioDetail : scenarios) {
        List<AbstractJStep> steps = jScenarioDetail.getSteps();
        set.addAll(steps);
      }
    }
    allSteps.addAll(set);
    return allSteps;
  }


  /**
   * for each step def method detail, find the matching step in the linked hash set, and remove it from the linked hash set and add it into the HashMap
   */
  private HashMap<AbstractJStep, MatchResult> mapAllStepsToStepDefMethods(Set<AbstractJStep> allSteps, List<JStepDefMethodDetail> jStepDefMethodDetails) {
    log.debug("Start matching {} steps to {} step definitions provided", allSteps.size(), jStepDefMethodDetails.size());
    var stepsCopy = new LinkedList<>(allSteps);
    var results = new HashMap<AbstractJStep, MatchResult>();

    while (!stepsCopy.isEmpty()) {
      var step = stepsCopy.poll();
      var matchResult = tryMatchStep(step, jStepDefMethodDetails).orElseThrow(
          () -> new EasyCucumberException(ErrorCode.EZCU013, "Step definition not found for: " + stepsCopy.get(0)
              + ". Are you sure you implemented this step definition? Or did you forget to make the method public?")
      );
      results.put(step, matchResult);
    }
    return results;
  }

  private Optional<MatchResult> tryMatchStep(AbstractJStep step, List<JStepDefMethodDetail> jStepDefMethodDetails) {
    for (JStepDefMethodDetail stepDefDetail : jStepDefMethodDetails) {
      var parametersOpt = jStepParameterExtractor.tryExtractParameters(step, stepDefDetail);
      if (parametersOpt.isPresent()) {
        var parameters = parametersOpt.get();
        log.debug("  Matched step {} with step definition {}", step, stepDefDetail.getMatcher());
        return Optional.of(new MatchResult(stepDefDetail, parameters.toArray()));
      }
    }
    return Optional.empty();
  }

  /**
   * with the filled map of matching steps to step def method details, create the method execution for each pair <br/>
   * lastly, create the JFeature object using all information we computed before
   */
  private JFeature buildJFeature(JFeatureDetail featureDetail, HashMap<AbstractJStep, MatchResult> matchingStepToMethodMap, BaseObjectProvider objectProvider) {
    var jScenarios = getjScenarios(featureDetail.getScenarios(), matchingStepToMethodMap, objectProvider);
    var jScenarioOutlines = new ArrayList<JScenarioOutline>();
    for (JScenarioOutlineDetail jScenarioOutlineDetail : featureDetail.getScenarioOutlines()) {
      ArrayList<JScenario> extractedJScenarios = getjScenarios(jScenarioOutlineDetail.getScenarios(), matchingStepToMethodMap, objectProvider);
      jScenarioOutlines.add(new JScenarioOutline(jScenarioOutlineDetail.getTitle(), jScenarioOutlineDetail.getTags(), extractedJScenarios));
    }
    return new JFeature(featureDetail.getTitle(), featureDetail.getTags(), jScenarios, jScenarioOutlines, featureDetail.getScenarioOrders(), featureDetail.getScenarioOutlineOrders());
  }

  private ArrayList<JScenario> getjScenarios(List<JScenarioDetail> jScenarioOutlineDetail, HashMap<AbstractJStep, MatchResult> matchingStepToMethodMap, BaseObjectProvider objectProvider) {
    var extractedJScenarios = new ArrayList<JScenario>();
    for (JScenarioDetail jScenarioDetail : jScenarioOutlineDetail) {
      var executions = new ArrayList<MethodExecution>();
      for (AbstractJStep step : jScenarioDetail.getSteps()) {
        var matchResult = matchingStepToMethodMap.get(step);
        var method = matchResult.jStepDefMethodDetail.getMethod();
        executions.add(new MethodExecution(method, objectProvider.get(method.getDeclaringClass()), matchResult.parameters));
      }
      extractedJScenarios.add(new JScenario(jScenarioDetail.getTitle(), jScenarioDetail.getTags(), executions));
    }
    return extractedJScenarios;
  }
}
