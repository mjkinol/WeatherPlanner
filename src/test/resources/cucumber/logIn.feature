Feature: The User can only log in with a correct username and password
	Scenario: Log in success
		Given I am on the 'Vacation Planning' page
		When I click 'log-in'
		And I input 'username' with 'account1'
		And I input 'password' with 'pass123'
		And I click 'submit-log-in'
		And I wait 500 milliseconds
		Then I am logged in
	Scenario: Log in not exist
		Given I am on the 'Vacation Planning' page
		When I click 'log-in'
		And I input 'username' with 'WRONGUSERNAME'
		And I input 'password' with 'pass123'
		And I click 'submit-log-in'
		And I wait 500 milliseconds
		Then 'log-in-message' reads 'User does not exist.'
	Scenario: Wrong password
		Given I am on the 'Vacation Planning' page
		When I click 'log-in'
		And I input 'username' with 'account1'
		And I input 'password' with 'WRONGPASSWORD'
		And I click 'submit-log-in'
		And I wait 500 milliseconds
		Then 'log-in-message' reads 'Incorrect password!'