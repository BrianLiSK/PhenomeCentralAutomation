package org.phenotips.testcases;

import org.phenotips.pageobjects.HomePage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * An abstract test. All tests should inherit this class.
 * We should put any high level methods using @after- annotations here
 * We can also put any high level @BeforeSuite methods here too to setup/check main conditions.
 */
public abstract class BaseTest
{
    /*************************************************************
     * Change browserToUse to the desired browser
     *************************************************************/
    private final activeBrowser browserToUse = activeBrowser.CHROME;

    private enum activeBrowser {
        CHROME, FIREFOX, SAFARI, EDGE, IE
    }

    protected static WebDriver theDriver;

    /**
     * Instantiate the webDriver instance here. The WebDriverManager takes care of setting up the
     * environment including the intermediary protocol used to communicate with the browser.
     * This allows a user to just run the tests and the manager should take care of finding the path
     * to the desired executable and setting up environment.
     */
    public BaseTest() {
        if (theDriver == null) {
            if (browserToUse == activeBrowser.CHROME) {
                WebDriverManager.chromedriver().setup();
                theDriver = new ChromeDriver();
            } else if (browserToUse == activeBrowser.FIREFOX) {
                WebDriverManager.firefoxdriver().setup();
                theDriver = new FirefoxDriver();
            } else if (browserToUse == activeBrowser.EDGE) {
                WebDriverManager.edgedriver().setup();
                theDriver = new EdgeDriver();
            } else if (browserToUse == activeBrowser.IE) {
                WebDriverManager.iedriver().setup();
                theDriver = new InternetExplorerDriver();
            } else if (browserToUse == activeBrowser.SAFARI) {
                // No need to setup for Safari, native Selenium api should work... according to Apple docs
                theDriver = new SafariDriver();
            } else {
                System.out.println("Unknown browser, defaulting to Chrome");
                WebDriverManager.chromedriver().setup();
                theDriver = new ChromeDriver();
            }

            System.out.println("Initiating the webDriver for: " + browserToUse.toString());
        }
    }

    /**
     * Runs after every test method. In the case that TestNG's listener reports a failure, will take a
     * screenshot and copy over to targets/screenshots directory as a .png with a methodName and timeStamp
     * This will also navigate to a blank page taking care of any "Unsaved Changes" warning box so that the next test
     * can be attempted. Without, the warning modal would block all the other tests.
     * @param testResult resulting status of a test method that has just run, as reported by TestNGs listener.
     *        Check this passed info for failure.
     */
    @AfterMethod
    public void onTestFailure(ITestResult testResult)
    {
        HomePage tempHomePage = new HomePage(theDriver);

        if (ITestResult.FAILURE == testResult.getStatus()) {

            // Navigate to a blank page. Might trigger a warning modal for unsaved edits.
            // Ensure that any open alert dialogue is closed before continuing.
            // Navigating away and taking care of potential unsaved changes alert allows the next test
            // to be run.
            try {
                theDriver.navigate().to("about:blank");
                theDriver.switchTo().alert().accept();
                theDriver.switchTo().defaultContent();
                tempHomePage.logOut();
                tempHomePage.navigateToLoginPage();
                System.out.println("Test failure, navigate to blank page. Closed an unsaved changes warning dialogue");
            } catch (NoAlertPresentException e) {
                tempHomePage.logOut();
                tempHomePage.navigateToLoginPage();
                System.out.println("Test failure, navigate to blank page. There is no unsaved changes warning.");
            }

            // Screenshot mechanism
            // Cast webDriver over to TakeScreenshot. Call getScreenshotAs method to create image file
            File srcFile = ((TakesScreenshot)theDriver).getScreenshotAs(OutputType.FILE);

            LocalDateTime dateTime = ZonedDateTime.now().toLocalDateTime();

            // Save screenshot in target/screenshots folder with the methodName of failed test and timestamp.
            File destFile = new File("target/screenshots/" + testResult.getMethod().getMethodName() + " " + dateTime + ".png");

            System.out.println("Test failed. Taking screenshot...");

            // Copy over to target/screenshots folder
            try {
                FileUtils.copyFile(srcFile, destFile);
            } catch (IOException e) {
                System.out.println("Something went wrong copying over screenshot: " + e);
            }

        }

        else {
            System.out.println("Method (test) suceeded or skipped. No screenshot. Moving on.");
        }

        // Always try to close warning dialogue regardless of test result

        try {
            theDriver.switchTo().alert().accept();
            System.out.println("Had to close a warning dialogue and logout again.");
        } catch (NoAlertPresentException e) {
            // There was no alert dialogue that appeared, so do nothing.
        }

    }

    /**
     * Runs after each class finishes. Now no longer really being used, except for a debug message.
     */
    @AfterClass
    public void testCleanup()
    {
        System.out.println("A single class has finished");
    }

    /**
     * Runs after the entire suite (all test cases specified in the test run) are finished. We explicitly quit
     * the webDriver here as it does not close on its own when the reference is trashed. Quitting the webDriver means
     * closing the browser window and quitting the firefox process (important).
     */
    @AfterSuite
    public void cleanup()
    {
        System.out.println("Test suite finished running");

        if (theDriver != null) {
            theDriver.quit();
        }
    }
}
