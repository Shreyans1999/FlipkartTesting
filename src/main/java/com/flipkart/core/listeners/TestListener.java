package com.flipkart.core.listeners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.flipkart.core.driver.DriverFactory;

/**
 * TestListener - TestNG ITestListener implementation for:
 * - Automatic ExtentReport generation
 * - Screenshot capture on test failure
 * - Test execution logging
 */
public class TestListener implements ITestListener {
    
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();
    
    private static final String REPORT_DIR = "reports/";
    private static final String SCREENSHOT_DIR = "screenshots/";
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("========================================");
        System.out.println("Test Suite Started: " + context.getName());
        System.out.println("========================================");
        
        // Initialize ExtentReports
        if (extent == null) {
            initExtentReport(context);
        }
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("========================================");
        System.out.println("Test Suite Finished: " + context.getName());
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
        System.out.println("========================================");
        
        // Flush the report
        if (extent != null) {
            extent.flush();
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();
        
        System.out.println("Starting Test: " + className + "." + testName);
        
        // Create test in ExtentReport
        ExtentTest test = extent.createTest(className + " - " + testName);
        test.assignCategory(className);
        testThreadLocal.set(test);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("✓ Test PASSED: " + testName);
        
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.log(Status.PASS, MarkupHelper.createLabel(testName + " PASSED", ExtentColor.GREEN));
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("✗ Test FAILED: " + testName);
        System.out.println("Failure Reason: " + result.getThrowable().getMessage());
        
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel(testName + " FAILED", ExtentColor.RED));
            test.log(Status.FAIL, result.getThrowable());
            
            // Take screenshot on failure
            try {
                if (DriverFactory.hasDriver()) {
                    String screenshotPath = captureScreenshot(testName);
                    test.addScreenCaptureFromPath(screenshotPath);
                }
            } catch (Exception e) {
                test.log(Status.WARNING, "Could not capture screenshot: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("○ Test SKIPPED: " + testName);
        
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.log(Status.SKIP, MarkupHelper.createLabel(testName + " SKIPPED", ExtentColor.YELLOW));
            if (result.getThrowable() != null) {
                test.log(Status.SKIP, result.getThrowable());
            }
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not commonly used, but can be implemented if needed
    }
    
    /**
     * Initialize ExtentReports with SparkReporter
     */
    private void initExtentReport(ITestContext context) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = REPORT_DIR + "ExtentReport_" + timestamp + ".html";
        
        // Ensure directory exists
        new File(REPORT_DIR).mkdirs();
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Flipkart Automation Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // System info
        extent.setSystemInfo("Application", "Flipkart");
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Suite Name", context.getSuite().getName());
        
        System.out.println("ExtentReport initialized: " + reportPath);
    }
    
    /**
     * Capture screenshot and save to file
     * @param testName Name of the test for filename
     * @return Path to the screenshot file
     */
    private String captureScreenshot(String testName) {
        // Ensure directory exists
        new File(SCREENSHOT_DIR).mkdirs();
        
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String screenshotPath = SCREENSHOT_DIR + testName + "_" + timestamp + ".png";
        
        try {
            WebDriver driver = DriverFactory.getDriver();
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(screenshotPath));
            System.out.println("Screenshot saved: " + screenshotPath);
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
        
        return screenshotPath;
    }
    
    /**
     * Get current test's ExtentTest instance
     * Useful for adding custom logs from test methods
     */
    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }
    
    /**
     * Log info message to current test
     */
    public static void logInfo(String message) {
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }
    
    /**
     * Log warning message to current test
     */
    public static void logWarning(String message) {
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.log(Status.WARNING, message);
        }
    }
}
