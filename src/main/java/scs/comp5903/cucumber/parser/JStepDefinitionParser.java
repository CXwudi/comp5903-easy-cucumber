package scs.comp5903.cucumber.parser;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.step.*;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jstepdef.JStepDefDetail;
import scs.comp5903.cucumber.model.jstepdef.JStepDefMethodDetail;
import scs.comp5903.cucumber.model.jstepdef.matcher.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public class JStepDefinitionParser {

  private static final Logger log = getLogger(JStepDefinitionParser.class);

  public JStepDefDetail parse(Class<?>... stepDefinitionClass) {
    return parse(List.of(stepDefinitionClass));
  }

  public JStepDefDetail parse(List<Class<?>> stepDefinitionClasses) {
    log.info("Start parsing step definition classes: {}", stepDefinitionClasses);
    var stepsFromAllClasses = new ArrayList<JStepDefMethodDetail>();
    for (var stepDefinitionClass : stepDefinitionClasses) {
      stepsFromAllClasses.addAll(extractOneClass(stepDefinitionClass));
    }
    log.info("Done parsing step definition classes: {}", stepDefinitionClasses);
    return new JStepDefDetail(stepsFromAllClasses);
  }

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
        if (keyword != null) {
          break;
        }
      }
      if (Objects.isNull(keyword)) { // means no jstep annotation
        continue;
      } else {
        // once we see at least one method with @JStep annotation, we know this is step definition class and then can perform check of class modifier
        clazzIsPublic = checkClazzIsPublicOrThrow(stepDefinitionClass, clazzIsPublic);
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

  private boolean checkClazzIsPublicOrThrow(Class<?> stepDefinitionClass, boolean isPublic) {
    if (isPublic || Modifier.isPublic(stepDefinitionClass.getModifiers())) {
      return true;
    } else {
      throw new EasyCucumberException(ErrorCode.EZCU038, "Step definition class must be public: " + stepDefinitionClass.getName());
    }
  }
}
