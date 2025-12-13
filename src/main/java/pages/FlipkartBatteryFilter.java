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


public class FlipkartBatteryFilter {

	private WebDriver driver;
	private Wait<WebDriver> wait;

    @FindBy(xpath = "//div[text()='3000 - 3999 mAh']/preceding-sibling::div")
    private WebElement batteryCapacityCheckbox;
    
    @FindBy(xpath="//div[text()='Battery Capacity']/..")
    private WebElement batteryCapacity;

    
    public FlipkartBatteryFilter(WebDriver driver) {
        this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean selectBatteryCapacity() throws InterruptedException {
    	wait.until(ExpectedConditions.elementToBeClickable(batteryCapacity));
        // Scroll element to center of viewport to avoid sticky header
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", batteryCapacity);
        Thread.sleep(500); // Wait for scroll to complete
        try {
            batteryCapacity.click();
        } catch (Exception e) {
            // Fallback to JavaScript click if regular click is intercepted
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", batteryCapacity);
        }
        wait.until(ExpectedConditions.elementToBeClickable(batteryCapacityCheckbox));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", batteryCapacityCheckbox);
        Thread.sleep(500); // Wait for scroll to complete
        try {
            batteryCapacityCheckbox.click();
        } catch (Exception e) {
            // Fallback to JavaScript click if regular click is intercepted
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", batteryCapacityCheckbox);
        }
        return true;
    }
}
