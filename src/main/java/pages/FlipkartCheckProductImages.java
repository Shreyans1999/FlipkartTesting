package pages;

import java.util.List;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FlipkartCheckProductImages {

    WebElement element = null;
    WebDriver driver = null;
    private WebDriverWait wait;

    public FlipkartCheckProductImages(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Updated XPath to use current Flipkart class names
    // f67RGv is the thumbnail container class (may change, so we use multiple fallbacks)
    @FindBy(xpath="//ul[@class='f67RGv'] | //ul[.//li//img[contains(@src,'rukminim')]] | //div[contains(@class,'JNmmSe')]//parent::div//ul")
    public WebElement imageContainer;

    public boolean iterateOverImages() throws InterruptedException {
        // Wait for page to load
        Thread.sleep(2000);
        
        // Wait for the image container to be present
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//ul[@class='f67RGv'] | //ul[.//li//img[contains(@src,'rukminim')]] | //div[contains(@class,'JNmmSe')]//parent::div//ul")
        ));
        
        // Find all image elements in the container
        List<WebElement> imageElements = imageContainer.findElements(By.tagName("img"));
        
        // Iterate over each image element
        for (WebElement imageElement : imageElements) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", imageElement);
            Thread.sleep(500); // Wait to see the scroll
            imageElement.click();
            Thread.sleep(1000); // Wait to see the image change
        }
        return true;
    }
}
