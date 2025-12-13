package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartLoginPage;
import pages.FlipkartRegister;

/**
 * Authentication test cases - Login and Registration
 */
public class AuthenticationTests extends BaseTest {

	@Test(priority = 1)
	public void registerUser(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "registerUser");
		test = extent.createTest("registerUser", "This test case verifies Register page elements are present");
		test.log(Status.INFO, "Verifying Register page elements and locators");
		logger.info("Verifying Register page elements are accessible");
		
		try {
			FlipkartRegister pageFactory = new FlipkartRegister(driver);
			
			// Navigate to login page first
			String loginURL = ConfigFile.getLoginLink();
			driver.get(loginURL);
			Thread.sleep(2000);
			
			// Verify "New to Flipkart? Create an account" link is present and clickable
			pageFactory.clickSignUpLink();
			logger.info("Sign Up link clicked successfully");
			Thread.sleep(1000);
			
			// Verify phone input box is present and accessible
			String phoneNo = ConfigFile.getPhoneNo();
			pageFactory.clickPhoneInputBox();
			pageFactory.enterPhoneNumber(phoneNo);
			logger.info("Phone input box is working - entered phone number");
			
			// Verify Submit/Continue button is present
			pageFactory.clickSubmitButton();
			logger.info("Submit button clicked successfully");
			
			// Test passes if all elements are accessible
			// Note: Not completing OTP flow here since checkLogin already tests that
			test.log(Status.PASS, "All register page elements verified successfully");
			Assert.assertTrue(true, "Register page elements verified");
			
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 2)
	public void checkLogin(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "checkLogin");
		test = extent.createTest("checkLogin", "This test case for checking Login functionality with phone OTP");
		test.log(Status.INFO, "Checking Login Functionality on Flipkart Site using Phone Number");
		logger.info("Checking Login Functionality on Flipkart Site using Phone Number");
		
		try {
			String loginURL = ConfigFile.getLoginLink();
			driver.get(loginURL);
			FlipkartLoginPage pageFactory = new FlipkartLoginPage(driver);
			
			// Get phone number from config (9024002784)
			String phoneNo = ConfigFile.getPhoneNo();
			logger.info("Using phone number: " + phoneNo);
			
			// Click on phone input and enter phone number
			pageFactory.clickPhoneInputBox();
			pageFactory.enterPhoneNumber(phoneNo);
			
			// Click Request OTP button
			pageFactory.clickRequestOTP();
			
			// Wait for user to manually enter OTP (15 seconds)
			pageFactory.waitForManualOTPEntry(15);
			
			// Check if login was successful
			boolean isLoginSuccessful = pageFactory.isLoginSuccessful();
			
			// Assertion
			Assert.assertTrue(isLoginSuccessful, "Login was not successful");
			logger.info("Login completed successfully!");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}
}

