Feature: Like button can be seen
  Scenario: Valid Inputs Can See Like Button
    Given I am on the 'Vacation Planning' page
		When I input 'temp-low' with '1'
		And I input 'temp-high' with '100'
		And I input 'location' with 'Atlanta'
		And I input 'radius' with '500'
		And I click 'submit'
		And I wait 2000 milliseconds
		Then The button 'add-like' exists
  Scenario: Can sort likes
    Given I am on the 'Vacation Planning' page
		When I input 'temp-low' with '1'
		And I input 'temp-high' with '100'
		And I input 'location' with 'Rochester'
		And I input 'radius' with '500'
		And I click 'submit'
		And I wait 2000 milliseconds
		And I click 'history'
		And I wait 2000 milliseconds
		Then The button 'add-like' exists
			