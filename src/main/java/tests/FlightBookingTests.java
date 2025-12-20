package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartFlightPage;

/**
 * Flight booking test cases for Flipkart Travel section.
 */
public class FlightBookingTests extends BaseTest {

	/**
	 * Test case for normal flight search flow:
	 * 1. Navigate to Flipkart homepage
	 * 2. Click on Flight Bookings in navigation
	 * 3. Enter departure city (From)
	 * 4. Enter arrival city (To)
	 * 5. Select departure date (today)
	 * 6. Set number of travellers
	 * 7. Click Search button
	 * 8. Verify search results page loads
	 */
	@Test
	public void testFlightSearch(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "testFlightSearch");
		test = extent.createTest("testFlightSearch", "Test case to verify flight search functionality");
		test.log(Status.INFO, "Starting flight search test");
		logger.info("Starting flight search test");
		
		try {
			// Step 1: Navigate to Flipkart homepage
			String URL = ConfigFile.getURL();
			driver.get(URL);
			test.log(Status.INFO, "Navigated to Flipkart homepage: " + URL);
			logger.info("Navigated to Flipkart homepage");
			Thread.sleep(2000);
			
			// Initialize Page Object
			FlipkartFlightPage flightPage = new FlipkartFlightPage(driver);
			
			// Step 2: Click on Flight Bookings in navigation
			flightPage.clickFlightBookings();
			test.log(Status.INFO, "Clicked on Flight Bookings link");
			logger.info("Clicked on Flight Bookings link");
			
			// Verify flight booking page is displayed
			Assert.assertTrue(flightPage.isFlightPageDisplayed(), 
				"Flight booking page should be displayed");
			test.log(Status.PASS, "Flight booking page loaded successfully");
			
			// Step 3-6: Enter search details
			String departureCity = ConfigFile.getDepCity();
			String arrivalCity = ConfigFile.getArrCity();
			
			test.log(Status.INFO, "Searching flights from " + departureCity + " to " + arrivalCity);
			logger.info("Searching flights from " + departureCity + " to " + arrivalCity);
			
			// Perform flight search
			flightPage.searchFlights(departureCity, arrivalCity);
			test.log(Status.INFO, "Entered search details and clicked Search");
			
			// Step 8: Verify search results page loads
			String currentUrl = driver.getCurrentUrl();
			test.log(Status.INFO, "Current URL after search: " + currentUrl);
			
			// Assertion: URL should contain flight search parameters
			Assert.assertTrue(
				currentUrl.contains("source") || 
				currentUrl.contains("destination") || 
				currentUrl.contains("travel") ||
				flightPage.isFlightResultsDisplayed(),
				"Flight search results page should be displayed"
			);
			
			test.log(Status.PASS, "Flight search completed successfully");
			logger.info("Flight search test completed successfully");
			
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			logger.error("Flight search test failed: " + e.getMessage());
			throw e;
		}
	}
}
