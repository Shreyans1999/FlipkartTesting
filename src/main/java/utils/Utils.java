package utils;

import org.openqa.selenium.WebDriver;
import java.util.Set;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;


public class Utils {
	
//	This Method Provides test data from an Excel file.
	public static Object[][] provideTestData(String filePath, String sheetName) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        Object[][] testData = new Object[rowCount][2];
        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            String testCaseName = row.getCell(0).getStringCellValue();
            String executionRequired = row.getCell(1).getStringCellValue();
            testData[i - 1][0] = testCaseName;
            testData[i - 1][1] = executionRequired;
        }
        return testData;
    }
	

//	This method is for handling New Tab
    public static String handleNewTab(WebDriver driver) {
        String originalTab = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();
        
        for (String handle : windowHandles) {
            if (!handle.equals(originalTab)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        return originalTab;
    }
    
    
    // Method to take a screenshot
    public static String takeScreenShot(WebDriver driver, String testCaseName) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date(0));
        String screenshotPath = "src/main/java/reports/screenshots/" + testCaseName + "_" + timeStamp + ".png";
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(screenshotPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenshotPath;
    }
    
//    This method is for capturing screenshot in real time
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
    }
}
