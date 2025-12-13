package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartClickProductHome;
import pages.FlipkartSearchBox;

/**
 * Product search and discovery test cases
 */
public class ProductSearchTests extends BaseTest {

	@Test
	public void clickProductOnHomepage(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "clickProductOnHomepage");
		test = extent.createTest("clickProductOnHomepage", "This test case is for clicking a product directly on Homepage");
		test.log(Status.INFO, "This test case is for clicking a product directly on Homepage");
		logger.info("Clicking on a product directly on Homepage");
		
		try {
			// Navigate to homepage first
			String URL = ConfigFile.getURL();
			driver.get(URL);
			
			FlipkartClickProductHome pageFactory = new FlipkartClickProductHome(driver);
			boolean isProductClicked = pageFactory.clickOnProduct();
			
			// Wait to visually confirm the product was clicked
			Thread.sleep(3000);
			
			// Assertion
			Assert.assertTrue(isProductClicked, "Product was not clicked successfully");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void searchAndClickProduct(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "searchAndClickProduct");
		test = extent.createTest("searchAndClickProduct", "This test case is for searching a product and clicking on it");
		test.log(Status.INFO, "This test case is for searching a product and clicking on it");
		logger.info("Searching a product and clicking on it");
		
		try {
			// Navigate to homepage first
			String URL = ConfigFile.getURL();
			driver.get(URL);
			
			FlipkartSearchBox pageFactory = new FlipkartSearchBox(driver);
			String product = ConfigFile.getProduct();
			Thread.sleep(2000);
			pageFactory.enterProductName(product);
			pageFactory.clickSearchButton();
			boolean isProductClicked = pageFactory.clickProduct();
			// Wait to visually confirm the product was clicked
			Thread.sleep(3000);
			// Assertion
			Assert.assertTrue(isProductClicked, "Product was not clicked successfully");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}
}
