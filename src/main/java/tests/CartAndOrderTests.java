package tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartAddToCart;
import pages.FlipkartCheckCart;
import pages.FlipkartPlaceOrder;
import reports.ExtentManager;

/**
 * Cart and order related test cases
 * These tests require a logged-in session and share the same browser session
 */
public class CartAndOrderTests extends BaseTest {

	@Override
	@BeforeTest
	@Parameters("browser")
	public void initializeDriver(@Optional("chrome") String browser) {
		// Call parent's initializeDriver first
		super.initializeDriver(browser);
		
		// Then perform login since cart/order tests need a logged-in user
		try {
			logger.info("Cart/Order tests require login - performing login...");
			performLogin(15); // 15 seconds for manual OTP entry
		} catch (InterruptedException e) {
			logger.error("Login was interrupted: " + e.getMessage());
		}
	}

	@Test
	public void addToCart(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "addToCart");
		test = extent.createTest("addToCart", "This test case is to Add Product to cart");
		test.log(Status.INFO, "This test case is to Add Product to cart");
		logger.info("Adding a laptop product into cart");
		
		try {
			// Navigate to a product page first
			String productLink = ConfigFile.getProductLink();
			driver.get(productLink);
			
			// Wait to see the product page
			System.out.println(">> Product page loaded - waiting 5 seconds to view...");
			Thread.sleep(5000);
			
			FlipkartAddToCart pageFactory = new FlipkartAddToCart(driver);
			boolean isClicked = pageFactory.clickAddToCartButton();
			
			// Wait to see the cart page after adding product
			System.out.println(">> Product added to cart - waiting 5 seconds to view cart...");
			Thread.sleep(5000);
			
			// Assertion
			Assert.assertTrue(isClicked, "Button was not clicked");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void placeOrder(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "placeOrder");
		test = extent.createTest("placeOrder", "This test case is to place order");
		test.log(Status.INFO, "This test case is to place order");
		logger.info("Placing the order");
		
		try {
			// Navigate to product page
			String productLink = ConfigFile.getProductLink();
			driver.get(productLink);
			
			// Execute the complete place order flow
			FlipkartPlaceOrder pageFactory = new FlipkartPlaceOrder(driver);
			String confirmationEmail = "test@example.com";  // You can use any email
			boolean isSuccess = pageFactory.completePlaceOrderFlow(confirmationEmail);
			
			// Assertion
			Assert.assertTrue(isSuccess, "Place order flow failed!");
			test.log(Status.PASS, "Place order flow completed successfully");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void checkMyCart(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "checkMyCart");
		test = extent.createTest("checkMyCart", "This test case is to check my Cart");
		test.log(Status.INFO, "This test case is to check my Cart");
		logger.info("Checking my Flipkart Cart");
		
		try {
			// Navigate to homepage first
			String URL = ConfigFile.getURL();
			driver.get(URL);
			
			FlipkartCheckCart pageFactory = new FlipkartCheckCart(driver);
			pageFactory.clickCartButton();
			
			// Assertion - verify we're on the cart page
			String expectedText = "Cart";
			String actualText = pageFactory.getHeading();
			Assert.assertTrue(actualText.toLowerCase().contains(expectedText.toLowerCase()), 
				"Cart page not loaded. Got: " + actualText);
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Override @AfterMethod to NOT quit browser between dependent tests.
	 * Only log the test result, don't close the browser.
	 */
	@Override
	@AfterMethod
	public void tearDownTest(ITestResult result, ITestContext context) throws IOException {
		// Only log test result, don't quit browser
		getTestResult(result, context);
		ExtentManager.flushReport();
		System.out.println("Testing done successfully !!!");
		// Note: Browser is NOT closed here - it will be closed in @AfterTest
	}

	/**
	 * Quit browser after ALL cart tests complete
	 */
	@AfterTest
	public void tearDownBrowser() {
		if (driver != null) {
			driver.quit();
		}
	}
}

