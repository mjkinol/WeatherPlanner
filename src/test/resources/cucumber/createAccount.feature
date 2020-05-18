Feature: The User can only log in with a correct username and password
	Scenario: Create account success
		Given I am on the 'Vacation Planning' page
		When I click 'log-in'
		And I input 'username' with 'newAccount'
		And I input 'password' with 'pass123'
		And I click 'create'
		And I wait 500 milliseconds
		Then I am logged in
	Scenario: Account already exists
		Given I am on the 'Vacation Planning' page
		When I click 'log-in'
		And I input 'username' with 'account1'
		And I input 'password' with 'password123'
		And I click 'create'
		And I wait 500 milliseconds
		Then 'log-in-message' reads 'User already exists.'