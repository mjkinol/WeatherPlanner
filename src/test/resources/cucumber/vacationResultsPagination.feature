Feature: Vacation Results Pagination
  Scenario: Valid Inputs Create Paginated Vacation Results
    Given I am on the 'Vacation Planning' page
		When I input 'temp-low' with '1'
		And I input 'temp-high' with '100'
		And I input 'location' with 'Atlanta'
		And I input 'radius' with '500'
		And I click 'submit'
		And I wait 2000 milliseconds
		Then The element 'pag_button1' exists
		
#Feature: Vacation Results Pagination Arrows
#  Scenario: Valid Inputs Create Pagination Arrows
#    Given I am on the 'Vacation Planning' page
#		When I input 'temp-low' with '1'
#		And I input 'temp-high' with '100'
#		And I input 'location' with 'Atlanta'
#		And I input 'radius' with '500'
#		And I click 'submit'
#		And I wait 2000 milliseconds
#		Then The element 'rightPagArrow' exists
