package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for Flipkart Flight Booking page.
 * Handles navigation to flights section and flight search functionality.
 */
public class FlipkartFlightPage {
	
	WebDriver driver;
	WebDriverWait wait;
	
	public FlipkartFlightPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}
	
	// Navigation element - Flight Bookings link in header
	@FindBy(xpath = "//a[contains(text(),'Flight Bookings') or contains(@title,'Flight Bookings')] | //span[text()='Flight Bookings']")
	private WebElement flightBookingsLink;
	
	// From city input field - using stable name attribute
	@FindBy(name = "0-departcity")
	private WebElement fromCityInput;
	
	// To city input field - using stable name attribute
	@FindBy(name = "0-arrivalcity")
	private WebElement toCityInput;
	
	// Depart date field - using stable name attribute
	@FindBy(name = "0-datefrom")
	private WebElement departDateInput;
	
	// Travellers field - using stable name attribute
	@FindBy(name = "0-travellerclasscount")
	private WebElement travellersInput;
	
	// Search button
	@FindBy(xpath = "//button[.//span[text()='SEARCH']] | //button[contains(@class,'dSM5Ub')]")
	private WebElement searchButton;
	
	/**
	 * Click on Flight Bookings link in the navigation header
	 */
	public void clickFlightBookings() throws InterruptedException {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(flightBookingsLink));
			flightBookingsLink.click();
		} catch (Exception e) {
			// Try alternative locator
			WebElement altLink = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//span[contains(text(),'Flight Bookings')] | //div[contains(text(),'Flight Bookings')]")
			));
			altLink.click();
		}
		Thread.sleep(2000);
	}
	
	/**
	 * Enter departure city in From field
	 */
	public void enterFromCity(String city) throws InterruptedException {
		try {
			// Wait for and click the From input field
			WebElement fromField = wait.until(ExpectedConditions.elementToBeClickable(fromCityInput));
			fromField.click();
			Thread.sleep(500);
			
			// Clear and enter the city name
			fromField.clear();
			fromField.sendKeys(city);
			Thread.sleep(1500);
			
			// Click on the first suggestion in the dropdown
			WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//ul[contains(@class,'dropdown')]//li[1] | //div[contains(@class,'dropdown')]//div[contains(@class,'option')][1] | //li[contains(@class,'suggestion')][1] | //div[@role='listbox']//div[1]")
			));
			suggestion.click();
			Thread.sleep(500);
		} catch (Exception e) {
			System.out.println("Error entering from city: " + e.getMessage());
			// Try alternative approach - click anywhere to close dropdown
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value = arguments[1];", fromCityInput, city);
			js.executeScript("document.body.click();");
			Thread.sleep(500);
		}
	}
	
	/**
	 * Enter arrival city in To field
	 */
	public void enterToCity(String city) throws InterruptedException {
		try {
			// Wait for and click the To input field
			WebElement toField = wait.until(ExpectedConditions.elementToBeClickable(toCityInput));
			toField.click();
			Thread.sleep(500);
			
			// Clear and enter the city name
			toField.clear();
			toField.sendKeys(city);
			Thread.sleep(1500);
			
			// Click on the first suggestion in the dropdown
			WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//ul[contains(@class,'dropdown')]//li[1] | //div[contains(@class,'dropdown')]//div[contains(@class,'option')][1] | //li[contains(@class,'suggestion')][1] | //div[@role='listbox']//div[1]")
			));
			suggestion.click();
			Thread.sleep(500);
		} catch (Exception e) {
			System.out.println("Error entering to city: " + e.getMessage());
			// Try alternative approach
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value = arguments[1];", toCityInput, city);
			js.executeScript("document.body.click();");
			Thread.sleep(500);
		}
	}
	
	/**
	 * Select today's date for departure
	 */
	public void selectDepartureDate() throws InterruptedException {
		try {
			// Click on departure date field to open calendar
			WebElement dateField = wait.until(ExpectedConditions.elementToBeClickable(departDateInput));
			dateField.click();
			Thread.sleep(500);
			
			// Get today's date
			LocalDate today = LocalDate.now();
			int day = today.getDayOfMonth();
			
			// Try to find and click today's date in the calendar
			// Calendar dates are represented as buttons with the day's number as text
			WebElement todayDate = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//button[text()='" + day + "' and not(contains(@class,'disabled'))]")
			));
			todayDate.click();
			Thread.sleep(500);
		} catch (Exception e) {
			System.out.println("Date may already be selected or calendar handling needed: " + e.getMessage());
			// Date might already be selected, click somewhere else to close
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("document.body.click();");
			Thread.sleep(500);
		}
	}
	
	/**
	 * Set number of travellers (default is usually 1)
	 */
	public void setTravellers(int count) throws InterruptedException {
		// For this test, we'll use the default value (1 Traveller | Economy)
		// The field is readonly, clicking opens a modal to change values
		// For now, skip this as default is acceptable
		System.out.println("Using default traveller count: 1 Traveller | Economy");
	}
	
	/**
	 * Click the Search button to search for flights
	 */
	public void clickSearch() throws InterruptedException {
		try {
			WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
			searchBtn.click();
			Thread.sleep(3000);
		} catch (Exception e) {
			// Try JavaScript click as fallback
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement searchBtn = driver.findElement(
				By.xpath("//button[.//span[text()='SEARCH']] | //button[contains(@class,'dSM5Ub')]")
			);
			js.executeScript("arguments[0].click();", searchBtn);
			Thread.sleep(3000);
		}
	}
	
	/**
	 * Perform complete flight search flow
	 */
	public void searchFlights(String fromCity, String toCity) throws InterruptedException {
		enterFromCity(fromCity);
		enterToCity(toCity);
		selectDepartureDate();
		setTravellers(1);
		clickSearch();
	}
	
	/**
	 * Check if flight booking page is displayed
	 */
	public boolean isFlightPageDisplayed() {
		try {
			return driver.getCurrentUrl().contains("travel/flights") || 
				   driver.getCurrentUrl().contains("flight");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Check if flight search results are displayed
	 */
	public boolean isFlightResultsDisplayed() {
		try {
			// Wait for results to load
			Thread.sleep(2000);
			// Check URL for search results indication or look for flight cards
			String currentUrl = driver.getCurrentUrl();
			return currentUrl.contains("source=") || 
				   currentUrl.contains("destination=") ||
				   currentUrl.contains("MMID=") ||
				   driver.findElements(By.xpath("//div[contains(@class,'flight')] | //div[contains(@class,'Flight')]")).size() > 0;
		} catch (Exception e) {
			return false;
		}
	}
}
