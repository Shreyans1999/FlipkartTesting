package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartAddToCart;
import pages.FlipkartCheckCart;
import pages.FlipkartPlaceOrder;

/**
 * Cart and order related test cases
 * These tests require a logged-in session
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
		logger.info("Adding a mobile product into cart");
		
		try {
			// Navigate to a product page first
			String productLink = ConfigFile.getProductLink();
			driver.get(productLink);
			
			FlipkartAddToCart pageFactory = new FlipkartAddToCart(driver);
			boolean isClicked = pageFactory.clickAddToCartButton();
			
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
			// Navigate to product and add to cart first
			String productLink = ConfigFile.getProductLink();
			driver.get(productLink);
			
			FlipkartPlaceOrder pageFactory = new FlipkartPlaceOrder(driver);
			pageFactory.clickOrderButton();
			boolean isClicked = pageFactory.enterEmail();
			Thread.sleep(1000);
			
			// Assertion
			Assert.assertTrue(isClicked, "Button was not clicked");
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
			
			// Assertion
			String expectedText = "New Delhi - 110010";
			String actualText = pageFactory.getHeading();
			Assert.assertTrue(actualText.contains(expectedText), "Expected text not found: " + expectedText);
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}
}

