package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartCheckProductImages;

/**
 * Product details/images related tests
 */
public class ProductDetailsTests extends BaseTest {

	@Test
	public void checkProductImages(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "checkProductImages");
		test = extent.createTest("checkProductImages", "This test case is for checking product images");
		test.log(Status.INFO, "This test case is for checking product images");
		logger.info("Iterating on product images");
		
		try {
			String productLink = ConfigFile.getProductLink();
			driver.get(productLink);
			FlipkartCheckProductImages pageFactory = new FlipkartCheckProductImages(driver);
			boolean isSeen = pageFactory.iterateOverImages();
			
			// Assertion
			Assert.assertTrue(isSeen, "Images were not seen");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}
}
