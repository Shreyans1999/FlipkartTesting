package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


public class FlipkartBrandFilters {
	
	WebElement element = null;
	WebDriver driver = null;
	private Wait<WebDriver> wait;
	
	public FlipkartBrandFilters(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}
	
	// Brand section header to scroll to first
	@FindBy(xpath = "//div[text()='BRAND' or text()='Brand']")
    private WebElement brandSection;
	
	// Google brand text div - simpler XPath that just targets the text
	@FindBy(xpath = "//div[text()='Google']")
    private WebElement googleBrand;
	
	// First product in the product list - use the product link anchor tag
	@FindBy(xpath = "(//a[@class='k7wcnx'])[1]")
    private WebElement firstProduct;

   
    public boolean brandFilter() throws InterruptedException {
    	// First scroll to the Brand section to ensure it's visible
    	Thread.sleep(2000); // Wait for page to fully load
    	
    	try {
    		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='BRAND' or text()='Brand']")));
    		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", brandSection);
    		Thread.sleep(1000);
    	} catch (Exception e) {
    		// Brand section might already be visible, continue
    	}
    	
    	// Now find and click Google brand
    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Google']")));
    	wait.until(ExpectedConditions.elementToBeClickable(googleBrand));
        
        // Scroll element to center of viewport to avoid sticky header
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", googleBrand);
        Thread.sleep(1000); // Wait for scroll to complete
        
        try {
            googleBrand.click();
        } catch (Exception e) {
            // Fallback to JavaScript click if regular click is intercepted
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", googleBrand);
        }
        
        // Wait for filter to be applied and products to load
        Thread.sleep(5000);
        
        // Scroll up to see products after filter is applied
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        Thread.sleep(2000);
        
        // Click on the first Google phone in the list - find by product link class
        String productXpath = "(//a[@class='k7wcnx'])[1]";
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(productXpath)));
        wait.until(ExpectedConditions.elementToBeClickable(firstProduct));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstProduct);
        Thread.sleep(1000);
        
        try {
            firstProduct.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstProduct);
        }
        
        // Wait to see the product page
        Thread.sleep(3000);
        
        return true;
     }

}
