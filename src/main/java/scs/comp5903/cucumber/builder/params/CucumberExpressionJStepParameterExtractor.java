package scs.comp5903.cucumber.builder.params;

import io.cucumber.cucumberexpressions.ExpressionFactory;
import io.cucumber.cucumberexpressions.ParameterTypeRegistry;
import scs.comp5903.cucumber.model.jfeature.jstep.AbstractJStep;
import scs.comp5903.cucumber.model.jstepdef.JStepDefMethodDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Charles Chen 101035684
 * @date 2022-12-07
 */
public class CucumberExpressionJStepParameterExtractor implements JStepParameterExtractor {
  private static final ParameterTypeRegistry parameterTypeRegistry;
  private static final ExpressionFactory expressionFactory;

  static {
    parameterTypeRegistry = new ParameterTypeRegistry(Locale.US);
    expressionFactory = new ExpressionFactory(parameterTypeRegistry);
  }
  @Override
  public Optional<List<Object>> tryExtractParameters(AbstractJStep jStep, JStepDefMethodDetail jStepDefDetail) {
    var parameters = new ArrayList<>();
    var jStepMatcher = jStepDefDetail.getMatcher();
    if (!isSameKeyword(jStep, jStepMatcher)) { // not matching
      return Optional.empty();
    }
    var jStepStr = jStep.getStepString(); // e.g. "I am a step with "string" and int 5"
    var matchingStr = jStepMatcher.getMatchingString(); // e.g. "I am a step with {string} and int {int}"

    var expression = expressionFactory.createExpression(matchingStr);
    var arguments = expression.match(jStepStr);
    if (arguments == null) { // not matching
      return Optional.empty();
    } else {
      arguments.forEach(argument -> parameters.add(argument.getValue()));
      return Optional.of(parameters);
    }
  }
}
