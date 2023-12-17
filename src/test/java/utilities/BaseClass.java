package utilities;

import java.time.Duration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseClass {
	public static WebDriver driver;
	static String url = "https://www.tarladalal.com";

	public static void setUpDriver(String headlessMode) {
		System.out.println("check headless value: " + headlessMode);

		// Enable headless mode based on the parameter value
		ChromeOptions options = new ChromeOptions();
		if ((Boolean.parseBoolean(headlessMode) == true)) {
			options.addArguments("--headless");
		}


		driver = new ChromeDriver(options);// initialize chrome driver

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));// implicit wait
		driver.manage().window().maximize();// maximize the window
	}

	public static void openPage() {
		driver.get(url);
	}

	public static void openSpecificPage(String subPage) {
		driver.get(url + subPage);
	}

	public static String title() {
		return driver.getTitle();
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static Document getJsoupDocument() {
		String htmlSource = driver.getPageSource();
		Document document = Jsoup.parse(htmlSource);
		return document;
	}

	public static void refresh() {
		driver.navigate().refresh();
	}

	public static void acceptAlert() throws InterruptedException {
		try {
			Thread.sleep(1000);
			driver.switchTo().alert().accept();
		} catch (NoAlertPresentException e) {
			e.printStackTrace();
		}
	}

	public static void back() throws InterruptedException {
		driver.navigate().back();
	}

	public static void tearDown() throws InterruptedException {

		if (driver != null) {
			driver.close();
			driver.quit();
			driver = null; // ? is this right?
		}

	}
}
