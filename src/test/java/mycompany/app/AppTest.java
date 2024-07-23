package com.mycompany.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Integration UI test for PHP App.
 */
public class AppTest {
    WebDriver driver; 
    WebDriverWait wait; 
    String url = "http://127.0.0.1/";

    @Before
    public void setUp() { 
        // Initialize the WebDriver (HtmlUnitDriver for headless testing)
        driver = new HtmlUnitDriver(true); 
        wait = new WebDriverWait(driver, 10); 
        driver.get(url); // Navigate to the home page
    } 

    @After
    public void tearDown() { 
        // Quit the WebDriver
        driver.quit(); 
    } 

    @Test
    public void testSearchFunctionality() {
        // Test the search functionality with a valid term
        WebElement searchInput = driver.findElement(By.name("query"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));
        
        // Enter a search term and submit
        searchInput.sendKeys("Valid search term");
        submitButton.click();
        
        // Wait for the results page and validate
        wait.until(ExpectedConditions.titleContains("Search Results"));
        WebElement resultText = driver.findElement(By.tagName("p"));
        
        assertTrue("Search term should be displayed", resultText.getText().contains("Sanitized Search Term: Valid search term"));
    }
    
    @Test
    public void testInvalidXSSInput() {
        // Test the search functionality with a term that might be considered an XSS attack
        WebElement searchInput = driver.findElement(By.name("query"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));
        
        // Enter an XSS payload and submit
        searchInput.sendKeys("<script>alert('XSS');</script>");
        submitButton.click();
        
        // Wait for redirection to home page and validate the error message
        wait.until(ExpectedConditions.titleContains("Search Page"));
        WebElement errorMessage = driver.findElement(By.className("error"));
        
        assertTrue("Error message should be displayed", errorMessage.getText().contains("Invalid search term. Please try again."));
    }
    
    @Test
    public void testInvalidSQLInjectionInput() {
        // Test the search functionality with a term that might be considered an SQL injection attack
        WebElement searchInput = driver.findElement(By.name("query"));
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit']"));
        
        // Enter an SQL injection payload and submit
        searchInput.sendKeys("'; DROP TABLE users; --");
        submitButton.click();
        
        // Wait for redirection to home page and validate the error message
        wait.until(ExpectedConditions.titleContains("Search Page"));
        WebElement errorMessage = driver.findElement(By.className("error"));
        
        assertTrue("Error message should be displayed", errorMessage.getText().contains("Invalid search term. Please try again."));
    }
}
