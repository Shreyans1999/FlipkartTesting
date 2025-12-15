package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FlipkartAddToCart {

	WebDriver driver = null;
	private WebDriverWait wait;
	
	public FlipkartAddToCart(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}
	
	// Add to Cart button - find by text "Add to cart"
	@FindBy(xpath = "//button[text()='Add to cart']")
	public WebElement addToCartButton;

	public boolean clickAddToCartButton() throws InterruptedException {
		// Wait for page to load
		Thread.sleep(2000);
		
		try {
			// Maximize window and reset zoom to handle Eclipse's smaller window issue
			driver.manage().window().maximize();
			((JavascriptExecutor) driver).executeScript("document.body.style.zoom='100%'");
			
			// Scroll down to bring the Add to Cart button into view
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300)");
			Thread.sleep(500);
			
			// Close any login popup if present
			closeLoginPopupIfPresent();
			
			// First, try to find "Add to cart" button (product not in cart yet)
			String[] addToCartPatterns = {
				"//button[text()='Add to cart']",
				"//button[contains(text(),'Add to cart')]",
				"//button[contains(text(),'ADD TO CART')]"
			};
			
			// If product already in cart, it shows "Go to cart" instead
			String[] goToCartPatterns = {
				"//button[text()='Go to cart']",
				"//button[contains(text(),'Go to cart')]",
				"//button[contains(text(),'GO TO CART')]"
			};
			
			WebElement buttonToClick = null;
			boolean isGoToCart = false;
			
			// Try "Add to cart" first
			for (String xpath : addToCartPatterns) {
				try {
					buttonToClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
					System.out.println("Found 'Add to Cart' button - product is new to cart");
					break;
				} catch (Exception e) {
					// Try next pattern
				}
			}
			
			// If "Add to cart" not found, try "Go to cart" (product already in cart)
			if (buttonToClick == null) {
				for (String xpath : goToCartPatterns) {
					try {
						buttonToClick = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
						System.out.println("Found 'Go to Cart' button - product is already in cart!");
						isGoToCart = true;
						break;
					} catch (Exception e) {
						// Try next pattern
					}
				}
			}
			
			if (buttonToClick == null) {
				System.out.println("ERROR: Could not find Add to Cart or Go to Cart button!");
				return false;
			}
			
			// Scroll to the button and click
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", buttonToClick);
			Thread.sleep(500);
			
			// Try native click first, fallback to JavaScript
			String buttonName = isGoToCart ? "Go to Cart" : "Add to Cart";
			try {
				buttonToClick.click();
				System.out.println(buttonName + " button clicked!");
			} catch (Exception clickException) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", buttonToClick);
				System.out.println(buttonName + " button clicked using JavaScript!");
			}
			
			// Wait for cart action to complete
			Thread.sleep(1500);
			
			// Navigate to cart page if not already there
			String currentUrl = driver.getCurrentUrl();
			if (!currentUrl.contains("viewcart")) {
				driver.get("https://www.flipkart.com/viewcart?exploreMode=true&preference=FLIPKART");
			}
			
			// *** VISUAL WAIT - See the product in cart ***
			System.out.println(">> Product added! Viewing cart for 3 seconds...");
			Thread.sleep(3000);
			
			// Quick verification
			boolean cartHasItems = driver.findElements(By.xpath("//*[contains(text(),'Place Order') or contains(text(),'PLACE ORDER')]")).size() > 0;
			if (cartHasItems) {
				System.out.println("SUCCESS: Product is in cart!");
			} else {
				boolean isCartEmpty = driver.findElements(By.xpath("//*[contains(text(),'Your cart is empty')]")).size() > 0;
				if (isCartEmpty) {
					System.out.println("WARNING: Cart is empty!");
					return false;
				}
			}
			
			return true;
			
		} catch (Exception e) {
			System.out.println("Add to cart failed: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Close login popup if it appears and blocks the add to cart button
	 */
	private void closeLoginPopupIfPresent() {
		try {
			// Common popup close button patterns
			String[] closeButtonXpaths = {
				"//button[contains(@class,'_2KpZ6l _2doB4z')]",  // Close button on login popup
				"//button[@class='_2KpZ6l _2doB4z']",
				"//*[@class='_30XB9F']",  // X button
				"//span[text()='✕']/.."
			};
			
			for (String xpath : closeButtonXpaths) {
				try {
					WebElement closeBtn = driver.findElement(By.xpath(xpath));
					if (closeBtn.isDisplayed()) {
						closeBtn.click();
						System.out.println("Closed popup using: " + xpath);
						Thread.sleep(500);
						return;
					}
				} catch (Exception ignored) {}
			}
		} catch (Exception e) {
			// No popup to close - this is fine
		}
	}
	
}

