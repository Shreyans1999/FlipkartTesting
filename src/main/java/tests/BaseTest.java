package tests;

import java.io.IOException;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import io.github.bonigarcia.wdm.WebDriverManager;
import pages.FlipkartLoginPage;
import reports.ExtentManager;
import reports.ReadConfigFile;
import utils.Utils;

/**
 * Base test class containing common setup, teardown, and utility methods.
 * All test classes should extend this class.
 */
public class BaseTest {
	
	protected static final Logger logger = LogManager.getLogger(BaseTest.class);
	protected ReadConfigFile ConfigFile = new ReadConfigFile();
	protected WebDriver driver;
	protected ExtentReports extent;
	protected ExtentTest test;
	protected boolean isLoggedIn = false;

	@BeforeMethod
	@Parameters("browser")
	public void initializeDriver(@Optional("chrome") String browser) {
		switch (browser.toLowerCase()) {
			case "chrome":
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				break;
			case "firefox":
				FirefoxOptions options = new FirefoxOptions();
				driver = new FirefoxDriver(options);
				break;
			case "edge":
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				break;
			case "headless":
				WebDriverManager.chromedriver().setup();
				ChromeOptions headlessChromeOptions = new ChromeOptions();
				headlessChromeOptions.addArguments("--headless");
				driver = new ChromeDriver(headlessChromeOptions);
				break;
			default:
				System.out.println("Invalid browser specified!");
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		extent = ExtentManager.getInstance();
		extent.setSystemInfo("Browser", browser);
		extent.setSystemInfo("OS", "Windows 11");
		logger.info("\n\n------------New Logging Details Added Below--------------");
	}

	/**
	 * Perform login with phone number and manual OTP entry.
	 * Call this method in test classes that require a logged-in session.
	 * @param waitSeconds Seconds to wait for manual OTP entry
	 * @return true if login was successful
	 */
	protected boolean performLogin(int waitSeconds) throws InterruptedException {
		logger.info("Performing login with phone number...");
		
		String loginURL = ConfigFile.getLoginLink();
		driver.get(loginURL);
		FlipkartLoginPage loginPage = new FlipkartLoginPage(driver);
		
		String phoneNo = ConfigFile.getPhoneNo();
		logger.info("Using phone number: " + phoneNo);
		
		loginPage.clickPhoneInputBox();
		loginPage.enterPhoneNumber(phoneNo);
		loginPage.clickRequestOTP();
		
		// Wait for user to manually enter OTP
		loginPage.waitForManualOTPEntry(waitSeconds);
		
		// Check if login was successful
		isLoggedIn = loginPage.isLoginSuccessful();
		if (isLoggedIn) {
			logger.info("Login successful!");
		} else {
			logger.warn("Login may not have completed successfully");
		}
		return isLoggedIn;
	}
	
	/**
	 * Perform login with default 15 seconds wait time
	 */
	protected boolean performLogin() throws InterruptedException {
		return performLogin(15);
	}

	public void getTestResult(ITestResult result, ITestContext context) throws IOException {
		String testCaseName = (String) context.getAttribute("testCaseName");

		if (result.getStatus() == ITestResult.FAILURE) {
			String screenshotPath = Utils.takeScreenShot(driver, testCaseName);
			test.log(Status.FAIL, MarkupHelper.createLabel(testCaseName + " Failed", ExtentColor.RED));
			logger.error(testCaseName + " Failed");
			test.addScreenCaptureFromPath(screenshotPath);
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, MarkupHelper.createLabel(testCaseName + " Passed", ExtentColor.GREEN));
			logger.info(testCaseName + " Passed");
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, MarkupHelper.createLabel(testCaseName + " Skipped", ExtentColor.YELLOW));
			logger.warn(testCaseName + " Skipped");
		}
	}

	@AfterMethod
	public void tearDownTest(ITestResult result, ITestContext context) throws IOException {
		getTestResult(result, context);
		if (driver != null) {
			driver.quit();
		}
		ExtentManager.flushReport();
		System.out.println("Testing done successfully !!!");
	}
	
	/**
	 * Handle switching to a new browser tab
	 */
	protected String handleNewTab() {
		return Utils.handleNewTab(driver);
	}
}

