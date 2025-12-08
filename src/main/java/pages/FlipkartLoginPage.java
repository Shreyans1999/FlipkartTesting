package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import reports.ReadConfigFile;

public class FlipkartLoginPage {

	WebDriver driver = null;
	ReadConfigFile ConfigFile = new ReadConfigFile();
	private WebDriverWait wait;
	
	public FlipkartLoginPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="(//a[@title=\"Login\"])[1]")
	public WebElement loginBtn;
	
	// Locator for the phone/email input field in login form
	@FindBy(xpath="//input[contains(@class,'c3Bd2c') and contains(@class,'yXUQVt')]")
	public WebElement phoneInputBox;
	
	@FindBy(xpath="//button[contains(text(),'Request OTP') or contains(text(),'CONTINUE') or contains(text(),'Continue')]")
	public WebElement requestOTP_Btn;

	@FindBy(xpath="//input[@type='text' and contains(@class,'otp')] | //input[contains(@placeholder,'OTP')] | //input[@maxlength='6']")
	public WebElement OTP_Box;
	
	public void clickPhoneInputBox() throws InterruptedException {
		// Wait for the input field to be visible and clickable
		Thread.sleep(2000); // Extra wait for page stability
		WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//input[contains(@class,'c3Bd2c') and contains(@class,'yXUQVt')]")));
		wait.until(ExpectedConditions.elementToBeClickable(inputField));
		inputField.click();
	}
	
	public void enterPhoneNumber(String phoneNumber) throws InterruptedException {
		// Find and enter phone number with explicit wait
		WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//input[contains(@class,'c3Bd2c') and contains(@class,'yXUQVt')]")));
		inputField.clear();
		inputField.sendKeys(phoneNumber);
	}
	
	public void clickRequestOTP() {
		wait.until(ExpectedConditions.elementToBeClickable(requestOTP_Btn));
		requestOTP_Btn.click();
	}
	
	/**
	 * Wait for user to manually enter OTP received on phone.
	 * This method pauses execution for the specified duration.
	 * @param seconds Number of seconds to wait for manual OTP entry
	 */
	public void waitForManualOTPEntry(int seconds) throws InterruptedException {
		System.out.println("===========================================");
		System.out.println("WAITING FOR MANUAL OTP ENTRY...");
		System.out.println("Please enter the OTP received on your phone within " + seconds + " seconds");
		System.out.println("===========================================");
		Thread.sleep(seconds * 1000);
	}
	
	/**
	 * Check if login was successful by verifying user is on account page or homepage
	 */
	public boolean isLoginSuccessful() {
		try {
			Thread.sleep(2000); // Wait for page to load after OTP
			String currentUrl = driver.getCurrentUrl();
			// Login is successful if redirected away from login page
			return !currentUrl.contains("login");
		} catch (Exception e) {
			return false;
		}
	}
	
	public void clickLoginBtn() {
		loginBtn.click();
	}
}
