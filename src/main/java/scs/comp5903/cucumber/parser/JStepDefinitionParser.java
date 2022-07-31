package scs.comp5903.cucumber.parser;

import scs.comp5903.cucumber.model.JStep;
import scs.comp5903.cucumber.model.JStepDefDetail;
import scs.comp5903.cucumber.model.JStepDefMethodDetail;
import scs.comp5903.cucumber.model.JStepKeyword;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.matcher.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public class JStepDefinitionParser {

  public JStepDefDetail parse(Class<?>... stepDefinitionClass) {
    return parse(List.of(stepDefinitionClass));
  }

  public JStepDefDetail parse(List<Class<?>> stepDefinitionClasses) {
    var stepsFromAllClasses = new ArrayList<JStepDefMethodDetail>();
    for (var stepDefinitionClass : stepDefinitionClasses) {
      stepsFromAllClasses.addAll(extractOneClass(stepDefinitionClass));
    }
    return new JStepDefDetail(stepsFromAllClasses);
  }

  List<JStepDefMethodDetail> extractOneClass(Class<?> stepDefinitionClass) {
    var list = new ArrayList<JStepDefMethodDetail>();
    // be aware that the order of methods can vary depends on OS
    for (Method method : stepDefinitionClass.getMethods()) {
      var jStepAnnotation = method.getAnnotation(JStep.class);
      if (Objects.isNull(jStepAnnotation)) {
        continue;
      }
      var keyword = jStepAnnotation.keyword();
      var stepMatcherString = jStepAnnotation.value();
      var jStepMatcher = createMethodDetail(keyword, stepMatcherString);
      list.add(new JStepDefMethodDetail(method, jStepMatcher));
    }
    return list;
  }

  private AbstractJStepMatcher createMethodDetail(JStepKeyword keyword, String stepMatcherString) {
    switch (keyword) {
      case GIVEN:
        return new GivenJStepMatcher(stepMatcherString);
      case WHEN:
        return new WhenJStepMatcher(stepMatcherString);
      case THEN:
        return new ThenJStepMatcher(stepMatcherString);
      case AND:
        return new AndJStepMatcher(stepMatcherString);
      case BUT:
        return new ButJStepMatcher(stepMatcherString);
      default:
        // this shouldn't happen
        throw new EasyCucumberException(ErrorCode.EZCU004, "Unknown step definition keyword: " + keyword);
    }
  }
}
