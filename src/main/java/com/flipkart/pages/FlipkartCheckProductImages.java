package com.flipkart.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FlipkartCheckProductImages extends BasePage {

    public FlipkartCheckProductImages(WebDriver driver) {
        super(driver);
    }

    // Updated XPath to use current Flipkart class names
    @FindBy(xpath="//ul[@class='f67RGv'] | //ul[.//li//img[contains(@src,'rukminim')]] | //div[contains(@class,'JNmmSe')]//parent::div//ul")
    public WebElement imageContainer;

    public boolean iterateOverImages() throws InterruptedException {
        // Wait for page to load
        pause(2000);
        
        // Wait for the image container to be present
        waitForPresence(
            By.xpath("//ul[@class='f67RGv'] | //ul[.//li//img[contains(@src,'rukminim')]] | //div[contains(@class,'JNmmSe')]//parent::div//ul")
        );
        
        // Find all image elements in the container
        List<WebElement> imageElements = imageContainer.findElements(By.tagName("img"));
        
        // Iterate over each image element
        for (WebElement imageElement : imageElements) {
            scrollIntoView(imageElement);
            pause(500); // Wait to see the scroll
            imageElement.click();
            pause(1000); // Wait to see the image change
        }
        return true;
    }
}
