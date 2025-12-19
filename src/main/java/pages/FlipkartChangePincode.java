package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;


public class FlipkartChangePincode {
	
	WebElement element = null;
	WebDriver driver = null;
	private Wait<WebDriver> wait;
	
	public FlipkartChangePincode(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//div[contains(text(),'Delivery') or contains(text(),'Usually delivered')] | //span[contains(text(),'Delivery')]")
	public WebElement expectedText;
	
	
	@FindBy(xpath="//input[@id=\"pincodeInputId\"]")
	public WebElement pincodeBox;
	
	
	public void enterPincode(String pincode) throws InterruptedException {
		// Wait to see the page load
		Thread.sleep(2000);
		
		wait.until(ExpectedConditions.elementToBeClickable(pincodeBox));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", pincodeBox);
		
		// Wait to see the pincode box highlighted
		Thread.sleep(1500);
		
		// Clear any existing value and enter new pincode
		pincodeBox.clear();
		pincodeBox.sendKeys(pincode);
		
		// Wait to see the entered pincode
		Thread.sleep(1500);
		
		// Press Enter to submit
		pincodeBox.sendKeys(Keys.RETURN);
		
		// Wait to see the delivery result
		Thread.sleep(2000);
	}
	
	public String getExpectedText() {
		return expectedText.getText();
	}

}
