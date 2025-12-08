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

	@Test
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

	@Test(enabled = false) // Currently disabled - using checkLogin for phone-based auth
	public void registerUser(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "registerUser");
		test = extent.createTest("registerUser", "This test case is for Register functionality");
		test.log(Status.INFO, "This test case is for Register functionality");
		logger.info("Trying to sign up as a new user with phone number");
		
		try {
			FlipkartRegister pageFactory = new FlipkartRegister(driver);
			
			// Navigate to login page first
			String loginURL = ConfigFile.getLoginLink();
			driver.get(loginURL);
			Thread.sleep(2000);
			
			// Click on "New to Flipkart? Create an account" link
			pageFactory.clickSignUpLink();
			Thread.sleep(1000);
			
			// Enter phone number from config
			String phoneNo = ConfigFile.getPhoneNo();
			pageFactory.clickPhoneInputBox();
			pageFactory.enterPhoneNumber(phoneNo);
			
			// Click Continue/Request OTP
			pageFactory.clickSubmitButton();
			
			// Wait for user to manually enter OTP (15 seconds)
			pageFactory.waitForManualOTPEntry(15);
			
			// Click Signup button
			boolean isClicked = pageFactory.clickSignup();
			
			// Assertion
			Assert.assertTrue(isClicked, "Signup button was not clicked");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}
}
