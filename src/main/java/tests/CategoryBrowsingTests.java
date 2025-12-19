package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartFurnitureProducts;
import pages.FlipkartGroceryPage;
import pages.FlipkartMenApparel;
import pages.FlipkartOfferZonePage;

/**
 * Category browsing test cases
 */
public class CategoryBrowsingTests extends BaseTest {

	@Test
	public void checkMenTshirts(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "checkMenTshirts");
		test = extent.createTest("checkMenTshirts", "This test case is to check Men T-Shirts products");
		test.log(Status.INFO, "This test case is to check Men T-Shirts products");
		logger.info("Checking Men T-Shirts products");
		
		try {
			// Navigate to homepage first
			String URL = ConfigFile.getURL();
			driver.get(URL);
			
			FlipkartMenApparel pageFactory = new FlipkartMenApparel(driver);
			pageFactory.navigateToTshirts();
			
			// Assertion
			String expectedText = "Men's T Shirts";
			String actualText = pageFactory.getHeading();
			Assert.assertTrue(actualText.contains(expectedText), "Expected text not found: " + expectedText);
			test.log(Status.PASS, "Successfully navigated to Men's T-Shirts page");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

//	@Test
//	public void checkFurniture(ITestContext context) throws InterruptedException {
//		context.setAttribute("testCaseName", "checkFurniture");
//		test = extent.createTest("checkFurniture", "This test case is to check furniture products");
//		test.log(Status.INFO, "This test case is to check furniture products");
//		logger.info("Checking Furniture products");
//		
//		try {
//			// Navigate to homepage first
//			String URL = ConfigFile.getURL();
//			driver.get(URL);
//			
//			FlipkartFurnitureProducts pageFactory = new FlipkartFurnitureProducts(driver);
//			pageFactory.checkBlankets();
//			
//			// Assertion
//			String expectedText = "Blankets";
//			String actualText = pageFactory.getHeading();
//			Assert.assertTrue(actualText.contains(expectedText), "Expected text not found: " + expectedText);
//		} catch (Exception e) {
//			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
//			throw e;
//		}
//	}
//
//	@Test
//	public void checkOfferZone(ITestContext context) throws InterruptedException {
//		context.setAttribute("testCaseName", "checkOfferZone");
//		test = extent.createTest("checkOfferZone", "This test case is to check Offer-Zone Section");
//		test.log(Status.INFO, "This test case is to check Offer-Zone Section");
//		logger.info("Checking Offer-Zone Products on Flipkart");
//		
//		try {
//			// Navigate to homepage first
//			String URL = ConfigFile.getURL();
//			driver.get(URL);
//			
//			FlipkartOfferZonePage pageFactory = new FlipkartOfferZonePage(driver);
//			pageFactory.clickOfferZone();
//			Thread.sleep(2000);
//			pageFactory.clickSportsWear();
//			
//			// Assertion
//			String expectedText = "Sports Casual Shoes Women's Footwear";
//			String actualText = pageFactory.getHeading();
//			Assert.assertTrue(actualText.contains(expectedText), "Expected text not found: " + expectedText);
//		} catch (Exception e) {
//			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
//			throw e;
//		}
//	}
//
//	@Test
//	public void checkGrocerySection(ITestContext context) throws InterruptedException {
//		context.setAttribute("testCaseName", "checkGrocerySection");
//		test = extent.createTest("checkGrocerySection", "This test case is to check Grocery Store of Flipkart");
//		test.log(Status.INFO, "This test case is to check Grocery Store of Flipkart");
//		logger.info("Checking Grocery Section of Flipkart");
//		
//		try {
//			FlipkartGroceryPage pageFactory = new FlipkartGroceryPage(driver);
//			String groceryLink = ConfigFile.getGroceryLink();
//			driver.get(groceryLink);
//			pageFactory.clickGrocery();
//			
//			// Assertion
//			String expectedText = "Vermicelli";
//			String actualText = pageFactory.getHeading();
//			Assert.assertTrue(actualText.contains(expectedText), "Expected text not found: " + expectedText);
//		} catch (Exception e) {
//			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
//			throw e;
//		}
//	}
}
