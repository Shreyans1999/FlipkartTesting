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
	public void clickOnProduct(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "clickOnProduct");
		test = extent.createTest("checkProductClickHome", "This test case is for clicking a product on Homepage");
		test.log(Status.INFO, "This test case is for clicking a product on Homepage");
		logger.info("Clicking on a product on Homepage");
		
		try {
			// Navigate to homepage first
			String URL = ConfigFile.getURL();
			driver.get(URL);
			
			FlipkartClickProductHome pageFactory = new FlipkartClickProductHome(driver);
			boolean isProductClicked = pageFactory.clickOnProduct();
			
			// Assertion
			Assert.assertTrue(isProductClicked, "Product was not clicked successfully");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void searchProduct(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "searchProduct");
		test = extent.createTest("searchProduct", "This test case is for searching a product on search-box");
		test.log(Status.INFO, "This test case is for searching a product on search-box");
		logger.info("Searching a product on Search-Box");
		
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
			
			// Assertion
			Assert.assertTrue(isProductClicked, "Product was not clicked successfully");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}
}
