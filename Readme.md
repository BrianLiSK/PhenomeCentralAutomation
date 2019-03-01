# Integration Tests for PhenomeCentral

##### End-to-end tests on a native web browser using Selenium

Jira story link: https://phenotips.atlassian.net/browse/PC-325

## Major Maven Dependencies
- [Selenium WebDriver](https://www.seleniumhq.org/projects/webdriver/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)
- [TestNG](https://testng.org/doc/index.html)
- [Allure](http://allure.qatools.ru/)

## Requirements
- JDK 1.8 or above. The codebase uses some Java 8 features such as lambdas. 
- phenomecentral.org (parent project) already built

## Usage
### Quick start
- Clone this repository
- Build phenomecentral.org
- `cd standalone/target` and ensure that `phenomecentral-standalone*.zip` is present from a `ls`
- `cd ../../scripts` and then `./runIntegrationTests.sh` to run locally with default environment variables.

This script will extract the standalone zip to a subfolder named "instances", start up the PC instance, run the tests, and generate an Allure report. 

By default, it will start PC on port 8083, with stop port 8084, run the tests using Chrome, have emails listened to on port 1025 and the email inbox page on port 8085. All dependencies taken care of by Maven.

### Custom Environment
- `runIntegrationTests.sh` accepts several arguments to specify several (optional) variables. `./runIntegrationTests.sh --help` for details.
- Example: `./runIntegrationTests.sh --browser chrome --start 8083 --stop 8084 --emailUI 8085`

If you already have a running instance:
- Start the SMTP server: `java -jar fake-smtp/MockMock.jar -p 1025 -h 8085`
- Run: `mvn test -Dsurefire.suiteXmlFiles=src/test/java/org/phenotips/endtoendtests/testcases/xml/AllTests.xml -Dbrowser=DESIRED_BROWSER -DhomePageURL=PC_INSTANCE_URL -DemailUIPageURL=EMAIL_UI_URL` replace `DESIRED_BROWSER` with one of `chrome, firefox, safari, edge, or ie` and `PC_Instance_URL` with the URL of the running instance and `EMAIL_UI_URL` with the URL to access the email inbox
	- Example: `mvn test -Dsurefire.suiteXmlFiles=src/test/java/org/phenotips/endtoendtests/testcases/xml/AllTests.xml -Dbrowser=chrome -DhomePageURL=localhost:8083 -DemailUIPageURL=localhost:8085`


## Opening the Allure Report
- Report is stored in `target/site/allure-maven-plugin/`. Open `index.html` with a browser other than Chrome to view the report.
- Chrome has a known feature of not allowing accessing data from other files.

## Running on IE
- These instructions need to be followed before IE can be used. Notably, you must manually disable "Enhanced Protected Mode" in the IE's settings before automated test software such as Selenium is able to interact with an IE browser. See https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver#required-configuration

## Test Assumptions
- The test suite assumes a blank PC instance.
- Certain tests check for emails. Ensure that the PC instance is setup to allow for emails to be sent. If running locally, that would mean a fakeSMTP service has started. Even if we don't run tests for emails (there are only two or three), we still need a working SMTP to approve new users
	

## Architecture
- The design of the test suite follows a PageObject model/design pattern. 
- Classes in the `pageobjects` package contain selectors for a page. It also contains methods that interact with a single webpage. For instance, in the `LoginPage` class, there is a method to type in a passed username, password, and then click on the login button. These call native Selenium methods to interact with the page, such as clicking and typing.
	-  `BasePage` is an abstract class that contains common methods for every page such as logging out. I.e. it contains what is on the toolbar that appears on every page.
- Classes in the `testcases` define endtoend test cases. A class defines a "theme" of tests. Most tests should be able to run individually given that the `SetupUsers` class has been run at least once for the PC instance. See individual classes for details.
- Classes in the `common` package contain classes or interface that are used in both test cases and pageobjects. Notably, we have `CommonPatientMeasurements` which defines measurements assigned to a patient. An object of that type can be constructed during a test case as something to verify against, and it can also be constructed in a page object class to define what is on the View Patient form.
- TestNG provides a `@Test` annotation to allow for running test methods and classes directly in an IDE by providing a main function. In intelliJ, you should see a green run triangle in front of each line on the test method declaration that you can interact with.
- `BasePage` contains environment variables such as the PC instance URL that can be changed directly if you want to run these tests from the IDE without supplying environment variables.
	- Change the variables HOMEPAGE_URL, ADMIN_USERNAME, ADMIN_PASS, etc. as needed

- XML files that define a test suite are located in `testcases/xml`. TestNG uses one of these XML files to execute a test suite during `mvn test`. The default is `NoTests.xml`.
	- By default, these end-to-end tests will not run on a build of phenomecentral.org during the `mvn install` or `mvn test` lifecycle. This prevents failure of the entire build if even one end to end test fails. Instead, we have to pass a different XML file with the `-Dsurefire.suiteXmlFiles` flag and run `mvn test` explicitly. See Usage.
	- The XML defines the order the tests should be run in. Instead of using `@dependsOnMethods`, `@Groups`, or `@priority` in the testcases code, we define the desired order on the XML. Each of those mentioned annotations creates a global variable that can conflict with each other and cause undefined behaviour if you are not careful. For example, don't mix @priority with @dependsonMethods as the order of importance is not well defined in that case.
- Allure has an attached listener that gets passed information from the `@step` annotation

## Suggestions for future
- Investigate video recording
- Multiple measurement entries

## Limitations
- UI/UX issues can't be detected  effectively. Ex. Element is present but is in 1 pt font or hard to see.
- A seperate machine or server is highly recommended for test stability. While Selenium does not explicitly capture the mouse or keyboard, the browser window might come into focus if a native OS error dialogue (such as the one we get when being prompted for unsaved changes) pops up. Use with caution. 
