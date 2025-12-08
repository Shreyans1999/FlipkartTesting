package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

/**
 * Site launch test - verifies Flipkart homepage loads correctly
 */
public class SiteLaunchTests extends BaseTest {

	@Test
	public void getFlipkartSite(ITestContext context) {
		context.setAttribute("testCaseName", "getFlipkartSite");
		test = extent.createTest("getFlipkartSite", "This test case for launching the site");
		test.log(Status.INFO, "Launching Flipkart Site");
		logger.info("Launching Flipkart Site");
		
		String URL = ConfigFile.getURL();
		driver.get(URL);
		
		// Assertion
		String currentURL = driver.getCurrentUrl();
		Assert.assertEquals(currentURL, URL, "URLs do not match");
	}
}
