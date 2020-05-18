Feature: The user can correctly see their search history
	Scenario: History table hidden before login
		Given I am on the 'Search' page
		Then 'miniTitle' is hidden
	Scenario: History title
		Given I am on the 'Search' page
		When I log in with 'account1' 'pass123'
		Then I input 'locationInput' with 'Rochester'
		Then I click 'mainSearchBtn'
		Then I go to the 'Search' page
		Then I see 'miniTitle'