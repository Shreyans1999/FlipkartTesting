package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import reports.ReadConfigFile;

/**
 * FlipkartRegister page - Simplified version for phone-based registration
 * Note: Currently registration flow is similar to login flow on Flipkart
 * The main checkLogin() test handles the login/registration via phone OTP
 */
public class FlipkartRegister {
	WebDriver driver = null;
	ReadConfigFile ConfigFile = new ReadConfigFile();
	private WebDriverWait wait;
	
	public FlipkartRegister(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//a[contains(text(),'Login')] | //span[text()='Login']")
	public WebElement loginLink;

	// Locator for phone/email input field
	@FindBy(xpath="//input[contains(@class,'r4vIwl') or contains(@class,'_2IX_2-')]")
	public WebElement phoneInputBox;
	
	@FindBy(xpath="//a[contains(text(),'New to Flipkart')] | //span[contains(text(),'New to Flipkart')]")
	public WebElement signUpLink;
	
	@FindBy(xpath="//button[contains(text(),'CONTINUE') or contains(text(),'Continue') or contains(text(),'Request OTP')]")
	public WebElement submitButton;
	
	@FindBy(xpath="//input[@type='text' and contains(@class,'otp')] | //input[contains(@placeholder,'OTP')] | //input[@maxlength='6']")
	public WebElement OTP_Box;
	
	@FindBy(xpath="//button[contains(text(),'Signup') or contains(text(),'SIGNUP') or contains(text(),'Sign Up')]")
	public WebElement signupButton;
	
	public void handleActionChain() throws InterruptedException {
	    Actions actions = new Actions(driver);
	    actions.moveToElement(loginLink).perform();
	    Thread.sleep(500);
	    signUpLink.click();
	}
	
	public void clickSignUpLink() {
		wait.until(ExpectedConditions.elementToBeClickable(signUpLink));
		signUpLink.click();
	}
	
	public void clickPhoneInputBox() throws InterruptedException {
		Thread.sleep(2000);
		WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//input[contains(@class,'r4vIwl') or contains(@class,'_2IX_2-')]")));
		wait.until(ExpectedConditions.elementToBeClickable(inputField));
		inputField.click();
	}
	
	public void enterPhoneNumber(String phoneNumber) throws InterruptedException {
		WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//input[contains(@class,'r4vIwl') or contains(@class,'_2IX_2-')]")));
		inputField.clear();
		inputField.sendKeys(phoneNumber);
	}
	
	public void clickSubmitButton() {
		wait.until(ExpectedConditions.elementToBeClickable(submitButton));
		submitButton.click();
	}
	
	/**
	 * Wait for user to manually enter OTP received on phone
	 */
	public void waitForManualOTPEntry(int seconds) throws InterruptedException {
		System.out.println("===========================================");
		System.out.println("WAITING FOR MANUAL OTP ENTRY...");
		System.out.println("Please enter the OTP received on your phone within " + seconds + " seconds");
		System.out.println("===========================================");
		Thread.sleep(seconds * 1000);
	}
	
	public boolean clickSignup() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(signupButton));
			signupButton.click();
			return true;
		} catch (Exception e) {
			System.out.println("Signup button not found or not clickable: " + e.getMessage());
			return false;
		}
	}
}

