package scs.comp5903.cucumber.parser;

import scs.comp5903.cucumber.model.JFeatureDetail;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-05
 */
public class JFeatureFileParser {
  private final DetailBuilder detailBuilder;

  public JFeatureFileParser(DetailBuilder detailBuilder) {
    this.detailBuilder = detailBuilder;
  }

  public JFeatureDetail parse(Path featureFile) {
    var jFeatureDetailBuilder = JFeatureDetail.builder();
    // we have to new this instance to support multi-threading, as the jFeatureDetailBuilder is created once per thread
    var lineByLineParser = new JFeatureFileLineByLineParser(jFeatureDetailBuilder, detailBuilder);
    try (var reader = Files.newBufferedReader(featureFile)) {
      for (var line = reader.readLine(); line != null; line = reader.readLine()) {
        // some easy filtering
        if (line.isBlank() || line.trim().startsWith("#")) {
          continue;
        }
        lineByLineParser.accept(line.trim());
      }
    } catch (IOException e) {
      throw new EasyCucumberException(ErrorCode.EZCU001, "Failed to parse jfeature file " + featureFile + " due to IO exception", e);
    }
    lineByLineParser.checkTempAndBuildScenarioOrScenarioOutline();
    var jFeatureDetail = jFeatureDetailBuilder.build();
    if (jFeatureDetail.getTitle() == null || jFeatureDetail.getTitle().isBlank()) {
      throw new EasyCucumberException(ErrorCode.EZCU014, "A feature must have a valid title, or is there a syntax issue in your feature file" + featureFile + "?");
    }
    return jFeatureDetail;
  }
}

