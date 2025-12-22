package com.flipkart.tests.e2e;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.flipkart.models.ProductData;
import com.flipkart.pages.FlipkartBatteryFilter;
import com.flipkart.pages.FlipkartBrandFilters;
import com.flipkart.pages.FlipkartSortFilter;
import com.flipkart.tests.base.BaseTest;

/**
 * Product filter and sort test cases.
 * Demonstrates use of POJO models for product data.
 */
public class ProductFilterTests extends BaseTest {

    @Test(groups = {"regression", "filter"})
    public void testBrandFilter() throws InterruptedException {
        logger.info("Testing brand filter functionality");
        
        // Use product data POJO
        ProductData productData = ProductData.builder()
            .category("Mobiles")
            .brand("Google")
            .build();
        
        logger.info("Testing filter for: " + productData);
        
        // Navigate to filter page
        String filterLink = config.getFilterLink();
        navigateTo(filterLink);
        Thread.sleep(2000);
        
        FlipkartBrandFilters brandFilter = new FlipkartBrandFilters(driver);
        boolean result = brandFilter.brandFilter();
        
        Assert.assertTrue(result, "Brand filter should be applied successfully");
        logger.info("Brand filter test passed");
    }

    @Test(groups = {"regression", "filter"})
    public void testBatteryCapacityFilter() throws InterruptedException {
        logger.info("Testing battery capacity filter");
        
        // Navigate to filter page
        String filterLink = config.getFilterLink();
        navigateTo(filterLink);
        Thread.sleep(2000);
        
        FlipkartBatteryFilter batteryFilter = new FlipkartBatteryFilter(driver);
        boolean result = batteryFilter.selectBatteryCapacity();
        
        Assert.assertTrue(result, "Battery filter should be applied successfully");
        logger.info("Battery filter test passed");
    }

    @Test(groups = {"regression", "filter"})
    public void testSortLowToHigh() throws InterruptedException {
        logger.info("Testing sort by price low to high");
        
        // Navigate to filter page
        String filterLink = config.getFilterLink();
        navigateTo(filterLink);
        Thread.sleep(2000);
        
        FlipkartSortFilter sortFilter = new FlipkartSortFilter(driver);
        boolean result = sortFilter.applySortLowToHigh();
        
        Assert.assertTrue(result, "Sort filter should be applied successfully");
        logger.info("Sort filter test passed");
    }
}
