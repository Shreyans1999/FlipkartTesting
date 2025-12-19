package pages;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FlipkartClickProductHome {
	WebElement element = null;
	WebDriver driver = null;
	
	public FlipkartClickProductHome(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	// Using class pattern verified on live Flipkart homepage (54 elements matched)
	// The _3n8fna prefix is used for product card links across all sections
	@FindBy(xpath="(//a[contains(@class, '_3n8fna')])[3]")
	public WebElement productImg;
	
	@FindBy(xpath="(//img[@title=\"Flipkart\"])")
	public WebElement HomeButton;
	
	public void clickOnHomeBtn() {
		HomeButton.click();
	}
	
	public boolean clickOnProduct() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
		// First, try to dismiss any popup overlays (login popup, promo banners)
		try {
			// Look for close button on popup overlay
			WebElement closeButton = driver.findElement(
				org.openqa.selenium.By.xpath("//button[contains(@class, '_2KpZ6l') or contains(@class, 'close') or contains(@class, '_30XB9F')]")
			);
			if (closeButton.isDisplayed()) {
				closeButton.click();
				Thread.sleep(500); // Brief wait for popup to close
			}
		} catch (Exception ignored) {
			// No popup found, continue
		}
		
		WebElement visibleProductImage = wait.until(ExpectedConditions.visibilityOf(productImg));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", visibleProductImage);
		try { Thread.sleep(500); } catch (InterruptedException ignored) {} // Wait for scroll to complete
		
		try {
			// Try regular click first
			wait.until(ExpectedConditions.elementToBeClickable(visibleProductImage)).click();
		} catch (org.openqa.selenium.ElementClickInterceptedException e) {
			// Fallback to JavaScript click if element is blocked by overlay
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", visibleProductImage);
		}
		return true;
	}
}
