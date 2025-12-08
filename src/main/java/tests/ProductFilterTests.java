package tests;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.FlipkartBatteryFilter;
import pages.FlipkartBrandFilters;
import pages.FlipkartPriceFilters;
import pages.FlipkartRatingFilters;
import pages.FlipkartSortFilter;

/**
 * Product filter and sort test cases
 */
public class ProductFilterTests extends BaseTest {

	@Test
	public void sortProduct(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "sortProduct");
		test = extent.createTest("sortProduct", "This test case is to sort product based on price");
		test.log(Status.INFO, "This test case is to sort product based on price");
		logger.info("Sorting products from Low To High Prices");
		
		try {
			String filterLink = ConfigFile.getFilterLink();
			driver.get(filterLink);
			FlipkartSortFilter pageFactory = new FlipkartSortFilter(driver);
			boolean isClicked = pageFactory.applySortLowToHigh();
			
			// Assertion
			Assert.assertTrue(isClicked, "Button was not clicked");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void applyPriceFilter(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "applyPriceFilter");
		test = extent.createTest("applyPriceFilter", "This test case is to apply price filter");
		test.log(Status.INFO, "This test case is to apply price filter");
		logger.info("Applying the price filter on search results");
		
		try {
			String filterLink = ConfigFile.getFilterLink();
			driver.get(filterLink);
			FlipkartPriceFilters pageFactory = new FlipkartPriceFilters(driver);
			boolean isClicked = pageFactory.priceTag();
			
			// Assertion
			Assert.assertTrue(isClicked, "Button was not clicked");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void applyBrandFilter(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "applyBrandFilter");
		test = extent.createTest("applyBrandFilter", "This test case is to apply brand filter");
		test.log(Status.INFO, "This test case is to apply brand filter");
		logger.info("Applying the brand filter on search results");
		
		try {
			String filterLink = ConfigFile.getFilterLink();
			driver.get(filterLink);
			FlipkartBrandFilters pageFactory = new FlipkartBrandFilters(driver);
			boolean isClicked = pageFactory.brandFilter();
			
			// Assertion
			Assert.assertTrue(isClicked, "Filter was not selected");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void applyRatingFilter(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "applyRatingFilter");
		test = extent.createTest("applyRatingFilter", "This test case is to apply rating filter");
		test.log(Status.INFO, "This test case is to apply rating filter");
		logger.info("Applying the rating filter on search results");
		
		try {
			String filterLink = ConfigFile.getFilterLink();
			driver.get(filterLink);
			FlipkartRatingFilters pageFactory = new FlipkartRatingFilters(driver);
			boolean isClicked = pageFactory.ratingFilter();
			
			// Assertion
			Assert.assertTrue(isClicked, "Filter was not selected");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void applyBatteryCapacityFilter(ITestContext context) throws InterruptedException {
		context.setAttribute("testCaseName", "applyBatteryCapacityFilter");
		test = extent.createTest("applyBatteryCapacityFilter", "This test case is to apply Battery Capacity filter");
		test.log(Status.INFO, "This test case is to apply Battery Capacity filter");
		logger.info("Applying the Battery Capacity filter on search results");
		
		try {
			String filterLink = ConfigFile.getFilterLink();
			driver.get(filterLink);
			FlipkartBatteryFilter pageFactory = new FlipkartBatteryFilter(driver);
			boolean isClicked = pageFactory.selectBatteryCapacity();
			
			// Assertion
			Assert.assertTrue(isClicked, "Filter was not selected");
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception occurred: " + e.getMessage());
			throw e;
		}
	}
}
