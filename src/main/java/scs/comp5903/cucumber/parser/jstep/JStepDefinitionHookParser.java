package scs.comp5903.cucumber.parser.jstep;

import org.slf4j.Logger;
import scs.comp5903.cucumber.execution.JScenarioStatus;
import scs.comp5903.cucumber.model.annotation.hook.*;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jstepdef.JHookType;
import scs.comp5903.cucumber.model.jstepdef.JStepDefHookDetail;
import scs.comp5903.cucumber.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-19
 */
public class JStepDefinitionHookParser {

  private static final Logger log = getLogger(JStepDefinitionHookParser.class);

  List<JStepDefHookDetail> extractOneClass(Class<?> stepDefinitionClass) {
    var list = new ArrayList<JStepDefHookDetail>();
    // be aware that the order of methods can vary depends on OS
    var clazzIsPublic = false;
    for (var method : stepDefinitionClass.getMethods()) {
      JHookType type = null;
      int order = -1;
      for (var annotation: method.getAnnotations()) {
        if (annotation instanceof BeforeAllJScenarios) {
          type = JHookType.BEFORE_ALL_JSCENARIOS;
          order = ((BeforeAllJScenarios) annotation).order();
        } else if (annotation instanceof AfterAllJScenarios) {
          type = JHookType.AFTER_ALL_JSCENARIOS;
          order = ((AfterAllJScenarios) annotation).order();
        } else if (annotation instanceof BeforeEachJScenario) {
          type = JHookType.BEFORE_EACH_JSCENARIO;
          order = ((BeforeEachJScenario) annotation).order();
        } else if (annotation instanceof AfterEachJScenario) {
          type = JHookType.AFTER_EACH_JSCENARIO;
          order = ((AfterEachJScenario) annotation).order();
        } else if (annotation instanceof BeforeEachJStep) {
          type = JHookType.BEFORE_EACH_JSTEP;
          order = ((BeforeEachJStep) annotation).order();
        } else if (annotation instanceof AfterEachJStep) {
          type = JHookType.AFTER_EACH_JSTEP;
          order = ((AfterEachJStep) annotation).order();
        }
        if (type != null) {
          checkMethodParameters(method, type);
          break;
        }
      }
      if (Objects.isNull(type)) { // means no hook annotation
        continue;
      } else if (!clazzIsPublic) {
        // once we see at least one method with hooks annotation, we know this is step definition class and then can perform check of class modifier
        ReflectionUtil.requireClassIsPublic(stepDefinitionClass);
        clazzIsPublic = true;
      }
      var stepDefHookDetail = new JStepDefHookDetail(method, type, order);
      log.debug("Created data class for hook definition method: {} {}", type, method.getName());
      list.add(stepDefHookDetail);
    }
    return list;
  }

  private void checkMethodParameters(Method method, JHookType type) {
    switch (type) {
      case BEFORE_ALL_JSCENARIOS:
      case AFTER_ALL_JSCENARIOS:
        if (method.getParameterCount() != 0) {
          throw new EasyCucumberException(ErrorCode.EZCU026, String.format("%s hook method %s#%s should not have any parameters", type, method.getDeclaringClass().getSimpleName(), method.getName()));
        }
        break;
      case BEFORE_EACH_JSCENARIO:
      case AFTER_EACH_JSCENARIO:
      case BEFORE_EACH_JSTEP:
      case AFTER_EACH_JSTEP:
        int paramCount = method.getParameterCount();
        if (paramCount > 1) {
          throw new EasyCucumberException(ErrorCode.EZCU027, String.format("%s hook method %s#%s should not have more than one parameter which is just the %s", type, method.getDeclaringClass().getSimpleName(), method.getName(), JScenarioStatus.class.getSimpleName()));
        } else if (paramCount == 1) {
          var paramType = method.getParameterTypes()[0];
          if (!paramType.equals(JScenarioStatus.class)) {
            throw new EasyCucumberException(ErrorCode.EZCU028, String.format("%s hook method %s#%s can only contain one parameter of type %s", type, method.getDeclaringClass().getSimpleName(), method.getName(), JScenarioStatus.class.getSimpleName()));
          }
        }
        break;
      default:
        throw new EasyCucumberException(ErrorCode.EZCU032, String.format("Unknown hook type: %s", type));
    }
  }
}
