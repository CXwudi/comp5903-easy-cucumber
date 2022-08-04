package scs.comp5903.cucumber.parser;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.JFeatureDetail;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.util.JFeatureKeyword;
import scs.comp5903.cucumber.util.LineUtil;
import scs.comp5903.cucumber.util.ThrowingConsumer;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static scs.comp5903.cucumber.parser.JFeatureFileLineByLineParser.ParseState.*;

/**
 * This is the functional interface implementation of the logic of parsing feature file inspired by state-machine diagram <br/>
 * It is used by {@link JFeatureFileParser}. <br/>
 * The implementation is not thread-safe. So need to create a new instance for each feature file. <br/>
 *
 * @author Charles Chen 101035684
 * @date 2022-07-05
 */
class JFeatureFileLineByLineParser implements ThrowingConsumer<String> {

  private static final Logger log = getLogger(JFeatureFileLineByLineParser.class);

  private final JFeatureDetail.JFeatureDetailBuilder jFeatureDetailBuilder;
  private final DetailBuilder detailBuilder;

  JFeatureFileLineByLineParser(JFeatureDetail.JFeatureDetailBuilder jFeatureDetailBuilder, DetailBuilder detailBuilder) {
    this.jFeatureDetailBuilder = jFeatureDetailBuilder;
    this.detailBuilder = detailBuilder;
  }

  enum ParseState {
    START,
    FEATURE,
    DESCRIPTION,
    TAG,
    SCENARIO,
    SCENARIO_OUTLINE,
    STEP,
    EXAMPLE_KEYWORD,
    EXAMPLE_CONTENT
  }

  /**
   * The parent state. <br/>
   * e.g. the parent state of scenario is feature. <br/>
   * Only a few states have parent state, such as scenario, scenario outline, step, example keyword, example content. <br/>
   * This is useful for tracking states like tags, description that acts alike a cut-point of the main state-machine
   */
  private ParseState parentState = null;
  /**
   * The current state from the result of the think() method. <br/>
   * It is also the previous state from the previous line. <br/>
   */
  private ParseState state = START;


  private String tempScenarioTitle = "";
  private ArrayList<String> tempScenarioSteps = new ArrayList<>();
  private ArrayList<String> tempScenarioOutlineExamples = new ArrayList<>();
  /**
   * currently unused until implementation
   */
  private ArrayList<String> tempTagsLiteral = new ArrayList<>();
  private int order = 0;
  private List<Integer> scenarioOrderList = new ArrayList<>();
  private List<Integer> scenarioOutlineOrderList = new ArrayList<>();


  /**
   * @param line a line that is not a blank line or a comment line
   */
  @Override
  public void acceptThrows(String line) throws Exception {
    log.trace("  line: {}", line);
    // WARNING: at here, state is the previous state
    // SENSE: detect the properties of this line
    var senceResult = sense(line);
    // THINK: based on the state from previous line, and the detection result, decide the new state
    think(senceResult);
    log.trace("  state determined: {}", state);
    // WARNING: only at here, state is the new value of the current state
    // REACT: do what to do based on the new state
    react(line);
  }

