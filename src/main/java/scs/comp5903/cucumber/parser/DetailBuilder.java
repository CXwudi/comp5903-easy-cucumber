package scs.comp5903.cucumber.parser;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.JScenarioDetail;
import scs.comp5903.cucumber.model.JScenarioOutlineDetail;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jstep.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static scs.comp5903.cucumber.model.exception.ErrorCode.EZCU002;
import static scs.comp5903.cucumber.model.exception.ErrorCode.EZCU003;

/**
 * This class takes the raw literal lines from file and parse them to {@link JScenarioDetail} and {@link JScenarioOutlineDetail}.
 *
 * @author Charles Chen 101035684
 * @date 2022-06-28
 */
public class DetailBuilder {

  private static final Logger log = getLogger(DetailBuilder.class);

  /**
   * this method takes the raw literal of scenario outline
   * flatten the scenario outline to a list of scenarios
   *
   * @param title                      title of the scenario outline
   * @param stepsLiteral               steps of the scenario outline
   * @param examplesLiteral            examples of the scenario outline
   * @param scenarioOutlineTagsLiteral tags of the scenario outline, in terms of list of lines
   * @return a list of scenarios
   */
  public JScenarioOutlineDetail buildJScenarioOutlineDetail(String title, List<String> stepsLiteral, List<String> examplesLiteral, List<String> scenarioOutlineTagsLiteral) {
    log.debug("Building data class for scenario outline: {}", title);
    List<String> paramNames = new ArrayList<>();
    for (String s1 : examplesLiteral.get(0).split("\\|")) {
      String trim = s1.trim();
      if (!trim.isEmpty()) {
        paramNames.add(trim);
      }
    }
    var nameToValueMap = new HashMap<String, String>(paramNames.size());
    for (String s : paramNames) {
      if (nameToValueMap.put(s, "") != null) {
        throw new EasyCucumberException(EZCU003, "A example should not have duplicate key", new IllegalStateException("Duplicate key"));
      }
    }
    var scenarios = new ArrayList<JScenarioDetail>();
    var counter = 0;
    for (var paramLine : examplesLiteral.subList(1, examplesLiteral.size())) {
      var params = new ArrayList<String>();
      for (String s : paramLine.split("\\|")) {
        String line = s.trim();
        if (!line.isEmpty()) {
          params.add(line);
        }
      }
      for (var i = 0; i < paramNames.size(); i++) {
        nameToValueMap.put(paramNames.get(i), params.get(i));
      }
      var extractedStepsLiteral = new ArrayList<String>();
      for (var line : stepsLiteral) {
        for (var entry : nameToValueMap.entrySet()) {
          var key = entry.getKey();
          var value = entry.getValue();
          line = line.replace("<" + key + ">", value);
        }
        extractedStepsLiteral.add(line);
      }
      scenarios.add(buildJScenarioDetail(title + " - Example " + ++counter, extractedStepsLiteral, scenarioOutlineTagsLiteral));
    }
    return new JScenarioOutlineDetail(title, new ArrayList<>(), scenarios);
  }


  /**
   * create {@link JScenarioDetail} from raw literals
   *
   * @param title        title of the scenario
   * @param stepsLiteral steps of the scenario
   * @param tagsLiteral  tags of the scenario, in terms of list of lines
   * @return a {@link JScenarioDetail}
   */
  public JScenarioDetail buildJScenarioDetail(String title, List<String> stepsLiteral, List<String> tagsLiteral) {
    //TODO: handle tags literal
    log.debug("Building data class for scenario: {}", title);
    List<AbstractJStep> steps = new ArrayList<>();
    for (String s : stepsLiteral) {
      AbstractJStep abstractJStep = parseStep(s);
      steps.add(abstractJStep);
    }
    List<String> tags = new ArrayList<>();
    for (String tagLiteral : tagsLiteral) {
      var tagsRaw = tagLiteral.split("@");
      for (var tag : tagsRaw) {
        var trimmed = tag.trim();
        if (trimmed.isBlank()) {
          throw new EasyCucumberException(ErrorCode.EZCU037, "Tag should not be a blank string: " + tagLiteral);
        }
        tags.add(trimmed);
      }
    }
    return new JScenarioDetail(title, tags, steps);
  }

  AbstractJStep parseStep(String line) {
    // we split, but we only want the first word which is the keyword
    var keyword = line.split(" ")[0];
    var step = line.replace(keyword, "").trim();
    AbstractJStep jStep;
    switch (keyword.toLowerCase()) {
      case "given":
        jStep = new GivenStep(step);
        break;
      case "when":
        jStep = new WhenStep(step);
        break;
      case "then":
        jStep = new ThenStep(step);
        break;
      case "and":
        jStep = new AndStep(step);
        break;
      case "but":
        jStep = new ButStep(step);
        break;
      default:
        throw new EasyCucumberException(EZCU002, "Unknown step keyword: " + keyword);
    }
    return jStep;
  }
}
