package cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.cucumber.java.en.Then;

public class TableStepDefinitions {
	
	@Then("I see {int} results in the table {string}")
	public void i_see_results_in_the_table(int numResults, String tableName) {
		int numRows = StepDefinitions.driver.findElements(By.xpath("//table[@id='" + tableName + "']/tbody/tr")).size();
		assertEquals(numResults, numRows);
	}
	
	@Then("I see at most {int} results in the table {string}")
	public void i_see_at_most_n_results_in_the_table(int maxResults, String tableName) {
		int numRows = StepDefinitions.driver.findElements(By.xpath("//table[@id='" + tableName + "']/tbody/tr")).size();
		assertTrue(numRows <= maxResults);
	}
	
	@Then("I see the table {string} with headers {string}")
	public void i_see_the_table_with_headers(String tableName, String headerNames) {
		List<WebElement> headers = StepDefinitions.driver.findElements(By.xpath("//table[@id='" + tableName + "']/thead/th"));
		String[] expectedHeaders = headerNames.split("|");
		
		for(int i = 0; i < headers.size(); i++) {
			assertEquals(expectedHeaders[i], headers.get(i).getText());
		}
	}
}
