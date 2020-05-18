Feature: The User can log in from any page
	Scenario: Logging in from main page
		Given I am on the 'Search' page
		When I click 'log-in'
		Then I see 'modal'
	Scenario: Logging in from vacation planning page
		Given I am on the 'Vacation Planning' page
		When I click 'log-in'
		Then I see 'modal'
	Scenario: Logging in from activity planning page
		Given I am on the 'Activity Planning' page
		When I click 'log-in'
		Then I see 'modal'
	Scenario: Logging in from analysis page
		Given I am on the 'Analysis' page
		When I click 'log-in'
		Then I see 'modal'