package scs.comp5903.cucumber.original;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @deprecated preserve it for reference
 */
@Deprecated(since = "0.0.1")
public class JScenario {
  private ArrayList<String> allFileLines;
  /**
   * the dashed_method_name (which get rigged of all parameters) to parameter types map
   * refactor idea: store the actual method instead of the dashed_method_name, probably will use literatures in JStep as keys
   */
  private HashMap<String, ArrayList<String>> methods;
  private Class<?> stepDefsClass;

  public JScenario(String fn, Class<?> sd) throws Exception {
    allFileLines = openFile(fn);
    stepDefsClass = sd;
    // run(); gonna let the user do that
  }

  /**
   * Opens, reads and saves step definitions from a file into the steps class variable
   *
   * @param filename filepath of scenario
   * @return an ArrayList of steps
   */
  private ArrayList<String> openFile(String filename) {
    ArrayList<String> allFileLines = new ArrayList<>();
    try {
      Scanner scanner = new Scanner(new File(filename));
      while (scanner.hasNextLine()) {
        allFileLines.add(scanner.nextLine());
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return allFileLines;
  }

  /**
   * Main logic of class - calls parsing functions and invokes step methods
   *
   * @throws Exception
   */
  public void run() throws Exception {
    // this requires that the class need the no arg constructor
    Object obj = stepDefsClass.getDeclaredConstructor().newInstance();

    parseStepDefs();
    HashMap<String, ArrayList<String>> featureDetails = parseFeatureFile();
    System.out.println("Running feature: " + featureDetails.get("title") + "\n");
    for (String k : featureDetails.keySet()) {

      if (k.equals("title")) {
        continue;
      }
      // list of pairs of dashed_method_name, parameter spots & values from the feature literature
      ArrayList<AbstractMap.SimpleEntry<String, ArrayList<Object>>> scenarioSteps = parseSteps(featureDetails.get(k));
      int numExamples = 1;

      //detect use of example table
      //list of parameter values, in terms of key-value pairs (param_name, param_value). value can be either int or string
      ArrayList<LinkedHashMap<String, Object>> examples = null;
      if (k.contains(": Example ")) { // it uses a hardcoded flag in scenario title string to indicate example table
        examples = parseExamples(featureDetails.get(k));
        numExamples = examples.size();
      } else {
        System.out.println("Scenario: " + k);
      }

      for (int row = 0; row < numExamples; ++row) {
        if (k.contains(": Example ")) {
          System.out.println("Scenario: " + k + (row + 1));
        }
        for (AbstractMap.SimpleEntry<String, ArrayList<Object>> stepMap : scenarioSteps) {
          String step = stepMap.getKey();
          ArrayList<Object> paramList = stepMap.getValue();
          Method method = stepDefsClass.getDeclaredMethod(step, getParamTypes(step));
          method.setAccessible(true);

          ArrayList<Object> params = new ArrayList<>();
          if (k.contains(": Example ")) {
            //find corresponding parameter in table to parameter name in step
            for (Object param : paramList) {
              // if it is a parameter spot, replace it with the corresponding parameter value from examples table
              if (param instanceof String && ((String) param).startsWith("<") && ((String) param).endsWith(">")) {
                params.add(examples.get(row).get(((String) param).replace("<", "").replace(">", "")));
              } else {
                params.add(param);
              }
            }
          } else {
            params = paramList;
          }
          method.invoke(obj, params.toArray());
        }
      }
    }
  }

  /**
   * Parses jfeature file and separates it into separate scenarios, and identifies the feature title  <br/>
   * refactor idea: use a dedicated class instead of {@code HashMap<String, ArrayList<String>> }
   * two classes: one is called JFeatureDetail, the other is called JScenarioDetails
   *
   * @return a map of either <br/>
   * "title" -> singleton list of feature title <br/>
   * scenario title -> list of steps literatures
   */
  private HashMap<String, ArrayList<String>> parseFeatureFile() {
    HashMap<String, ArrayList<String>> featureDetails = new HashMap<>(allFileLines.size());
    ArrayList<String> tempScenarioSteps = new ArrayList<>();
    String tempScenarioTitle = "";

    for (String line : allFileLines) {
      //title of feature
      if (line.startsWith("Feature:")) {
        String featureTitle = line.replace("Feature:", "").trim();
        featureDetails.put("title", new ArrayList<>(Arrays.asList(featureTitle)));
      } else if (line.trim().startsWith("Scenario Outline:")) { // when see scenario outline, create new entry
        if (!"".equals(tempScenarioTitle)) { // this is where when a scenario is done, then simply add it to the map
          featureDetails.put(tempScenarioTitle, tempScenarioSteps);
          tempScenarioSteps = new ArrayList<>();
        }
        tempScenarioTitle = line.replace("Scenario Outline:", "").trim() + ": Example ";
      } else if (line.trim().startsWith("Scenario:")) { // same above
        //finalize previous scenario
        if (!"".equals(tempScenarioTitle)) {
          featureDetails.put(tempScenarioTitle, tempScenarioSteps);
          tempScenarioSteps = new ArrayList<>();
        }
        tempScenarioTitle = line.replace("Scenario:", "").trim();
      } else if (!"".equals(line.trim())) {  // for current scenario (outline), add their step lines
        tempScenarioSteps.add(line.trim());
      }
    }

    //add last scenario
    if (!"".equals(tempScenarioTitle)) {
      featureDetails.put(tempScenarioTitle, tempScenarioSteps);
    }

    return featureDetails;
  }

  /**
   * Looks up parameter types of a given step in the methods HashMap
   *
   * @param step for which the parameter types will be retrieved
   * @return an array of Class objects corresponding to the step's parameter types
   */
  private Class[] getParamTypes(String step) {
    ArrayList<String> pTypes = methods.get(step);
    Class[] paramTypes = new Class[pTypes.size()];
    for (int i = 0; i < pTypes.size(); ++i) {
      if (pTypes.get(i).equals("int")) {
        paramTypes[i] = int.class;
      } else if (pTypes.get(i).equals("string")) {
        paramTypes[i] = String.class;
      }
    }
    return paramTypes;
  }

  /**
   * Given scenario outline, parse the example table into a list of key-value pairs
   *
   * @param steps all steps of a scenario outline, which is the list from the {@code HashMap<String, ArrayList<String>>}
   * @return list of parameter values, in terms of key-value pairs (param_name, param_value). value can be either int or string
   */
  private ArrayList<LinkedHashMap<String, Object>> parseExamples(ArrayList<String> steps) {

    ArrayList<LinkedHashMap<String, Object>> examples = new ArrayList<>();
    boolean isExample = false;
    ArrayList<String> paramNames = new ArrayList<>();
    for (String step : steps) {

      if (step.trim().contains("Examples:")) {
        isExample = true;
        continue;
      }

      if (isExample) {
        if (paramNames.isEmpty()) {
          for (String s : step.split("\\|")) {
            if (!s.trim().equals("")) {
              paramNames.add(s.trim());
            }
          }
        } else { // this block should not be executed since this function is only executed for scenario outlines
          LinkedHashMap<String, Object> params = new LinkedHashMap<>();
          int count = 0;
          for (String s : step.split("\\|")) {
            if (!s.trim().equals("")) {
              if (isNumeric(s.trim())) { // this is where we can implement the optional type conversion functionality
                params.put(paramNames.get(count), Integer.parseInt(s.trim()));
              } else {
                params.put(paramNames.get(count), s.replace("\"", "").trim());
              }
              count++;
            }
          }
          examples.add(params);
        }
      }
    }
    return examples;
  }

  /**
   * Parses the scenario's steps into an ArrayList, which contains Maps with the dashed_method_name as keys, and ArrayLists of parameters spots and parameters value as values.
   * <p>
   * a parameter spot is the {@code <xxx>} string and any {@code "xxx"} word and integer word are traded as corresponding string parameter value and int parameter value respectively
   * <p>
   * CX: parse each scenaio' steps into pair of method_name, [parameters spots and values] <p>
   * input: the list of steps from the Map<String, ArrayList<String>>
   *
   * @return list of pairs of dashed_method_name, parameter spots & values from the feature literature
   */
  private ArrayList<AbstractMap.SimpleEntry<String, ArrayList<Object>>> parseSteps(ArrayList<String> steps) throws Exception {
    ArrayList<AbstractMap.SimpleEntry<String, ArrayList<Object>>> parsedSteps = new ArrayList<>();
    for (int i = 0; i < steps.size(); ++i) {

      //stop if example table is detected
      if ("Examples:".equals(steps.get(i).trim())) {
        break;
      }

      String[] words = steps.get(i).split(" ");
      String firstWord = words[0].toLowerCase();
      if (!"given".equals(firstWord) && !"when".equals(firstWord) && !"then".equals(firstWord) && !"and".equals(firstWord)) {
        throw new Exception("Scenario step must start with Given, When, Then, or And");
      }

      StringBuilder parsedStep = new StringBuilder();
      ArrayList<Object> params = new ArrayList<>();
      Boolean isParameter = false;
      StringBuilder param = new StringBuilder(); //temp variable to hold string parameter (that can include spaces)

      for (int j = 1; j < words.length; ++j) {

        if (words[j].startsWith("<") && words[j].endsWith(">")) { //param in example table
          params.add(words[j]);
        } else if (words[j].startsWith("\"") && words[j].endsWith("\"")) { //1-word string param
          params.add(words[j].substring(1, words[j].length() - 1));
        } else if (words[j].startsWith("\"")) { //start of multi-word string param
          param = new StringBuilder(words[j].substring(1));
          isParameter = true;
        } else if (words[j].endsWith("\"")) { //end of multi-word string param
          param.append(" ").append(words[j].substring(0, words[j].length() - 1));
          params.add(param.toString());
          isParameter = false;
          param = new StringBuilder();
        } else if (isParameter) { //middle of multi-word string param
          param.append(" ").append(words[j]);
        } else if (isNumeric(words[j])) { //int param
          params.add(Integer.parseInt(words[j]));
        } else { //not a param
          parsedStep.append(words[j].toLowerCase());
          parsedStep.append("_");
        }
      }

      AbstractMap.SimpleEntry<String, ArrayList<Object>> parsedStepMap = new AbstractMap.SimpleEntry<>(trimTrailingUnderscore(parsedStep.toString()), params); //<method_name, [parameters]>
      parsedSteps.add(parsedStepMap);

    }
    System.out.println(parsedSteps);
    return parsedSteps;
  }

  /**
   * Checks if a string contains only numeric values
   *
   * @param str input
   * @return boolean
   */
  private Boolean isNumeric(String str) {
    int startIdx = 0;
    if (str.charAt(0) == '-') {//detect negative numbers
      startIdx = 1;
    }
    for (int i = startIdx; i < str.length(); ++i) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * If a string ends with an underscore, remove it
   *
   * @param str input
   * @return string without a trailing underscore
   */
  private String trimTrailingUnderscore(String str) {
    if (str.endsWith("_")) {
      return str.substring(0, str.length() - 1);
    }
    return str;
  }

  /**
   * Returns the condition of the step that is non-empty
   *
   * @param step JStep annotation to analyze
   * @return
   */
  private String getStepString(JStep step) throws InvocationTargetException, IllegalAccessException {
    String str = "";
    for (Method m : step.annotationType().getDeclaredMethods()) {
      String cond = (String) m.invoke(step, null);
      if (!cond.equals("")) {
        str = cond;
      }
    }
    return str.toLowerCase();
  }

  /**
   * Reads all methods in step defs class, and store a map of dashed_method_name to parameter types into a HashMap
   *
   * @throws Exception
   */
  private void parseStepDefs() throws Exception {
    methods = new HashMap<>(); //<method_name, [parameters]>

    //loop through all declared methods in step defs class
    for (Method m : stepDefsClass.getMethods()) {
      JStep intfc = m.getAnnotation(JStep.class);
      if (intfc == null) {
        continue;
      }

      String cond = getStepString(intfc);
      String[] words = cond.split(" ");

      String parsedCond = "";
      ArrayList<String> params = new ArrayList<>();

      for (int i = 0; i < words.length; ++i) {
        if (words[i].startsWith("{") && words[i].endsWith("}")) {
          params.add(words[i].substring(1, words[i].length() - 1));
        } else {
          parsedCond += words[i] + "_";
        }
      }

      methods.put(trimTrailingUnderscore(parsedCond), params);
    }
  }

}
