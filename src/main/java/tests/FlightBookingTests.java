package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartFlightPage;

/**
 * Flight booking related tests
 */
public class FlightBookingTests extends BaseTest {

	@Test
	public void checkFlights(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "checkFlights");
		test = extent.createTest("checkFlights", "This test case is to check Flight Booking Section");
		test.log(Status.INFO, "This test case is to check Flight Booking Section");
		logger.info("Checking Flight section on Flipkart");
		
		try {
			String URL = ConfigFile.getFlightLink();
			driver.get(URL);
			FlipkartFlightPage pageFactory = new FlipkartFlightPage(driver);
			Thread.sleep(1000);
			String departureCity = ConfigFile.getDepCity();
			String arrivalCity = ConfigFile.getArrCity();
			String city = ConfigFile.getCity();
			pageFactory.enterDepartureCity(departureCity);
			pageFactory.selectCity(city);
			pageFactory.enterArrivalCity(arrivalCity);
			boolean isClicked = pageFactory.searchFlights();
			
			// Assertion
			Assert.assertTrue(isClicked, "Button was not clicked");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}
}