  SenceResult sense(String line) {
    var senceResult = new SenceResult();
    //    if (LineUtil.isFeatureTitle(line)) {
//      senceResult.sawFeature = true;
//    }
//    if (LineUtil.isScenarioTitle(line)) {
//      senceResult.sawScenario = true;
//    }
//    if (LineUtil.isScenarioOutlineTitle(line)) {
//      senceResult.sawScenarioOutline = true;
//    }
//    if (LineUtil.isStep(line)) {
//      senceResult.sawStep = true;
//    }
//    if (LineUtil.isExampleKeyword(line)) {
//      senceResult.sawExampleKeyword = true;
//    }
//    if (LineUtil.isExampleContent(line)) {
//      senceResult.sawExampleContent = true;
//    }
//    if (LineUtil.isTag(line)) {
//      senceResult.sawTag = true;
//    }
    // state here is the previous state from the previous line
    // we can simplify this method to above code, but we didn't due to performance concern
    // using switch statement, we can eliminate the # of checks to be called per line
    switch (state) {
      case START:
        if (LineUtil.isFeatureTitle(line)) {
          senceResult.sawFeature = true;
        }
        break;
      case FEATURE:
        if (LineUtil.isScenarioTitle(line)) {
          senceResult.sawScenario = true;
        } else if (LineUtil.isScenarioOutlineTitle(line)) {
          senceResult.sawScenarioOutline = true;
        }
        break;
      case SCENARIO:
        if (LineUtil.isStep(line)) {
          senceResult.sawStep = true;
        }
        break;
      case SCENARIO_OUTLINE:
        if (LineUtil.isStep(line)) {
          senceResult.sawStep = true;
        }
        break;
      case DESCRIPTION:
        var previousState = state;
        // base on parent, delegate to the proper switch block
        if (parentState == FEATURE) {
          state = FEATURE;
        } else if (parentState == SCENARIO) {
          state = SCENARIO;
        } else if (parentState == SCENARIO_OUTLINE) {
          state = SCENARIO_OUTLINE;
        } else {
          // to prevent infinite recursion
          throw new EasyCucumberException(ErrorCode.EZCU025, "Illegal state transition from " + parentState + " to " + DESCRIPTION);
        }
        // this should only happen once per line
        var result = /*re*/sense(line);
        state = previousState;
        // return the delegated actual result based on parent state
        return result;
      case STEP:
        if (LineUtil.isStep(line)) {
          senceResult.sawStep = true;
        } else if (LineUtil.isExampleKeyword(line)) {
          senceResult.sawExampleKeyword = true;
        } else if (LineUtil.isScenarioTitle(line)) {
          senceResult.sawScenario = true;
        } else if (LineUtil.isScenarioOutlineTitle(line)) {
          senceResult.sawScenarioOutline = true;
        }
        break;
      case EXAMPLE_KEYWORD:
        if (LineUtil.isExampleContent(line)) {
          senceResult.sawExampleContent = true;
        }
        break;
      case EXAMPLE_CONTENT:
        if (LineUtil.isExampleContent(line)) {
          senceResult.sawExampleContent = true;
        } else if (LineUtil.isScenarioTitle(line)) {
          senceResult.sawScenario = true;
        } else if (LineUtil.isScenarioOutlineTitle(line)) {
          senceResult.sawScenarioOutline = true;
        }
        break;
    }

    return senceResult;
  }

  void think(SenceResult senceResult) {
    // state here is the previous state from the previous line
    switch (state) {
      case START:
        if (senceResult.sawFeature) {
          state = FEATURE;
        } else {
          throw new EasyCucumberException(ErrorCode.EZCU016, "The feature file must start with \"Feature:\" ");
        }
        break;
      case FEATURE:
        parentState = FEATURE;
        if (senceResult.sawScenario) {
          state = SCENARIO;
        }
//        else if (senceResult.isTag) { // un-comment when tag is supported
//          state = TAG;
//        }
        else if (senceResult.sawScenarioOutline) {
          state = SCENARIO_OUTLINE;
        } else { // this is description
          state = DESCRIPTION;
        }
        break;
      case SCENARIO:
        parentState = SCENARIO;
        if (senceResult.sawStep) {
          state = STEP;
        } else { // this is description
          state = DESCRIPTION;
        }
        break;
      case SCENARIO_OUTLINE:
        parentState = SCENARIO_OUTLINE;
        if (senceResult.sawStep) {
          state = STEP;
        } else { // this is description
          state = DESCRIPTION;
        }
        break;
      case DESCRIPTION:
        // base on parent, delegate to the proper switch block
        if (parentState == FEATURE) {
          state = FEATURE;
        } else if (parentState == SCENARIO) {
          state = SCENARIO;
        } else if (parentState == SCENARIO_OUTLINE) {
          state = SCENARIO_OUTLINE;
        } else {
          throw new EasyCucumberException(ErrorCode.EZCU024, "Illegal state transition from " + parentState + " to " + DESCRIPTION);
        }
        // this should only recurse once per line
        /*re*/
        think(senceResult);
        break;
      case STEP:
        if (!senceResult.sawStep) {
          if (parentState == SCENARIO_OUTLINE && senceResult.sawExampleKeyword) {
            state = EXAMPLE_KEYWORD;
          } else if (parentState == SCENARIO && senceResult.sawScenario) {
            state = SCENARIO;
            parentState = FEATURE;
          } else if (parentState == SCENARIO && senceResult.sawScenarioOutline) {
            state = SCENARIO_OUTLINE;
            parentState = FEATURE;
          } else {
            throw new EasyCucumberException(ErrorCode.EZCU021, "After a step, another step, example or a new scenario or scenario outline are allowed");
          }
        }
        // else, keep in step state
        break;
      case EXAMPLE_KEYWORD:
        parentState = EXAMPLE_KEYWORD;
        if (senceResult.sawExampleContent) {
          state = EXAMPLE_CONTENT;
        } else {
          throw new EasyCucumberException(ErrorCode.EZCU022, "After an example keyword, only example content is allowed");
        }
        break;
      case EXAMPLE_CONTENT:
        if (!senceResult.sawExampleContent) {
          if (parentState != EXAMPLE_KEYWORD) {
            // just a check, although I don't think this can happen
            throw new EasyCucumberException(ErrorCode.EZCU023, "Illegal state transition from " + parentState + " to " + EXAMPLE_CONTENT);
          }
          if (senceResult.sawScenario) {
            state = SCENARIO;
            parentState = FEATURE;
          } else if (senceResult.sawScenarioOutline) {
            state = SCENARIO_OUTLINE;
            parentState = FEATURE;
          } else {
            throw new EasyCucumberException(ErrorCode.EZCU024, "After an example content, another example content, scenario or scenario outline are allowed");
          }
        }
        // else, keep in example content state
        break;
    }
  }

