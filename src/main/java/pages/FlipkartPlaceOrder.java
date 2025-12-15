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

/**
 * FlipkartPlaceOrder - Handles the complete checkout flow:
 * 1. Click Buy Now button on product page
 * 2. Click Change button for Delivery Address
 * 3. Click Edit button to edit address
 * 4. Modify address field
 * 5. Click Save and Deliver Here button
 * 6. Enter email for order confirmation
 * 7. Click Continue button
 */
public class FlipkartPlaceOrder {

	WebDriver driver = null;
	private WebDriverWait wait;
	
	public FlipkartPlaceOrder(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * Step 1: Click the Buy Now button on product page
	 */
	public void clickBuyNowButton() throws InterruptedException {
		System.out.println("Step 1: Clicking Buy Now button...");
		Thread.sleep(2000);
		
		// Maximize window and scroll to reveal button
		driver.manage().window().maximize();
		((JavascriptExecutor) driver).executeScript("document.body.style.zoom='100%'");
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300)");
		Thread.sleep(500);
		
		String[] buyNowXpaths = {
			"//button[text()='Buy Now']",
			"//button[contains(text(),'Buy Now')]",
			"//button[contains(text(),'BUY NOW')]"
		};
		
		WebElement buyNowButton = null;
		for (String xpath : buyNowXpaths) {
			try {
				buyNowButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				break;
			} catch (Exception e) {
				// Try next pattern
			}
		}
		
		if (buyNowButton == null) {
			throw new RuntimeException("Buy Now button not found!");
		}
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", buyNowButton);
		Thread.sleep(500);
		
		try {
			buyNowButton.click();
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", buyNowButton);
		}
		
		System.out.println("Buy Now button clicked! Waiting for checkout page...");
		Thread.sleep(3000);
		
		// Verify we're on checkout page
		String currentUrl = driver.getCurrentUrl();
		if (currentUrl.contains("checkout")) {
			System.out.println("Successfully navigated to checkout page: " + currentUrl);
		} else {
			System.out.println("Warning: Expected checkout page, but got: " + currentUrl);
		}
	}
	
	/**
	 * Step 2: Click the Change button next to Delivery Address
	 */
	public void clickChangeButton() throws InterruptedException {
		System.out.println("Step 2: Clicking Change button for Delivery Address...");
		Thread.sleep(1000);
		
		// Find the CHANGE button in the Delivery Address section
		String[] changeButtonXpaths = {
			"(//button[contains(text(),'CHANGE')])[2]",  // Second CHANGE button (for delivery address)
			"//div[contains(.,'DELIVERY ADDRESS')]//following::button[contains(text(),'CHANGE')][1]",
			"//button[contains(text(),'CHANGE')][2]"
		};
		
		WebElement changeButton = null;
		for (String xpath : changeButtonXpaths) {
			try {
				changeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				break;
			} catch (Exception e) {
				// Try next pattern
			}
		}
		
		if (changeButton == null) {
			// Try a more general approach - get all CHANGE buttons and click the second one
			try {
				java.util.List<WebElement> changeButtons = driver.findElements(By.xpath("//button[contains(text(),'CHANGE')]"));
				if (changeButtons.size() >= 2) {
					changeButton = changeButtons.get(1);  // Second button (index 1)
				} else if (changeButtons.size() == 1) {
					changeButton = changeButtons.get(0);  // Only one button
				}
			} catch (Exception e) {
				throw new RuntimeException("Change button not found!");
			}
		}
		
		if (changeButton != null) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", changeButton);
			System.out.println("Change button clicked!");
		}
		
