package scs.comp5903.cucumber.parser.jfeature;

import scs.comp5903.cucumber.model.jfeature.JFeatureDetail;

import java.nio.file.Path;

/**
 * @author Charles Chen 101035684
 * @date 2022-12-09
 */
public interface JFeatureFileParser {
  JFeatureDetail parse(Path featureFile);
}
