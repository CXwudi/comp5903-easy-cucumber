package scs.comp5903.cucumber.sample;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.JStepKeyword;
import scs.comp5903.cucumber.model.annotation.JStep;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DecimalAndBigIntStepDef {

  public DecimalAndBigIntStepDef() {
  }

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(DecimalAndBigIntStepDef.class);

  @JStep(keyword = JStepKeyword.GIVEN, value = "{biginteger} as big integer")
  public void s1(BigInteger biginteger) {
    log.info("{} as big integer", biginteger);
  }

  @JStep(keyword = JStepKeyword.AND, value = "{bigdecimal} as big decimal")
  public void s2(BigDecimal bigdecimal) {
    log.info("{} as big decimal", bigdecimal);
  }

  @JStep(keyword = JStepKeyword.AND, value = "{double} as double")
  public void s3(double d) {
    log.info("{} as double", d);
  }

  @JStep(keyword = JStepKeyword.THEN, value = "I should not crash")
  public void then() {
    log.info("then");
  }
}
