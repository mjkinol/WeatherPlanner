package cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.cucumber.java.en.Then;

public class DomStepDefinitions {	
	@Then("The {string} has focus")
	public void the_name_has_focus(String name) {
		WebElement element = StepDefinitions.driver.findElement(By.id(name));
		assertEquals(element, StepDefinitions.driver.switchTo().activeElement());
	}
	@Then("The {string} {string} is {string}")
	public void the_name_attribute_is_value(String name, String attribute, String value) {
		WebElement element = StepDefinitions.driver.findElement(By.id(name));
		assertEquals(element.getAttribute(attribute), value);
	}
	
	@Then("I see {string}")
	public void i_see(String name) {
		WebElement element = StepDefinitions.driver.findElement(By.id(name));
		assertTrue(element.isDisplayed());
	}
}
