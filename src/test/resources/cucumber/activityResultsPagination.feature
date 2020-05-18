Feature: Activity Results Pagination
  Scenario: Valid Inputs Create Paginated Activity Results
    Given I am on the 'Activity Planning' page
		When I input 'activity' with 'hiking'
		And I input 'location' with 'Atlanta'
		And I input 'radius' with '500'
		And I click 'submit'
		And I wait 2000 milliseconds
		Then The element 'pag_button1' exists
		
#Feature: Activity Results Pagination Arrows
#  Scenario: Valid Inputs Create Pagination Arrows
#    Given I am on the 'Activity Planning' page
#		When I input 'activity' with 'hiking'
#		And I input 'location' with 'Atlanta'
#		And I input 'radius' with '500'
#		And I click 'submit'
#		And I wait 2000 milliseconds
#		Then The element 'rightPagArrow' exists