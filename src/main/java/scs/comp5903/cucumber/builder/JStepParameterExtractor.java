package scs.comp5903.cucumber.builder;

import scs.comp5903.cucumber.model.JStepDefMethodDetail;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jstep.*;
import scs.comp5903.cucumber.model.matcher.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * It tries to match the {@link AbstractJStep} with the {@link JStepDefMethodDetail} <br/>
 * if they match, it will extract out parameters
 *
 * @author Charles Chen 101035684
 * @date 2022-06-29
 */
public class JStepParameterExtractor {
  /**
   * simply match the digits with an optional minus sign
   */
  static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");
  /**
   * same as {@link #INTEGER_PATTERN} but with an optional decimal point, and an optional exponent
   */
  static final Pattern FLOATING_POINT_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?((E|e)-?\\d+)?");
  /**
   * match either "content" or 'content'
   */
  static final Pattern STRING_PATTERN = Pattern.compile("(\"[^\"]*\")|('[^']*')");

  /**
   * Given a step and a step def method detail, try to extract the parameters from the step based on the step def method detail
   *
   * @param jStep          the step to extract parameters from
   * @param jStepDefDetail the step def method detail that expresses the parameters to extract
   * @return none if jstep doesn't match this step definition <br/>
   * else, return list of parameters, can be empty lists meaning no parameters
   */
  public Optional<List<Object>> tryExtractParameters(AbstractJStep jStep, JStepDefMethodDetail jStepDefDetail) {
    var parameters = new ArrayList<>();
    var jStepMatcher = jStepDefDetail.getMatcher();
    if (!isSameKeyword(jStep, jStepMatcher)) { // not matching
      return Optional.empty();
    }
    var jStepStr = jStep.getStepString(); // e.g. "I am a step with "string" and int 5"
    var matchingStr = jStepMatcher.getMatchingString(); // e.g. "I am a step with {string} and int {int}"

    for (int j = 0, m = 0; j < jStepStr.length() && m < matchingStr.length(); ) {
      // each iteration is new one step of walking both strings
      var jc = jStepStr.charAt(j);
      var mc = matchingStr.charAt(m);
      if (jc == mc) {
        j++;
        m++;
      } else {
        if (mc == '{') {
          var endIndex = matchingStr.indexOf('}', m);
          if (endIndex == -1) {
            throw new EasyCucumberException(ErrorCode.EZCU012, "Uncompleted parameter type: " + matchingStr.substring(m));
          }
          var parameterType = matchingStr.substring(m + 1, endIndex);
          var endingChar = endIndex == matchingStr.length() - 1 ? null : matchingStr.charAt(endIndex + 1);
          var jOpt = extractParameterValueAndGetNextIndex(jStepStr, j, endingChar, parameterType, parameters);
          if (jOpt.isEmpty()) { // means a miss match is actually happened.
            return Optional.empty();
          }
          j = jOpt.get();
          m = endIndex + 1;
        } else { // not matching
          return Optional.empty();
        }
      }
    }
    // until here all matches, check if the size of extracted parameters is the same as the size of parameters in method
    if (parameters.size() != jStepDefDetail.getMethod().getParameterTypes().length) {
      throw new EasyCucumberException(ErrorCode.EZCU015,
          "The amount of extracted parameters doesn't match the amount of parameters of the step definition: " + jStepDefDetail.getMethod().getName() +
              ", please check your step definition declaration.");
    }
    // e.g. [ "string", 5 ]
    return Optional.of(parameters);
  }

