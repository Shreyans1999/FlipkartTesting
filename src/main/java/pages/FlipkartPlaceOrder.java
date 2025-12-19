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
	 * Step 4: Edit the Address field (Area and Street) - add some random text
	 * Note: This specifically targets the "Address (Area and Street)" field, 
	 * NOT the City/District/Town field
	 */
	public void editAddressField() throws InterruptedException {
		System.out.println("Step 4: Editing Address (Area and Street) field...");
		Thread.sleep(1000);
		
		// Find the Address (Area and Street) field specifically
		// Based on DOM: textarea with name="addressLine1", autocomplete="street-address"
		String[] addressFieldXpaths = {
			// Exact match using name attribute (from DOM inspection)
			"//textarea[@name='addressLine1']",
			// Match by autocomplete attribute
			"//textarea[@autocomplete='street-address']",
			// Match by Flipkart's specific classes
			"//textarea[contains(@class,'lWqG58')]",
			// Match by label for attribute
			"//label[contains(text(),'Address (Area and Street)')]/preceding-sibling::textarea",
			"//label[@for='addressLine1']/preceding-sibling::textarea",
			// Try finding textarea near the Address label
			"//label[contains(text(),'Address') and contains(text(),'Street')]/parent::div//textarea",
			"//div[contains(.,'Address (Area and Street)')]//textarea[@name='addressLine1']",
			// Fallback patterns
			"//textarea[@cols='10'][@tabindex='5']",
			"//textarea[contains(@class,'afiehA')]"
		};
		
		WebElement addressField = null;
		for (String xpath : addressFieldXpaths) {
			try {
				addressField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				if (addressField != null) {
					System.out.println("Found Address field with xpath: " + xpath);
					break;
				}
			} catch (Exception e) {
				// Try next pattern
			}
		}
		
		// Fallback: Find the correct field by checking the nearby label text
		if (addressField == null) {
			System.out.println("Primary selectors failed, trying to find Address field by structure...");
			try {
				// Find all form groups/divs that contain inputs
				java.util.List<WebElement> allInputs = driver.findElements(By.tagName("input"));
				java.util.List<WebElement> allTextareas = driver.findElements(By.tagName("textarea"));
				
				// Combine inputs and textareas
				java.util.List<WebElement> allFields = new java.util.ArrayList<>();
				allFields.addAll(allInputs);
				allFields.addAll(allTextareas);
				
				for (WebElement field : allFields) {
					try {
						// Check if this field's parent or ancestor contains "Address (Area and Street)" label
						WebElement parent = field.findElement(By.xpath("./ancestor::div[contains(.,'Address') and contains(.,'Area') and contains(.,'Street')][1]"));
						if (parent != null) {
							// Make sure it's not the City field
							String parentText = parent.getText();
							if (!parentText.contains("City") && !parentText.contains("Town") && !parentText.contains("District")) {
								addressField = field;
								System.out.println("Found Address (Area and Street) field via structure analysis");
								break;
							}
						}
					} catch (Exception e) {
						// This field doesn't have the right parent, continue
					}
				}
			} catch (Exception e) {
				System.out.println("Fallback approach failed: " + e.getMessage());
			}
		}
		
		// Last resort: Find textarea (Address fields are often textareas while City is an input)
		if (addressField == null) {
			try {
				// Look for the first textarea that's likely the address field
				java.util.List<WebElement> textareas = driver.findElements(By.tagName("textarea"));
				if (!textareas.isEmpty()) {
					addressField = textareas.get(0);
					System.out.println("Using first textarea as Address field");
				}
			} catch (Exception e) {
				System.out.println("Warning: Address field not found, skipping edit");
				return;
			}
		}
		
		if (addressField == null) {
			System.out.println("Warning: Address (Area and Street) field not found, skipping edit");
			return;
		}
		
		// Clear and add new text
		String existingAddress = addressField.getAttribute("value");
		if (existingAddress == null) {
			existingAddress = addressField.getText();
		}
		addressField.clear();
		String newAddress = existingAddress + ", ABC Colony";
		addressField.sendKeys(newAddress);
		System.out.println("Address (Area and Street) field updated: '" + existingAddress + "' -> '" + newAddress + "'");
		
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
		Thread.sleep(2000);
		
		// Scroll down to reveal the Continue button
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 400)");
		Thread.sleep(1000);
		
		String[] continueButtonXpaths = {
			"//button[contains(text(),'CONTINUE')]",
			"//button[contains(text(),'Continue')]",
			"//span[contains(text(),'CONTINUE')]/parent::button",
			"//span[text()='CONTINUE']/ancestor::button",
			"//span[contains(text(),'CONTINUE')]/ancestor::*[self::button or @role='button']",
			"//*[contains(@class,'continue') or contains(@class,'Continue')]//button",
			"//button[contains(@class,'continue') or contains(@class,'Continue')]",
			"//div[contains(text(),'CONTINUE')]/parent::button",
			"//button[@type='submit']",
			"//*[@role='button'][contains(.,'CONTINUE')]",
			"//button[.//span[contains(text(),'CONTINUE')]]"
		};
		
		WebElement continueButton = null;
		for (String xpath : continueButtonXpaths) {
			try {
				continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
				if (continueButton != null) {
					System.out.println("Found Continue button with xpath: " + xpath);
					break;
				}
			} catch (Exception e) {
				// Try next pattern
			}
		}
		
		// Fallback: Try to find any button with CONTINUE text
		if (continueButton == null) {
			System.out.println("Primary selectors failed, trying fallback approach...");
			try {
				java.util.List<WebElement> allButtons = driver.findElements(By.tagName("button"));
				for (WebElement btn : allButtons) {
					String btnText = btn.getText().toUpperCase();
					if (btnText.contains("CONTINUE")) {
						continueButton = btn;
						System.out.println("Found Continue button via fallback: " + btnText);
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("Fallback approach also failed: " + e.getMessage());
			}
		}
		
		// Second fallback: Try CSS selectors
		if (continueButton == null) {
			System.out.println("Trying CSS selector fallback...");
			String[] cssSelectors = {
				"button._2KpZ6l._2U9uOA._3v1-ww",
				"button._2KpZ6l._2U9uOA",
				"button[type='submit']",
				".continue-btn",
				"button._2KpZ6l"
			};
			
			for (String css : cssSelectors) {
				try {
					continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(css)));
					if (continueButton != null) {
						System.out.println("Found Continue button with CSS: " + css);
						break;
					}
				} catch (Exception e) {
					// Try next pattern
				}
			}
		}
		
		if (continueButton == null) {
			// Log page source to help debug
			System.out.println("Warning: Continue button not found. Current URL: " + driver.getCurrentUrl());
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
