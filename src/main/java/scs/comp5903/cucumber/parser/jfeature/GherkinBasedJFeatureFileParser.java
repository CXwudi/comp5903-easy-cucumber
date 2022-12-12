package scs.comp5903.cucumber.parser.jfeature;

import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.types.Envelope;
import io.cucumber.messages.types.GherkinDocument;
import io.cucumber.messages.types.ParseError;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;
import scs.comp5903.cucumber.model.jfeature.JFeatureDetail;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Charles Chen 101035684
 * @date 2022-12-11
 */
public class GherkinBasedJFeatureFileParser implements JFeatureFileParser {

  private static final GherkinParser gherkinParser = GherkinParser.builder()
      .includeGherkinDocument(true)
      .build();

  private final GherkinDocument2DetailMapper mapper = new GherkinDocument2DetailMapper();

  @Override
  public JFeatureDetail parse(Path featureFile) {
    GherkinDocument gherkinDocument = readGherkinDocument(featureFile);
    return mapToJFeatureDetail(gherkinDocument);
  }

  private GherkinDocument readGherkinDocument(Path featureFile) {
    List<Envelope> envelopeList;
    try {
      envelopeList = gherkinParser.parse(featureFile).collect(Collectors.toList());
    } catch (IOException e) {
      throw new EasyCucumberException(ErrorCode.EZCU001, "Failed to parse feature file " + featureFile + " due to IOException", e);
    }
    List<ParseError> errors = new ArrayList<>(envelopeList.size());
    GherkinDocument gherkinDocument = null;
    for (var envelope : envelopeList) {
      if (envelope.getGherkinDocument().isPresent()) {
        gherkinDocument = envelope.getGherkinDocument().orElseThrow();
      } else if (envelope.getParseError().isPresent()) {
        errors.add(envelope.getParseError().orElseThrow());
      }
    }
    if (!errors.isEmpty()) {
      throw new EasyCucumberException(ErrorCode.EZCU011, "Failed to parse feature file " + featureFile + " due to parse errors: " + errors);
    }
    return Objects.requireNonNull(gherkinDocument, "Gherkin document shouldn't be null here");
  }

  private JFeatureDetail mapToJFeatureDetail(GherkinDocument gherkinDocument) {
    var jfeatureDetailBuilder = JFeatureDetail.builder();
    var feature = gherkinDocument.getFeature().orElseThrow(() -> new EasyCucumberException(ErrorCode.EZCU014, "A feature must have a valid title"));
    jfeatureDetailBuilder.title(feature.getName());
    var tagsLiteral = mapper.toTagsLiteral(feature.getTags());
    jfeatureDetailBuilder.tags(tagsLiteral);
    var order = 0;
    for (var child : feature.getChildren()) {
      var scenario = child.getScenario().orElseThrow(() -> new EasyCucumberException(ErrorCode.EZCU015, "A children without a scenario or scenario outline is not supported"));

      if (scenario.getExamples().isEmpty()) {
        var jScenarioDetail = mapper.mapScenarioToDetail(scenario);
        jfeatureDetailBuilder.addScenario(jScenarioDetail);
        jfeatureDetailBuilder.addScenarioOrder(order++);
      } else {
        var jScenarioOutlineDetail = mapper.mapScenarioOutlineToDetail(scenario);
        jfeatureDetailBuilder.addScenarioOutline(jScenarioOutlineDetail);
        jfeatureDetailBuilder.addScenarioOutlineOrder(order++);
      }
    }
    return jfeatureDetailBuilder.build();
  }

}
