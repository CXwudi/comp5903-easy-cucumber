package scs.comp5903.cucumber.parser.jstep;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.step.*;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jstepdef.JStepDefMethodDetail;
import scs.comp5903.cucumber.model.jstepdef.matcher.*;
import scs.comp5903.cucumber.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-12
 */
public class JStepDefinitionMethodParser {

  private static final Logger log = getLogger(JStepDefinitionMethodParser.class);


  List<JStepDefMethodDetail> extractOneClass(Class<?> stepDefinitionClass) {

    var list = new ArrayList<JStepDefMethodDetail>();
    // be aware that the order of methods can vary depends on OS
    var clazzIsPublic = false;
    for (Method method : stepDefinitionClass.getMethods()) { // this gets only public methods
      JStepKeyword keyword = null;
      String stepMatcherString = null;
      for (var annotation : method.getAnnotations()) {
        if (annotation instanceof JStep) {
          keyword = ((JStep) annotation).keyword();
          stepMatcherString = ((JStep) annotation).value();
        } else if (annotation instanceof JGivenStep) {
          keyword = JStepKeyword.GIVEN;
          stepMatcherString = ((JGivenStep) annotation).value();
        } else if (annotation instanceof JWhenStep) {
          keyword = JStepKeyword.WHEN;
          stepMatcherString = ((JWhenStep) annotation).value();
        } else if (annotation instanceof JThenStep) {
          keyword = JStepKeyword.THEN;
          stepMatcherString = ((JThenStep) annotation).value();
        } else if (annotation instanceof JAndStep) {
          keyword = JStepKeyword.AND;
          stepMatcherString = ((JAndStep) annotation).value();
        } else if (annotation instanceof JButStep) {
          keyword = JStepKeyword.BUT;
          stepMatcherString = ((JButStep) annotation).value();
        }
        if (Objects.nonNull(keyword)) {
          break;
        }
      }
      if (Objects.isNull(keyword)) { // means no jstep annotation
        continue;
      } else if (!clazzIsPublic) {
        // once we see at least one method with @JStep annotation, we know this is step definition class and then can perform check of class modifier
        ReflectionUtil.requireClassIsPublic(stepDefinitionClass);
        clazzIsPublic = true;
      }
      var jStepMatcher = createMethodDetail(keyword, stepMatcherString);
      var methodDetail = new JStepDefMethodDetail(method, jStepMatcher);
      log.debug("Created data class for step definition method: {} {}", keyword, stepMatcherString);
      list.add(methodDetail);
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
