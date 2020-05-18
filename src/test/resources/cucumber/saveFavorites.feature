Feature: User favorites are saved between sessions
	Scenario: Save between session
		Given I am on the 'Vacation Planning' page
		When I log in with 'account1' 'pass123'
		And I wait 500 milliseconds
		And I fill all vacation parameters correctly
		And I click 'submit'
		And I wait 1000 milliseconds
		And I click 'fav-add-Atlanta,US'
		And I enter a new session
		And I go to the 'Vacation Planning' page
		And I wait 2000 milliseconds
		And I log in with 'account1' 'pass123'
		And I wait 500 milliseconds
		And I fill all vacation parameters correctly
		And I click 'submit'
		And I wait 1000 milliseconds
		Then The element 'fav-remove-Atlanta,US' exists