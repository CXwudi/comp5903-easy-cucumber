package scs.comp5903.cucumber.builder.params;

import io.cucumber.cucumberexpressions.Argument;
import io.cucumber.cucumberexpressions.ExpressionFactory;
import io.cucumber.cucumberexpressions.ParameterTypeRegistry;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-12-07
 */
@Disabled("This is a POC test, not a real test")
class CucumberExpressionPocTest {

  private final ParameterTypeRegistry parameterTypeRegistry = new ParameterTypeRegistry(Locale.ENGLISH);
  private final ExpressionFactory expressionFactory = new ExpressionFactory(parameterTypeRegistry);

  @Test
  void tryOutCucumberExpression() {
    var expression = expressionFactory.createExpression("I have a {string} and an int {int}");
    var arguments = expression.match("I have a \"string\" and an int 5");
    assertNotNull(arguments);
    var values = arguments.stream().map(Argument::getValue).collect(Collectors.toList());
    assertTrue(values.containsAll(List.of("string", 5)));
  }

  @Test
  void whatAboutMismatch() {
    var expression = expressionFactory.createExpression("I have a {string} and an int {int}");
    var arguments = expression.match("I have a \"string\" and an int 5.5");
    assertNull(arguments);
  }
}
