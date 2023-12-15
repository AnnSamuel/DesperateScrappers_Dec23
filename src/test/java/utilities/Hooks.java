package utilities;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class Hooks {

	@BeforeSuite
	 @Parameters("headlessMode")
	public static void setUp(String headlessMode) throws Throwable {
		// BaseClass.setUpDriver(ConfigReader.getBrowserType());
		BaseClass.setUpDriver(headlessMode);
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
