package scs.comp5903.cucumber.parser.jstep;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.jstepdef.JStepDefDetail;
import scs.comp5903.cucumber.model.jstepdef.JStepDefHookDetail;
import scs.comp5903.cucumber.model.jstepdef.JStepDefMethodDetail;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public class JStepDefinitionParser {

  private static final Logger log = getLogger(JStepDefinitionParser.class);

  final JStepDefinitionMethodParser methodParser;

  public JStepDefinitionParser(JStepDefinitionMethodParser methodParser) {
    this.methodParser = methodParser;
  }

  public JStepDefDetail parse(Class<?>... stepDefinitionClass) {
    return parse(List.of(stepDefinitionClass));
  }

  public JStepDefDetail parse(List<Class<?>> stepDefinitionClasses) {
    log.info("Start parsing step definition classes: {}", stepDefinitionClasses);
    var stepsFromAllClasses = new ArrayList<JStepDefMethodDetail>();
    var hooksFromAllClasses = new ArrayList<JStepDefHookDetail>();
    for (var stepDefinitionClass : stepDefinitionClasses) {
      stepsFromAllClasses.addAll(methodParser.extractOneClass(stepDefinitionClass));
    }
    log.info("Done parsing step definition classes: {}", stepDefinitionClasses);
    return new JStepDefDetail(stepsFromAllClasses, List.of());
  }

}