  void react(String line) {
    switch (state) {
      case FEATURE:
        jFeatureDetailBuilder.title(line.replace(JFeatureKeyword.FEATURE, "").trim());
        break;
      case SCENARIO:
        checkTempAndBuildScenarioOrScenarioOutline();
        tempScenarioTitle = line.replace(JFeatureKeyword.SCENARIO, "").trim();
        break;
      case SCENARIO_OUTLINE:
        checkTempAndBuildScenarioOrScenarioOutline();
        tempScenarioTitle = line
            .replace(JFeatureKeyword.SCENARIO_OUTLINE, "")
            .replace(JFeatureKeyword.SCENARIO_TEMPLATE, "")
            .trim();
        break;
      case DESCRIPTION:
        // do nothing with multi-line description
        break;
      case STEP:
        tempScenarioSteps.add(line);
        break;
      case EXAMPLE_KEYWORD:
        // don't need to do anything
        break;
      case EXAMPLE_CONTENT:
        tempScenarioOutlineExamples.add(line);
        break;

    }
  }

  public boolean checkTempAndBuildScenarioOrScenarioOutline() {
    if (tempScenarioTitle != null && !tempScenarioTitle.isEmpty()) {
      if (tempScenarioOutlineExamples.isEmpty()) {
        jFeatureDetailBuilder.addScenario(detailBuilder.buildJScenarioDetail(tempScenarioTitle, tempScenarioSteps, tempTagsLiteral));
        jFeatureDetailBuilder.addScenarioOrder(order++);
      } else {
        jFeatureDetailBuilder.addScenarioOutline(detailBuilder.buildJScenarioOutlineDetail(tempScenarioTitle, tempScenarioSteps, tempScenarioOutlineExamples, tempTagsLiteral));
        jFeatureDetailBuilder.addScenarioOutlineOrder(order++);
      }
      tempScenarioTitle = "";
      tempScenarioSteps.clear();
      tempScenarioOutlineExamples.clear();
      tempTagsLiteral.clear();
      return true;
    }
    return false;
  }

  private class SenceResult {
    boolean sawFeature;
    boolean sawDescription;
    boolean sawTag;
    boolean sawScenario;
    boolean sawScenarioOutline;
    boolean sawStep;
    boolean sawExampleKeyword;
    boolean sawExampleContent;
  }

  /**
   * these getters and setters are only used by test cases
   */
  ParseState getState() {
    return state;
  }

  ParseState getParentState() {
    return parentState;
  }

  String getTempScenarioTitle() {
    return tempScenarioTitle;
  }

  ArrayList<String> getTempScenarioSteps() {
    return tempScenarioSteps;
  }

  int getOrder() {
    return order;
  }

  List<Integer> getScenarioOrderList() {
    return scenarioOrderList;
  }

  List<Integer> getScenarioOutlineOrderList() {
    return scenarioOutlineOrderList;
  }

  ArrayList<String> getTempScenarioOutlineExamples() {
    return tempScenarioOutlineExamples;
  }

  void setParentState(ParseState parentState) {
    this.parentState = parentState;
  }

  void setState(ParseState state) {
    this.state = state;
  }

  void setTempScenarioTitle(String tempScenarioTitle) {
    this.tempScenarioTitle = tempScenarioTitle;
  }

  void setTempScenarioSteps(ArrayList<String> tempScenarioSteps) {
    this.tempScenarioSteps = tempScenarioSteps;
  }

  void setOrder(int order) {
    this.order = order;
  }

  void setScenarioOrderList(List<Integer> scenarioOrderList) {
    this.scenarioOrderList = scenarioOrderList;
  }

  void setScenarioOutlineOrderList(List<Integer> scenarioOutlineOrderList) {
    this.scenarioOutlineOrderList = scenarioOutlineOrderList;
  }

  void setTempScenarioOutlineExamples(ArrayList<String> tempScenarioOutlineExamples) {
    this.tempScenarioOutlineExamples = tempScenarioOutlineExamples;
  }
}


