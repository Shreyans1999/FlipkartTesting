package com.flipkart.tests.e2e;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.flipkart.models.AddressData;
import com.flipkart.models.UserData;
import com.flipkart.pages.FlipkartAddToCart;
import com.flipkart.pages.FlipkartPlaceOrder;
import com.flipkart.tests.base.BaseTest;

/**
 * Cart and order end-to-end test cases.
 * Demonstrates use of POJO models for address and user data.
 */
public class CartAndOrderTests extends BaseTest {

    @Test(groups = {"e2e", "cart"})
    public void testAddToCart() throws InterruptedException {
        logger.info("Testing add to cart functionality");
        
        // Navigate to product page
        String productLink = config.getProductLink();
        if (productLink != null) {
            navigateTo(productLink);
        } else {
            navigateToBaseUrl();
        }
        Thread.sleep(2000);
        
        FlipkartAddToCart cartPage = new FlipkartAddToCart(driver);
        boolean result = cartPage.clickAddToCartButton();
        
        Assert.assertTrue(result, "Product should be added to cart successfully");
        logger.info("Add to cart test passed");
    }

    @Test(groups = {"e2e", "order"})
    public void testPlaceOrderFlow() throws InterruptedException {
        logger.info("Testing place order flow");
        
        // Using POJO models for test data
        UserData userData = UserData.builder()
            .email("test@example.com")
            .phoneNumber(config.getPhoneNumber())
            .build();
        
        AddressData addressData = AddressData.builder()
            .pincode(config.getPincode())
            .addressLine1("123 Test Street")
            .city("Bangalore")
            .state("Karnataka")
            .build();
        
        logger.info("User: " + userData);
        logger.info("Address: " + addressData);
        
        // Navigate to product page
        String productLink = config.getProductLink();
        if (productLink != null) {
            navigateTo(productLink);
        } else {
            navigateToBaseUrl();
        }
        Thread.sleep(2000);
        
        FlipkartPlaceOrder orderPage = new FlipkartPlaceOrder(driver);
        boolean result = orderPage.completePlaceOrderFlow(userData.getEmail());
        
        Assert.assertTrue(result, "Place order flow should complete successfully");
        logger.info("Place order flow test passed");
    }
}
