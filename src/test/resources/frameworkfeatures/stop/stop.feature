Feature: Stop the game
  This feature checks that the "Stop" button stops the game.

  Scenario: Press stop button
    Given the user has launched the JPacman GUI and has pressed the start button
    When the user presses the "Stop" button
    Then the game is not running anymore