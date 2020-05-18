package cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UserInputStepDefinitions {	
	public UserInputStepDefinitions() {
	}
	
	@When("I input {string} with {string}")
	public void i_input(String input, String value) {
		WebElement element = StepDefinitions.driver.findElement(By.id(input));
		element.clear();
		element.sendKeys(value);
	}
	
	@When("I fill all vacation parameters correctly")
	public void i_fill_all_vacation_parameters_correctly() {
	    i_input("temp-low", "50");
	    i_input("temp-high", "80");
	    //i_input("precip-type", "Clear");
	    i_input("location", "Atlanta");
	    i_input("radius", "500");
	}
	
	@When("I click {string}")
	public void i_click_element(String element) {
		StepDefinitions.driver.findElement(By.id(element)).click();
	}
	
	@When("I am at {string}, {string}")
	public void i_am_at_lat_long(String latitude, String longitude) {
		System.out.println("SETTING LOCATION");
		JavascriptExecutor js = (JavascriptExecutor) StepDefinitions.driver;
		js.executeScript("window.navigator.geolocation.getCurrentPosition=function(success){"
				+ "var position = {\"coords\" : {\"latitude\": \"\" + arguments[0],\"longitude\": \"999\"}};" 
				+ "success(position);};", latitude, longitude);
		System.out.println(js.executeScript("var positionStr=\"\";"+
                "window.navigator.geolocation.getCurrentPosition(function(pos){positionStr=pos.coords.latitude+\":\"+pos.coords.longitude});"+
                "return positionStr;"));
		
	}
	
	@Then("The element {string} exists")
	public void the_element_name_exists(String name) {
		assertNotNull(StepDefinitions.driver.findElement(By.id(name)));
	}
	
	@Then("The button {string} exists")
	public void the_button_name_exists(String name) {
		assertNotNull(StepDefinitions.driver.findElement(By.className(name)));
	}
	
	@Then("{string} reads {string}")
	public void element_reads(String element, String expected) {
		String actualMessage = StepDefinitions.driver.findElement(By.id(element)).getText();
		assertEquals(expected, actualMessage);
	}
	
	@Then("I see error {string}")
	public void i_see_error(String message) {
		String actualMessage = StepDefinitions.driver.findElement(By.id("error-message")).getText();
		assertEquals(message, actualMessage);
	}
	
	@Then("I see error for {string}")
	public void i_see_error_for(String input) {
		Map<String, String> inputName = new HashMap<String, String>();
		inputName.put("temp-low", "Temperature Low");
		inputName.put("temp-high", "Temperature High");
		inputName.put("num-results", "Num Results");
		inputName.put("location", "Location");
		
		String expectedMessage = "Illegal value for input " + inputName.get(input);
		
		i_see_error(expectedMessage);
		
		assertEquals("rgb(0, 0, 0) rgb(222, 226, 230) rgb(0, 0, 0) rgb(0, 0, 0)", 
				StepDefinitions.driver.findElement(By.id("input")).getCssValue("border-color"));
	}
	
	@Then("I see no error")
	public void i_see_no_error() {
		String expectedMessage = "";
		String actualMessage = StepDefinitions.driver.findElement(By.id("error-message")).getText();
		
		assertEquals(expectedMessage, actualMessage);
	}
	
	@Then("I log in with {string} {string}")
	public void log_in(String username, String password) {
		i_click_element("log-in");
		i_input("username", username);
		i_input("password", password);
		i_click_element("submit-log-in");
	}
	
	@Then("I am logged in")
	public void i_am_logged_in() {
		
	}
}