		Thread.sleep(2000);
	}
	
	/**
	 * Step 3: Click the Edit button that appears after clicking Change
	 */
	public void clickEditButton() throws InterruptedException {
		System.out.println("Step 3: Clicking Edit button...");
		Thread.sleep(1000);
		
		String[] editButtonXpaths = {
			"//span[text()='EDIT']",
			"//button[contains(text(),'EDIT')]",
			"//*[text()='EDIT']",
			"//span[contains(text(),'EDIT')]"
		};
		
		WebElement editButton = null;
		for (String xpath : editButtonXpaths) {
			try {
				editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				break;
			} catch (Exception e) {
				// Try next pattern
			}
		}
		
		if (editButton == null) {
			throw new RuntimeException("Edit button not found!");
		}
		
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", editButton);
		System.out.println("Edit button clicked!");
		Thread.sleep(2000);
	}
	
	/**
	 * Step 4: Edit the Address field - add some random text
	 */
	public void editAddressField() throws InterruptedException {
		System.out.println("Step 4: Editing Address field...");
		Thread.sleep(1000);
		
		// Find the Address (Area and Street) field
		String[] addressFieldXpaths = {
			"//input[@name='addressLine1']",
			"//textarea[contains(@placeholder,'Address')]",
			"//input[contains(@placeholder,'Address')]",
			"//label[contains(text(),'Address')]/following::input[1]",
			"//label[contains(text(),'Address')]/following::textarea[1]",
			"(//div[contains(text(),'Address')]//following::textarea)[1]",
			"(//div[contains(text(),'Address')]//following::input)[1]"
		};
		
		WebElement addressField = null;
		for (String xpath : addressFieldXpaths) {
			try {
				addressField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				break;
			} catch (Exception e) {
				// Try next pattern
			}
		}
		
		if (addressField == null) {
			// Try to find any textarea on the page
			try {
				addressField = driver.findElement(By.xpath("//textarea"));
			} catch (Exception e) {
				System.out.println("Warning: Address field not found, skipping edit");
				return;
			}
		}
		
		// Clear and add new text
		String existingAddress = addressField.getAttribute("value");
		addressField.clear();
		addressField.sendKeys(existingAddress + ", ABC Colony");
		System.out.println("Address field updated with 'ABC Colony'");
		
		Thread.sleep(1000);
	}
	
	/**
	 * Step 5: Scroll down and click "Save and Deliver Here" button
	 */
	public void clickSaveAndDeliverButton() throws InterruptedException {
		System.out.println("Step 5: Clicking Save and Deliver Here button...");
		
		// Scroll down 500 pixels
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500)");
		Thread.sleep(1000);
		
		String[] saveButtonXpaths = {
			"//button[contains(text(),'SAVE AND DELIVER HERE')]",
			"//button[contains(text(),'Save and Deliver Here')]",
			"//button[contains(text(),'SAVE')]",
			"//button[contains(text(),'DELIVER HERE')]"
		};
		
		WebElement saveButton = null;
		for (String xpath : saveButtonXpaths) {
			try {
				saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				break;
			} catch (Exception e) {
				// Try next pattern
			}
		}
		
		if (saveButton == null) {
			throw new RuntimeException("Save and Deliver Here button not found!");
		}
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", saveButton);
		Thread.sleep(500);
		
		try {
			saveButton.click();
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
		}
		
		System.out.println("Save and Deliver Here button clicked!");
		Thread.sleep(2000);
	}
	
	/**
	 * Step 6: Enter email for order confirmation
	 */
	public void enterConfirmationEmail(String email) throws InterruptedException {
		System.out.println("Step 6: Entering order confirmation email...");
		Thread.sleep(1000);
		
		// Scroll down to see the email field
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300)");
		Thread.sleep(500);
		
		String[] emailFieldXpaths = {
			"//input[@placeholder='Enter your email ID']",
			"//input[contains(@placeholder,'email')]",
			"//input[@type='email']",
			"//input[contains(@class,'email')]"
		};
		
		WebElement emailField = null;
		for (String xpath : emailFieldXpaths) {
			try {
				emailField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				break;
			} catch (Exception e) {
				// Try next pattern
			}
		}
		
		if (emailField == null) {
			System.out.println("Warning: Email field not found, may already be filled");
			return;
		}
		
		emailField.clear();
		emailField.sendKeys(email);
		System.out.println("Email entered: " + email);
		
		Thread.sleep(1000);
	}
	
	/**
	 * Step 7: Click the Continue button
	 */
	public void clickContinueButton() throws InterruptedException {
		System.out.println("Step 7: Clicking Continue button...");
		Thread.sleep(1000);
		
		String[] continueButtonXpaths = {
			"//button[contains(text(),'CONTINUE')]",
			"//button[contains(text(),'Continue')]",
			"//span[contains(text(),'CONTINUE')]/parent::button",
			"//button[@type='submit']"
		};
		
		WebElement continueButton = null;
		for (String xpath : continueButtonXpaths) {
			try {
				continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				break;
			} catch (Exception e) {
				// Try next pattern
			}
		}
		
		if (continueButton == null) {
			throw new RuntimeException("Continue button not found!");
		}
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", continueButton);
		Thread.sleep(500);
		
		try {
			continueButton.click();
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);
		}
		
		System.out.println("Continue button clicked!");
		Thread.sleep(2000);
	}
	
	/**
	 * Execute the complete place order flow
	 */
	public boolean completePlaceOrderFlow(String email) throws InterruptedException {
		try {
			// Step 1: Click Buy Now
			clickBuyNowButton();
			
			// Step 2: Click Change button for delivery address
			clickChangeButton();
			
			// Step 3: Click Edit button
			clickEditButton();
			
			// Step 4: Edit address field
			editAddressField();
			
			// Step 5: Click Save and Deliver Here
			clickSaveAndDeliverButton();
			
			// Step 6: Enter confirmation email
			enterConfirmationEmail(email);
			
			// Step 7: Click Continue
			clickContinueButton();
			
			System.out.println("=== Place Order Flow Completed Successfully! ===");
			
			// Visual wait to see the result
			System.out.println(">> Waiting 5 seconds to view the result...");
			Thread.sleep(5000);
			
			return true;
			
		} catch (Exception e) {
			System.out.println("Place Order Flow Failed: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
