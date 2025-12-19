package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FlipkartBeautyFoodPage {
	
	WebDriver driver = null;
	
	public FlipkartBeautyFoodPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//span[text()=\"Beauty, Food..\"]")
	private WebElement beautyFood;
	
	@FindBy(xpath = "//a[text()=\"Food & Drinks\"]")
    private WebElement foodAndDrinks;
	
	@FindBy(xpath="//h1[contains(text(),\"Chocolates\")]")
	private WebElement chocolatesHeading;
	
	public void navigateToChocolates() throws InterruptedException {
		Actions actions = new Actions(driver);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
		// Hover on Beauty, Food.. dropdown
        actions.moveToElement(beautyFood).perform();
        Thread.sleep(500);
        
        // Hover on Food & Drinks submenu
        actions.moveToElement(foodAndDrinks).perform();
        Thread.sleep(500);
        
        // Wait for and find the Chocolates link in the dropdown menu (not footer)
        // The nav menu link has 'otracker=categorytree' while footer has 'undefined_footer'
        WebElement chocolatesLink = wait.until(ExpectedConditions.elementToBeClickable(
        	By.xpath("//a[text()='Chocolates' and contains(@href, 'categorytree')]")
        ));
        
        // Click using JavaScript to bypass any overlay
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", chocolatesLink);
		Thread.sleep(2000);
	}
	
	public String getHeading() {
		return chocolatesHeading.getText();
	}
}

