package utilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class Hooks {

	@BeforeSuite
	public static void setUp() throws Throwable {
		// BaseClass.setUpDriver(ConfigReader.getBrowserType());
		BaseClass.setUpDriver();
		// ExcelUtils.init();
	}

	@AfterSuite
	public static void tearDown() throws InterruptedException {

		// validate if scenario has failed
//        if(scenario.isFailed()) {
//            final byte[] screenshot = ((TakesScreenshot) BaseClass.getDriver()).getScreenshotAs(OutputType.BYTES);
//            scenario.attach(screenshot, "image/png", scenario.getName()); 
//        }   
		BaseClass.tearDown();
	}

}
