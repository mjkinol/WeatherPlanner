package cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import csci310.weatherplanner.db.Database;

/**
 * Run all the cucumber tests in the current package.
 */
@RunWith(Cucumber.class)
@CucumberOptions(strict = true)
public class RunCucumberTests {

	@BeforeClass
	public static void setup() {
		Database.resetDB();
		WebDriverManager.chromedriver().setup();
	}

}
