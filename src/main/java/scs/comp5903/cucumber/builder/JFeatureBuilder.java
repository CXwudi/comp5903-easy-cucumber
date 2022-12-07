package scs.comp5903.cucumber.builder;

import org.slf4j.Logger;
import scs.comp5903.cucumber.builder.params.JStepParameterExtractor;
import scs.comp5903.cucumber.execution.*;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jfeature.JFeatureDetail;
import scs.comp5903.cucumber.model.jfeature.JScenarioDetail;
import scs.comp5903.cucumber.model.jfeature.JScenarioOutlineDetail;
import scs.comp5903.cucumber.model.jfeature.jstep.AbstractJStep;
import scs.comp5903.cucumber.model.jstepdef.JHookType;
import scs.comp5903.cucumber.model.jstepdef.JStepDefDetail;
import scs.comp5903.cucumber.model.jstepdef.JStepDefHookDetail;
import scs.comp5903.cucumber.model.jstepdef.JStepDefMethodDetail;

import java.util.*;
import java.util.stream.Collectors;

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
    // for each step def method detail, find the matching step in the linked hash set
    // and remove it from the linked hash set and add it into the HashMap
    HashMap<AbstractJStep, MatchResult> matchingStepToMethodMap = mapAllStepsToStepDefMethods(allSteps, stepDefDetail.getSteps());
    // for each step def hook detail, convert it into an executable hook method,
    // and group them by hook type and sort them by order
    EnumMap<JHookType, List<JHookMethodExecution>> hookTypeToHookMethodMap = mapAllHookDetailsToMethods(stepDefDetail.getHooks(), objectProvider);
    // with the filled map of matching steps to step def method details, create the method execution for each pair
    // lastly, create the JFeature object using all information we computed before
    log.info("Done building runnable JFeature using data classes from the feature file and step definitions");
    // TODO: let building takes the hookTypeToHookMethodMap as input
    return buildJFeature(featureDetail, matchingStepToMethodMap, hookTypeToHookMethodMap, objectProvider);
  }

  private EnumMap<JHookType, List<JHookMethodExecution>> mapAllHookDetailsToMethods(List<JStepDefHookDetail> hookDetails, BaseObjectProvider objectProvider) {
    var typeToHookDetailsMap = new EnumMap<JHookType, List<JStepDefHookDetail>>(JHookType.class);
    // first, create a map of hook type -> list of hook details
    for (JStepDefHookDetail hookDetail : hookDetails) {
      typeToHookDetailsMap.computeIfAbsent(hookDetail.getType(), k -> new ArrayList<>()).add(hookDetail);
    }
    // then, for each list of hook details, sort them by order increasing
    for (var entry: typeToHookDetailsMap.entrySet()) {
      entry.getValue().sort(Comparator.comparingInt(JStepDefHookDetail::getOrder));
    }
    // then, map each hook detail to a hook method execution
    var typeToHookMethodMap = new EnumMap<JHookType, List<JHookMethodExecution>>(JHookType.class);
    for (var entry: typeToHookDetailsMap.entrySet()) {
      var executions = entry.getValue().stream().map(detail -> {
        var method = detail.getMethod();
        return new JHookMethodExecution(method, objectProvider.get(method.getDeclaringClass()));
      }).collect(Collectors.toList());
      typeToHookMethodMap.put(entry.getKey(), executions);
    }
    return typeToHookMethodMap;
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
          () -> {
            log.error("No matching step definition found for step: {}", step);
            var expMsg = String.format(
                "Step definition not found for: %s. There can be many reasons for this failure, please check that:\n" +
                    "1. Are you sure you implemented this step definition?\n" +
                    "2. Did you forget to make the method public?\n" +
                    "3. Did you forget to add the correct annotation to method?\n" +
                    "4. Does the string in your annotation matches the step?\n" +
                    "5. Are all parameters declared in the step definition method correct?\n" +
                    "6. Are you sure you are using our own annotation but not the official Cucumber's annotation?"
                , step
            );
            return new EasyCucumberException(ErrorCode.EZCU013, expMsg);
          }
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
  private JFeature buildJFeature(
      JFeatureDetail featureDetail,
      HashMap<AbstractJStep, MatchResult> matchingStepToMethodMap,
      EnumMap<JHookType, List<JHookMethodExecution>> hookTypeToHookMethodMap,
      BaseObjectProvider objectProvider) {
    var jScenarios = buildJScenarios(featureDetail.getScenarios(), matchingStepToMethodMap, hookTypeToHookMethodMap, objectProvider);
    var jScenarioOutlines = new ArrayList<JScenarioOutline>();
    for (JScenarioOutlineDetail jScenarioOutlineDetail : featureDetail.getScenarioOutlines()) {
      ArrayList<JScenario> extractedJScenarios = buildJScenarios(jScenarioOutlineDetail.getScenarios(), matchingStepToMethodMap, hookTypeToHookMethodMap, objectProvider);
      jScenarioOutlines.add(new JScenarioOutline(jScenarioOutlineDetail.getTitle(), jScenarioOutlineDetail.getTags(), extractedJScenarios));
    }
    return new JFeature(featureDetail.getTitle(),
        featureDetail.getTags(),
        jScenarios, jScenarioOutlines,
        featureDetail.getScenarioOrders(),
        featureDetail.getScenarioOutlineOrders(),
        hookTypeToHookMethodMap.computeIfAbsent(JHookType.BEFORE_ALL_JSCENARIOS, k -> Collections.emptyList()),
        hookTypeToHookMethodMap.computeIfAbsent(JHookType.AFTER_ALL_JSCENARIOS, k -> Collections.emptyList())
    );
  }

  private ArrayList<JScenario> buildJScenarios(
      List<JScenarioDetail> jScenarioOutlineDetail,
      HashMap<AbstractJStep, MatchResult> matchingStepToMethodMap,
      EnumMap<JHookType, List<JHookMethodExecution>> hookTypeToHookMethodMap,
      BaseObjectProvider objectProvider) {
    var extractedJScenarios = new ArrayList<JScenario>();
    for (JScenarioDetail jScenarioDetail : jScenarioOutlineDetail) {
      var executions = new ArrayList<JStepDefMethodExecution>();
      for (AbstractJStep step : jScenarioDetail.getSteps()) {
        var matchResult = matchingStepToMethodMap.get(step);
        var method = matchResult.jStepDefMethodDetail.getMethod();
        executions.add(new JStepDefMethodExecution(method, objectProvider.get(method.getDeclaringClass()), matchResult.parameters));
      }
      var newJScenario = new JScenario(
          jScenarioDetail.getTitle(),
          jScenarioDetail.getTags(),
          executions,
          hookTypeToHookMethodMap.computeIfAbsent(JHookType.BEFORE_EACH_JSCENARIO, k -> Collections.emptyList()),
          hookTypeToHookMethodMap.computeIfAbsent(JHookType.AFTER_EACH_JSCENARIO, k -> Collections.emptyList()),
          hookTypeToHookMethodMap.computeIfAbsent(JHookType.BEFORE_EACH_JSTEP, k -> Collections.emptyList()),
          hookTypeToHookMethodMap.computeIfAbsent(JHookType.AFTER_EACH_JSTEP, k -> Collections.emptyList()));
      extractedJScenarios.add(newJScenario);
    }
    return extractedJScenarios;
  }
}
