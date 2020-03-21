@S1 @framework
Feature: Start to play and stop
	As a player
	I want to start the game and stop it
	so that I can actually play and pause

	@S1.1
	Scenario: Press start button
		Given the user has launched the JPacman GUI
		When  the user presses the "Start" button
		Then  the game is running

	@S1.2
	Scenario: Press stop button
		Given the user has launched the JPacman GUI
		And the user presses the "Start" button
		When  the user presses the "Stop" button
		Then  the game is not running anymore
