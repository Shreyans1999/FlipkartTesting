package pages;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

public class FlipkartLoginPage {

	WebElement element = null;
	WebDriver driver = null;
	ReadConfigFile ConfigFile = new ReadConfigFile();
	String API_KEY = ConfigFile.getAPI_KEY();
    String serverID = ConfigFile.getServerID();
    String from = "noreply@ncb.flipkart.com";
    private WebDriverWait wait;
	
	public FlipkartLoginPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="(//a[@title=\"Login\"])[1]")
	public WebElement loginBtn;
	
	// Multiple fallback locators for the email/phone input field
	@FindBy(xpath="//input[contains(@class,'_18u87m')] | //input[contains(@class,'_2IX_2-')] | //form//input[@type='text']")
	public WebElement email_TextBox;
	
	@FindBy(xpath="//button[contains(text(),'Request OTP') or contains(text(),'CONTINUE') or contains(text(),'Continue')]")
	public WebElement requestOTP_Btn;

	@FindBy(xpath="//input[@type='text' and contains(@class,'otp')] | //input[contains(@placeholder,'OTP')] | //input[@maxlength='6']")
	public WebElement OTP_Box;
	
	public String getRandomEmail() {
		return "user" + System.currentTimeMillis() + "@";
	}
	
	public void clickEmailBox() throws InterruptedException {
		// Wait for the input field to be visible and clickable
		Thread.sleep(2000); // Extra wait for page stability
		WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//input[contains(@class,'_18u87m')] | //input[contains(@class,'_2IX_2-')] | //form//input[@type='text']")));
		wait.until(ExpectedConditions.elementToBeClickable(inputField));
		inputField.click();
	}
	
	public void enterLoginEmail(String emailId) throws InterruptedException {
		// Find and enter email with explicit wait
		WebElement inputField = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//input[contains(@class,'_18u87m')] | //input[contains(@class,'_2IX_2-')] | //form//input[@type='text']")));
		inputField.clear();
		inputField.sendKeys(emailId);
	}
	
	public void clickSubmitBox() {
		wait.until(ExpectedConditions.elementToBeClickable(requestOTP_Btn));
		requestOTP_Btn.click();
	}
	
	public boolean enterOTPBox(String OTP) {
		wait.until(ExpectedConditions.visibilityOf(OTP_Box));
		OTP_Box.sendKeys(OTP);
		return true;
	}
	
	public void clickLoginBtn() {
		loginBtn.click();
	}
	
//	Method to read OTP from Email Inbox
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
