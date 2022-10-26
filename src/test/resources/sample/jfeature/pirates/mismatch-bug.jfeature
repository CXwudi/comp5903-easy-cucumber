Feature: Single Player Scoring
  This feature tests and validates scoring for a single player

  @R37
  Scenario: Die with three skulls on first roll
    Given The game starts with 1 player
    And The player names are the following 'Player1'
    When 'Player1' gets 'GOLD' fortune card
    And 'Player1' rolls the following 'SKULL,SKULL,SKULL,SWORD,SWORD,SWORD,SWORD,SWORD'
    Then 'Player1' gets disqualified
    And Player scores are the following 'Player1 0'