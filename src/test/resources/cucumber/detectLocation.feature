Feature: The User can use the detect location button to detect location
	Scenario: Location on vacation planning page
		Given I am on the 'Vacation Planning' page
		When I click 'auto-loc'
		And I wait 1000 milliseconds
		Then The 'location' 'value' is 'Roswell'
	Scenario: Location on activity planning page
		Given I am on the 'Activity Planning' page
		When I click 'auto-loc'
		And I wait 1000 milliseconds
		Then The 'location' 'value' is 'Roswell'
	Scenario: Location on from main page
		Given I am on the 'Search' page
		When I click 'auto-loc'
		And I wait 1000 milliseconds
		Then The 'location' 'value' is 'Roswell'