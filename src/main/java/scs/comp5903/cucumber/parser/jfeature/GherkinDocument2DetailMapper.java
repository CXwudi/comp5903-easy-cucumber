package scs.comp5903.cucumber.parser.jfeature;

import io.cucumber.messages.types.Scenario;
import io.cucumber.messages.types.Step;
import io.cucumber.messages.types.Tag;
import org.slf4j.Logger;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jfeature.JScenarioDetail;
import scs.comp5903.cucumber.model.jfeature.JScenarioOutlineDetail;
import scs.comp5903.cucumber.model.jfeature.jstep.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-12-11
 */
public class GherkinDocument2DetailMapper {
  private static final Logger log = getLogger(GherkinDocument2DetailMapper.class);

  public JScenarioOutlineDetail mapScenarioOutlineToDetail(Scenario scenario) {
    // first do validation
    var examples = scenario.getExamples();
    if (examples.isEmpty()) {
      throw new EasyCucumberException(ErrorCode.EZCU017, "Scenario outline should have at least one example");
    }
    var example = examples.get(0); // only support one example for now
    var tableRow = example.getTableHeader().orElseThrow(() -> new EasyCucumberException(ErrorCode.EZCU018, "Only support Example with a header"));
    var tableRowCells = tableRow.getCells();

    // then start by first extracting root info
    var scenarioOutlineTagsLiteral = toTagsLiteral(scenario.getTags());
    var scenarioOutlineTitle = scenario.getName();
    var scenarioOutlineSteps = scenario.getSteps();
    log.debug("Building data class for scenario outline: {}", scenarioOutlineTitle);

    // constructing the header to value base map
    var headerToValueMap = new HashMap<String, String>();
    for (var cell : tableRowCells) {
      var trimmed = cell.getValue().trim();
      if (trimmed.isEmpty()) {
        throw new EasyCucumberException(ErrorCode.EZCU019, "Example header should not have empty cell");
      }
      headerToValueMap.put(trimmed, "");
    }

    // then start to construct the scenarios
    var scenarios = new ArrayList<JScenarioDetail>();
    var counter = 0;
    for (var row : example.getTableBody()) {
      // first, update the header to value map based on current row
      var rowCells = row.getCells();
      for (int i = 0; i < rowCells.size(); i++) {
        var header = tableRowCells.get(i).getValue();
        var value = rowCells.get(i).getValue();
        headerToValueMap.put(header, value);
      }
      // then, use the map to construct the extracted steps
      var extractedSteps = new ArrayList<AbstractJStep>();
      for (var step : scenarioOutlineSteps) {
        var stepText = step.getText();
        for (var entry : headerToValueMap.entrySet()) {
          stepText = stepText.replace("<" + entry.getKey() + ">", entry.getValue());
        }
        var extractedStep = parseStep(step.getKeyword(), stepText);
        extractedSteps.add(extractedStep);
      }
      // add the scenario
      scenarios.add(new JScenarioDetail(scenarioOutlineTitle + " - Example " + ++counter, scenarioOutlineTagsLiteral, extractedSteps));
    }
    return new JScenarioOutlineDetail(scenarioOutlineTitle, scenarioOutlineTagsLiteral, scenarios);
  }

  public JScenarioDetail mapScenarioToDetail(Scenario scenario) {
    var scenarioTagsLiteral = toTagsLiteral(scenario.getTags());
    var scenarioTitle = scenario.getName();
    var scenarioStepsLiteral = toStepsLiteral(scenario.getSteps());
    log.debug("Building data class for scenario: {}", scenarioTitle);
    return new JScenarioDetail(scenarioTitle, scenarioTagsLiteral, scenarioStepsLiteral);
  }


  public List<String> toTagsLiteral(List<Tag> tags) {
    List<String> list = new ArrayList<>();
    for (Tag tag : tags) {
      String trim = tag.getName().replace('@', ' ').trim();
      list.add(trim);
    }
    return list;
  }

  private List<AbstractJStep> toStepsLiteral(List<Step> steps) {
    List<AbstractJStep> list = new ArrayList<>();
    for (Step step : steps) {
      list.add(parseStep(step.getKeyword(), step.getText()));
    }
    return list;
  }


  AbstractJStep parseStep(String keyword, String step) {
    AbstractJStep jStep;
    switch (keyword.toLowerCase().trim()) {
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
        throw new EasyCucumberException(ErrorCode.EZCU002, "Unknown step keyword: " + keyword);
    }
    return jStep;
  }
}
