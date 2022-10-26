package scs.comp5903.cucumber.builder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-13
 */
class JStepParameterExtractorRegexTest {
  private final JStepParameterExtractor jStepParameterExtractor = new JStepParameterExtractor();

  static Stream<Arguments> argumentsStream() {
    return Stream.of(
        // input: String jStepStr, int j, Character endingChar, String parameterType,
        // expected output: Object expectedExtractedValue, int expectedNextIndex, Class<?> expectedClass
        Arguments.of("I have a \"string1\" and an int 5", 9, ' ', "string", "string1", 18, String.class),
        Arguments.of("I have a \"string1\" and another string \"string2\"", 9, ' ', "string", "string1", 18, String.class),
        Arguments.of("I have a \"string1\" and an int 5", 30, null, "int", 5, 31, Integer.class),
        Arguments.of("'multi\"quota\"str' should works", 0, ' ', "string", "multi\"quota\"str", 17, String.class),
        Arguments.of("\"multi'quota'str\" should works", 0, ' ', "string", "multi'quota'str", 17, String.class),
        Arguments.of("match -10-10 should get -10", 6, '-', "int", -10, 9, Integer.class),
        Arguments.of("match 1234567890 as big integer", 6, ' ', "biginteger", new BigInteger("1234567890"), 16, BigInteger.class),
        Arguments.of("can match -3.7 as double", 10, ' ', "double", -3.7, 14, Double.class),
        Arguments.of("can match -3.7e2 as double", 10, ' ', "double", -3.7e2, 16, Double.class),
        Arguments.of("can match -3.7E2 as double", 10, ' ', "double", -3.7E2, 16, Double.class),
        Arguments.of("can match 3E2 as double", 10, ' ', "double", 3e2, 13, Double.class),
        Arguments.of("can match -3.7 as big decimal", 10, ' ', "bigdecimal", new BigDecimal("-3.7"), 14, BigDecimal.class),
        Arguments.of("can match very small -3.7e-5 as big decimal", 21, ' ', "bigdecimal", new BigDecimal("-3.7e-5"), 28, BigDecimal.class),
        Arguments.of("can match very large 3.7e4 as big decimal", 21, ' ', "bigdecimal", new BigDecimal("3.7e4"), 26, BigDecimal.class),
        Arguments.of("can parse any string in step", 14, ' ', "", "string", 20, String.class),
        Arguments.of("can parse ending string", 17, null, "", "string", 23, String.class)
    );
  }

  @ParameterizedTest
  @MethodSource("argumentsStream")
  void testExtractWithRegex(String jStepStr, int j, Character endingChar, String parameterType, Object expectedExtractedValue, int expectedNextIndex, Class<?> expectedClass) {
    var results = new ArrayList<>();
    var nextIndex = jStepParameterExtractor.extractParameterValueAndGetNextIndex(jStepStr, j, endingChar, parameterType, results);
    assertEquals(expectedClass, results.get(0).getClass());
    assertEquals(expectedExtractedValue, results.get(0));
    assertEquals(expectedNextIndex, nextIndex.orElseThrow());
  }

  @Test
  void pocParsing() {
    assertThrows(NumberFormatException.class, () -> Integer.parseInt("1.1e2"));
    assertDoesNotThrow(() -> Double.parseDouble("1.1e-2"));
  }
}