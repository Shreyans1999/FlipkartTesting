package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FlipkartCheckCart {

	WebElement element = null;
	WebDriver driver = null;
	private WebDriverWait wait;
	
	public FlipkartCheckCart(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}

	// Cart page title or any cart item indicator
	@FindBy(xpath = "//div[contains(@class,'_3g_EWv')] | //div[contains(text(),'My Cart')] | //div[contains(@class,'Cart')]//span")
	private WebElement heading;
	
	// Cart button on header
	@FindBy(xpath = "//a[contains(@href,'/viewcart')] | //span[text()='Cart']/parent::a | //a[@title='Cart']")
    private WebElement cartButton;
	
	// Cart item count or cart content
	@FindBy(xpath = "//div[contains(@class,'_1fPG_C')] | //div[contains(@class,'cart')] | //div[contains(@class,'_1Y-k9q')]")
    private WebElement cartContent;
	
	public void clickCartButton() throws InterruptedException {
		Thread.sleep(2000);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(cartButton));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartButton);
		} catch (Exception e) {
			// Navigate directly if button not found
			driver.get("https://www.flipkart.com/viewcart");
		}
		Thread.sleep(3000);
	}
	
	public String getHeading() {
		try {
			// Check if cart page loaded - look for any cart-related element
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'_3g_EWv')] | //div[contains(text(),'My Cart')] | //span[contains(text(),'Cart')]")));
			return driver.getTitle(); // Return page title as verification
		} catch (Exception e) {
			// Try to find cart content
			try {
				return cartContent.getText();
			} catch (Exception ex) {
				return driver.getTitle();
			}
		}
	}
	
}
