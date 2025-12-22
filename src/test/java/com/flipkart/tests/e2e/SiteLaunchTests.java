package com.flipkart.tests.e2e;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.flipkart.tests.base.BaseTest;

/**
 * Smoke test - verifies Flipkart homepage loads correctly
 */
public class SiteLaunchTests extends BaseTest {

    @Test(groups = {"smoke"})
    public void verifyHomepageLoads() {
        logger.info("Launching Flipkart Site");
        
        navigateToBaseUrl();
        
        // Assertion - verify URL contains flipkart
        String currentURL = getCurrentUrl();
        Assert.assertTrue(currentURL.contains("flipkart"), 
            "Not on Flipkart site. Current URL: " + currentURL);
        
        logger.info("Homepage loaded successfully: " + currentURL);
    }
    
    @Test(groups = {"smoke"})
    public void verifyPageTitle() {
        logger.info("Verifying page title");
        
        navigateToBaseUrl();
        
        String title = getPageTitle();
        Assert.assertTrue(title.toLowerCase().contains("flipkart") || 
                         title.toLowerCase().contains("online shopping"),
            "Page title does not indicate Flipkart. Title: " + title);
        
        logger.info("Page title verified: " + title);
    }
}
