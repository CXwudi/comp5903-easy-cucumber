package scs.comp5903.cucumber.builder.params;

import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jfeature.jstep.*;
import scs.comp5903.cucumber.model.jstepdef.JStepDefMethodDetail;
import scs.comp5903.cucumber.model.jstepdef.matcher.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Charles Chen 101035684
 * @date 2022-12-07
 */
public interface JStepParameterExtractor {
  Optional<List<Object>> tryExtractParameters(AbstractJStep jStep, JStepDefMethodDetail jStepDefDetail);

  default boolean isSameKeyword(AbstractJStep jStep, AbstractJStepMatcher jStepMatcher) {
    if (jStep instanceof GivenStep) {
      return jStepMatcher instanceof GivenJStepMatcher;
    } else if (jStep instanceof WhenStep) {
      return jStepMatcher instanceof WhenJStepMatcher;
    } else if (jStep instanceof ThenStep) {
      return jStepMatcher instanceof ThenJStepMatcher;
    } else if (jStep instanceof AndStep) {
      return jStepMatcher instanceof AndJStepMatcher;
    } else if (jStep instanceof ButStep) {
      return jStepMatcher instanceof ButJStepMatcher;
    } else {
      throw new EasyCucumberException(ErrorCode.EZCU008, "we are seeing unknown type here??");
    }
  }
}
