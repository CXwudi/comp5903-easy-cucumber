package scs.comp5903.cucumber.sample;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.JStep;

import static scs.comp5903.cucumber.model.JStepKeyword.*;

public class RummikubDummyJStepDefs {
  private final static Logger log = org.slf4j.LoggerFactory.getLogger(RummikubDummyJStepDefs.class);

  @JStep(keyword = GIVEN, value = "Test Server is started")
  public void test_server_is_started() {
    log.debug("Given Test Server is started");
  }

  @JStep(keyword = GIVEN, value = "Player {int} hand starts with {string}")
  public void player_hand_starts_with(int pNum, String tiles) {
    log.debug("Given Player {} hand starts with {}", pNum, tiles);
  }

  @JStep(keyword = GIVEN, value = "Player {int} has played initial points")
  public void player_has_played_initial_points(int pNum) {
    log.debug("Given Player {} has played initial points", pNum);
  }

  @JStep(keyword = WHEN, value = "Player {int} draws {string}")
  public void player_draws(int pNum, String tile) {
    log.debug("When Player {} draws {}", pNum, tile);
  }

  @JStep(keyword = WHEN, value = "Player {int} chooses to draw")
  public void player_chooses_to_draw(int pNum) {
    log.debug("When Player {} chooses to draw", pNum);
  }

  @JStep(keyword = WHEN, value = "Player {int} has to draw")
  public void player_has_to_draw(int pNum) {
    log.debug("When Player {} has to draw", pNum);
  }

  @JStep(keyword = WHEN, value = "Player {int} plays {string}")
  public void player_plays(int pNum, String tiles) {
    log.debug("When Player {} plays {}", pNum, tiles);
  }

  @JStep(keyword = THEN, value = "table contains {string}")
  public void table_contains(String expected) {
    log.debug("Then table contains {}", expected);
  }

  @JStep(keyword = THEN, value = "Player {int} hand contains {string}")
  public void player_hand_contains(int pNum, String expected) {
    log.debug("Then Player {} hand contains {}", pNum, expected);
  }

  @JStep(keyword = THEN, value = "Player {int} wins")
  public void player_wins(int pNum) {
    log.debug("Then Player {} wins", pNum);
  }

  @JStep(keyword = THEN, value = "Player {int} score is {int}")
  public void player_score_is(int pNum, int score) {
    log.debug("Then Player {} score is {}", pNum, score);
  }

  // belows are same step defs but with and keywords, not all step defs below are used


  @JStep(keyword = AND, value = "Player {int} hand starts with {string}")
  public void and_player_hand_starts_with(int pNum, String tiles) {
    log.debug("And Player {} hand starts with {}", pNum, tiles);
  }

  @JStep(keyword = AND, value = "Player {int} has played initial points")
  public void and_player_has_played_initial_points(int pNum) {
    log.debug("And Player {} has played initial points", pNum);
  }

  @JStep(keyword = AND, value = "Player {int} draws {string}")
  public void and_player_draws(int pNum, String tile) {
    log.debug("And Player {} draws {}", pNum, tile);
  }

  @JStep(keyword = AND, value = "Player {int} chooses to draw")
  public void and_player_chooses_to_draw(int pNum) {
    log.debug("And Player {} chooses to draw", pNum);
  }

  @JStep(keyword = AND, value = "Player {int} has to draw")
  public void and_player_has_to_draw(int pNum) {
    log.debug("And Player {} has to draw", pNum);
  }

  @JStep(keyword = AND, value = "Player {int} plays {string}")
  public void and_player_plays(int pNum, String tiles) {
    log.debug("And Player {} plays {}", pNum, tiles);
  }

  @JStep(keyword = AND, value = "table contains {string}")
  public void and_table_contains(String expected) {
    log.debug("And table contains {}", expected);
  }

  @JStep(keyword = AND, value = "Player {int} hand contains {string}")
  public void and_player_hand_contains(int pNum, String expected) {
    log.debug("And Player {} hand contains {}", pNum, expected);
  }

  @JStep(keyword = AND, value = "Player {int} wins")
  public void and_player_wins(int pNum) {
    log.debug("And Player {} wins", pNum);
  }

  @JStep(keyword = AND, value = "Player {int} score is {int}")
  public void and_player_score_is(int pNum, int score) {
    log.debug("And Player {} score is {}", pNum, score);
  }
}

