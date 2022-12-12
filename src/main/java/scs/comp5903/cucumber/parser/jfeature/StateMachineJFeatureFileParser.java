package scs.comp5903.cucumber.parser.jfeature;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jfeature.JFeatureDetail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-07-05
 */
public class StateMachineJFeatureFileParser implements JFeatureFileParser {

  private static final Logger log = getLogger(StateMachineJFeatureFileParser.class);
  private final StateMachineJFeatureFileParserInternal lineByLineParser;

  public StateMachineJFeatureFileParser() {
    this.lineByLineParser = new StateMachineJFeatureFileParserInternal(new LineBasedDetailBuilder());
  }

  @Override
  public JFeatureDetail parse(Path featureFile) {
    log.info("Start parsing feature file: {}", featureFile.getFileName());
    // we have to new this instance to support multi-threading, as the jFeatureDetailBuilder is created once per thread
    lineByLineParser.initNewJFeatureDetailBuilder();
    try (var reader = Files.newBufferedReader(featureFile)) {
      for (var line = reader.readLine(); line != null; line = reader.readLine()) {
        // some easy filtering
        if (line.isBlank() || line.trim().startsWith("#")) {
          continue;
        }
        lineByLineParser.accept(line.trim());
      }
    } catch (IOException e) {
      throw new EasyCucumberException(ErrorCode.EZCU_DEPRECATED, "Failed to parse jfeature file " + featureFile + " due to IO exception", e);
    }
    var jFeatureDetail = lineByLineParser.buildJFeatureDetail();
    if (jFeatureDetail.getTitle() == null || jFeatureDetail.getTitle().isBlank()) {
      throw new EasyCucumberException(ErrorCode.EZCU_DEPRECATED, "A feature must have a valid title, or is there a syntax issue in your feature file" + featureFile + "?");
    }
    log.info("Done parsing feature file: {}", featureFile.getFileName());
    return jFeatureDetail;
  }
}

