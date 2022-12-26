package scs.comp5903.cucumber.integration.rummikub;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.step.JAndStep;
import scs.comp5903.cucumber.model.annotation.step.JGivenStep;
import scs.comp5903.cucumber.model.annotation.step.JThenStep;
import scs.comp5903.cucumber.model.annotation.step.JWhenStep;

public class RummikubDummyJStepDefsWithOverlappedAnnotation {
  private final static Logger log = org.slf4j.LoggerFactory.getLogger(RummikubDummyJStepDefsWithOverlappedAnnotation.class);

  @JGivenStep("Test Server is started")
  @JAndStep("Test Server is started")
  public void test_server_is_started() {
    log.debug("Given Test Server is started");
  }

  @JGivenStep("Player {int} hand starts with {string}")
  @JAndStep("Player {int} hand starts with {string}")
  public void player_hand_starts_with(int pNum, String tiles) {
    log.debug("Given Player {} hand starts with {}", pNum, tiles);
  }

  @JGivenStep("Player {int} has played initial points")
  @JAndStep("Player {int} has played initial points")
  public void player_has_played_initial_points(int pNum) {
    log.debug("Given Player {} has played initial points", pNum);
  }

  @JWhenStep("Player {int} draws {string}")
  @JAndStep("Player {int} draws {string}")
  public void player_draws(int pNum, String tile) {
    log.debug("When Player {} draws {}", pNum, tile);
  }

  @JWhenStep("Player {int} chooses to draw")
  @JAndStep("Player {int} chooses to draw")
  public void player_chooses_to_draw(int pNum) {
    log.debug("When Player {} chooses to draw", pNum);
  }

  @JWhenStep("Player {int} has to draw")
  @JAndStep("Player {int} has to draw")
  public void player_has_to_draw(int pNum) {
    log.debug("When Player {} has to draw", pNum);
  }

  @JWhenStep("Player {int} plays {string}")
  @JAndStep("Player {int} plays {string}")
  public void player_plays(int pNum, String tiles) {
    log.debug("When Player {} plays {}", pNum, tiles);
  }

  @JThenStep("table contains {string}")
  @JAndStep("table contains {string}")
  public void table_contains(String expected) {
    log.debug("Then table contains {}", expected);
  }

  @JThenStep("Player {int} hand contains {string}")
  @JAndStep("Player {int} hand contains {string}")
  public void player_hand_contains(int pNum, String expected) {
    log.debug("Then Player {} hand contains {}", pNum, expected);
  }

  @JThenStep("Player {int} wins")
  @JAndStep("Player {int} wins")
  public void player_wins(int pNum) {
    log.debug("Then Player {} wins", pNum);
  }

  @JThenStep("Player {int} score is {int}")
  @JAndStep("Player {int} score is {int}")
  public void player_score_is(int pNum, int score) {
    log.debug("Then Player {} score is {}", pNum, score);
  }
}