  /**
   * extraction is done given the
   *
   * @param jStepStr                the whole jstep string literal
   * @param j                       the starting index of jStepStr for beginning matching
   * @param endingCharOnMatchingStr the ending character right after the '}' in the matching string <br/>
   *                                {@code null} means there is no more char after '}'
   *                                and all string after index j are matched <br/>
   *                                this parameter is useful if the extraction relays
   *                                on reading the ending character instead of regex
   * @param parameterType           the parameter type, e.g. "string" or "int"
   * @param parameters              this is the input/output parameter list, the extracted parameter will be added to this list
   * @return the next index of jStepStr that should continue checking the matching, after the parameter is extracted
   */
  Optional<Integer> extractParameterValueAndGetNextIndex(String jStepStr, int j, Character endingCharOnMatchingStr, String parameterType, ArrayList<Object> parameters) {
    var jStepSubStr = jStepStr.substring(j);
    switch (parameterType.toLowerCase()) {
      case "int":
        var matcher = INTEGER_PATTERN.matcher(jStepSubStr);
        var found = matcher.find(0);
        if (!found) {
          throw new EasyCucumberException(ErrorCode.EZCU009, "Unable to find integer parameter as int: " + jStepSubStr);
        }
        var intStr = matcher.group();
        if (jStepSubStr.indexOf(intStr) != 0) { // failed to match from index 0 == miss match
          return Optional.empty();
        } else {
          parameters.add(Integer.parseInt(intStr));
          return Optional.of(j + matcher.end());
        }
      case "double":
        matcher = FLOATING_POINT_PATTERN.matcher(jStepSubStr);
        found = matcher.find(0);
        if (!found) {
          throw new EasyCucumberException(ErrorCode.EZCU027, "Unable to find floating point parameter as double: " + jStepSubStr);
        }
        var doubleStr = matcher.group();
        if (jStepSubStr.indexOf(doubleStr) != 0) { // failed to match from index 0 == miss match
          return Optional.empty();
        } else {
          parameters.add(Double.parseDouble(doubleStr));
          return Optional.of(j + matcher.end());
        }
      case "string":
        matcher = STRING_PATTERN.matcher(jStepSubStr);
        found = matcher.find(0);
        if (!found) {
          throw new EasyCucumberException(ErrorCode.EZCU010, "Unable to find string literals: " + jStepSubStr +
              ", the string literal should be in double or single quotes.");
        }
        var matchedStr = matcher.group();
        if (jStepSubStr.indexOf(matchedStr) != 0) { // failed to match from index 0 == miss match
          return Optional.empty();
        } else {
          parameters.add(matchedStr.substring(1, matchedStr.length() - 1));
          return Optional.of(j + matcher.end());
        }
      case "biginteger":
        matcher = INTEGER_PATTERN.matcher(jStepSubStr);
        found = matcher.find(0);
        if (!found) {
          throw new EasyCucumberException(ErrorCode.EZCU026, "Unable to find integer parameter as big integer: " + jStepSubStr);
        }
        intStr = matcher.group();
        if (jStepSubStr.indexOf(intStr) != 0) { // failed to match from index 0 == miss match
          return Optional.empty();
        } else {
          parameters.add(new BigInteger(intStr));
          return Optional.of(j + matcher.end());
        }
      case "bigdecimal":
        matcher = FLOATING_POINT_PATTERN.matcher(jStepSubStr);
        found = matcher.find(0);
        if (!found) {
          throw new EasyCucumberException(ErrorCode.EZCU028, "Unable to find floating point parameter as big decimal: " + jStepSubStr);
        }
        doubleStr = matcher.group();
        if (jStepSubStr.indexOf(doubleStr) != 0) { // failed to match from index 0 == miss match
          return Optional.empty();
        } else {
          parameters.add(new BigDecimal(doubleStr));
          return Optional.of(j + matcher.end());
        }
      case "": // the special case of {}, which needs the endingCharOnMatchingStr
        if (endingCharOnMatchingStr == null) { // null == it is all the way to the end of jStepStr
          parameters.add(jStepSubStr);
          return Optional.of(jStepStr.length());
        } else {
          var endIndex = jStepStr.indexOf(endingCharOnMatchingStr, j);
          // the ending index should always be found here.
          parameters.add(jStepStr.substring(j, endIndex));
          return Optional.of(endIndex);
        }
      default:
        throw new EasyCucumberException(ErrorCode.EZCU011, "Invalid parameter type: " + parameterType);
    }
  }

  boolean isSameKeyword(AbstractJStep jStep, AbstractJStepMatcher jStepMatcher) {
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
