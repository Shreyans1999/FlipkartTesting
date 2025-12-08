package pages;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;

import reports.ReadConfigFile;

public class FlipkartRegister {
	WebElement element = null;
	WebDriver driver = null;
	ReadConfigFile ConfigFile = new ReadConfigFile();
	String API_KEY = ConfigFile.getAPI_KEY();
    String serverID = ConfigFile.getServerID();
    String from = "noreply@ncb.flipkart.com";
    private WebDriverWait wait;
	
	public FlipkartRegister(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//a[contains(text(),'Login')] | //span[text()='Login']")
	public WebElement loginLink;

	// Multiple fallback locators for the email/phone input field
	@FindBy(xpath="//input[contains(@class,'_18u87m')] | //input[contains(@class,'_2IX_2-')] | //form//input[@type='text']")
	public WebElement emailOrPhone_Box;
	
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
	
	public void clickEmailBox() throws InterruptedException {
		// Wait for the input field to be visible and clickable
		Thread.sleep(2000); // Extra wait for page stability
		WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//input[contains(@class,'_18u87m')] | //input[contains(@class,'_2IX_2-')] | //form//input[@type='text']")));
		wait.until(ExpectedConditions.elementToBeClickable(inputField));
		inputField.click();
	}
	
	public void enterEmail(String email) throws InterruptedException {
		// Find and enter email with explicit wait
		WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//input[contains(@class,'_18u87m')] | //input[contains(@class,'_2IX_2-')] | //form//input[@type='text']")));
		inputField.clear();
		inputField.sendKeys(email);
	}
	
	public void clickSubmitButton() {
		wait.until(ExpectedConditions.elementToBeClickable(submitButton));
		submitButton.click();
	}
	
	public void enterOTP(String otp) {
		wait.until(ExpectedConditions.visibilityOf(OTP_Box));
		OTP_Box.sendKeys(otp);
	}
	
	public boolean clickSignup() {
		wait.until(ExpectedConditions.elementToBeClickable(signupButton));
		signupButton.click();
		return true;
	}
	
	// Method to read OTP from Email Inbox using Mailosaur
	public String generateOTP() throws IOException, MailosaurException {
        String serverDomain = ConfigFile.getServerDomain();
        System.out.println("Fetching OTP from Mailosaur: " + serverDomain);
		MailosaurClient mailosaur = new MailosaurClient(API_KEY);
        
        MessageSearchParams params = new MessageSearchParams();
        params.withServer(serverID);
        
        SearchCriteria criteria = new SearchCriteria();
        String emailID = ConfigFile.getEmail();
        criteria.withSentTo(emailID);
        criteria.withSentFrom(from);
        
        Message message = mailosaur.messages().get(params, criteria);
        String subject = message.subject();
        System.out.println("Email Subject: " + subject);
        
        // Get OTP from subject - looking for 6 digit OTP
        Pattern pattern = Pattern.compile("([0-9]{6})");
        Matcher matcher = pattern.matcher(subject);
        
        if (matcher.find()) {
            String OTP = matcher.group(1);
            System.out.println("OTP Found: " + OTP);
            return OTP;
        }
        
        // If not in subject, try message body
        String body = message.text().body();
        matcher = pattern.matcher(body);
        if (matcher.find()) {
            String OTP = matcher.group(1);
            System.out.println("OTP Found in body: " + OTP);
            return OTP;
        }
        
        throw new RuntimeException("OTP not found in email");
	}
}
