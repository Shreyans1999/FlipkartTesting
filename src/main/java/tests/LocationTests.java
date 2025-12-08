package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartChangePincode;
import utils.Utils;

/**
 * Location/Pincode related tests
 */
public class LocationTests extends BaseTest {

	@Test
	public void changingPinCode(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "changingPinCode");
		test = extent.createTest("changingPinCode", "This test case is for changing the pincode");
		test.log(Status.INFO, "This test case is for changing the pincode");
		logger.info("Changing the pincode");
		
		try {
			// Navigate to a product page first
			String productLink = ConfigFile.getProductLink();
			driver.get(productLink);
			
			// Handle new tab
			String originalTab = Utils.handleNewTab(driver);
			
			// Perform actions on the new tab
			FlipkartChangePincode pageFactory = new FlipkartChangePincode(driver);
			String pincode = ConfigFile.getPincode();
			pageFactory.enterPincode(pincode);
			
			// Assertion
			String expectedText = "Delivery";
			String actualText = pageFactory.getExpectedText();
			Assert.assertTrue(actualText.contains(expectedText), "Expected text not found: " + expectedText);
			
			// Switch back to previous tab
			driver.switchTo().window(originalTab);
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}
}
