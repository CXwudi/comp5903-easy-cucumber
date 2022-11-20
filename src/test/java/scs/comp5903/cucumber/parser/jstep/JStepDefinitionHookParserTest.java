package scs.comp5903.cucumber.parser.jstep;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.execution.JScenarioStatus;
import scs.comp5903.cucumber.model.annotation.hook.BeforeEachJScenario;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.jstepdef.JHookType;
import scs.comp5903.cucumber.parser.samplestepdef.SampleHookDefinition;
import scs.comp5903.cucumber.parser.samplestepdef.SampleInvalidHookDefClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-19
 */
class JStepDefinitionHookParserTest {

  private final JStepDefinitionHookParser hookParser = new JStepDefinitionHookParser();

  @Test
  void canParseAllHooks() {
    var hookDetails = hookParser.extractOneClass(SampleHookDefinition.class);
    assertEquals(6, hookDetails.size());
    assertTrue(hookDetails.stream().anyMatch(hookDetail ->
        hookDetail.getType().equals(JHookType.BEFORE_ALL_JSCENARIOS) && hookDetail.getOrder() == 1));
    assertTrue(hookDetails.stream().anyMatch(hookDetail ->
        hookDetail.getType().equals(JHookType.AFTER_ALL_JSCENARIOS) && hookDetail.getOrder() == 2));
    assertTrue(hookDetails.stream().anyMatch(hookDetail ->
        hookDetail.getType().equals(JHookType.BEFORE_EACH_JSCENARIO) && hookDetail.getOrder() == 3));
    assertTrue(hookDetails.stream().anyMatch(hookDetail ->
        hookDetail.getType().equals(JHookType.AFTER_EACH_JSCENARIO) && hookDetail.getOrder() == 4));
    assertTrue(hookDetails.stream().anyMatch(hookDetail ->
        hookDetail.getType().equals(JHookType.BEFORE_EACH_JSTEP) && hookDetail.getOrder() == 5));
    assertTrue(hookDetails.stream().anyMatch(hookDetail ->
        hookDetail.getType().equals(JHookType.AFTER_EACH_JSTEP) && hookDetail.getOrder() == 6));
  }

  @Test
  void shouldThrowOnNonPublicStepDefClass() {
    var exp = assertThrows(EasyCucumberException.class, () -> hookParser.extractOneClass(NonPublicHookDefClass.class));
    assertTrue(exp.getMessage().contains(" is not public"));
  }

  @Test
  void doesNotThrowIfTheClassIsNotStepDef() {
    assertDoesNotThrow(() -> hookParser.extractOneClass(NonHookDefClass.class));
  }

  @Test
  void shouldThrowOnInvalidBeforeAfterAllScenariosHook() {
    var exp = assertThrows(EasyCucumberException.class, () -> hookParser.extractOneClass(SampleInvalidHookDefClass.class));
    assertTrue(exp.getMessage().contains("should not have any parameters"), "Wrong expecting exp msg: " + exp.getMessage());
  }

  //TODO: add more tests for invalid hooks

}

class NonPublicHookDefClass {
  @BeforeEachJScenario
  public void nonPublicHookDefMethod(JScenarioStatus status) {
    // do nothing
  }

}

class NonHookDefClass {
  public void aMethod() {
  }
}