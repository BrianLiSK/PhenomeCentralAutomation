# Integration Tests for PhenomeCentral

##### End-to-end tests on a native web browser using Selenium

Jira story link: https://phenotips.atlassian.net/browse/PC-325

## Major Maven Dependencies
- Selenium WebDriver
- WebDriverManager
- TestNG
- Allure

## Requirements
- Fresh PC instance
- Certain tests check for emails. Ensure that the PC instance is setup to allow for emails to be sent and that a fakeSMTP service has started

## Usage
Quick start:
- Clone this repository
- Build phenomecentral.org
- `cd standalone/target` and ensure that phenomecentral-standalone*.zip is present from a `ls`
- `cd ../../scripts` and then `./runIntegrationTests.sh`
This script will extract the standalone zip to a subfolder named "instances", start up the PC instance, run the tests, and generate an Allure report. All dependencies taken care of by Maven
- `runIntegrationTests.sh` accepts several arguments to specify several (optional) variables. `./runIntegrationTests.sh --help` for details.
- Example: `./runIntegrationTests.sh --browser chrome --start 8083 --stop 8084 --emailUI 8085 --emailListen 1025

If you already have a running instance:
- `java -jar fake-smtp/MockMock.jar -p 1025 -h 8085`
- `mvn test -Dsurefire=xmlLocation -Dbrowser=DesiredBrowser -PCInstanceURL=localhost:8085 -emailUIURL=8086`

## Opening the Allure Report
- Report is stored in

## Architecture

- Start MockMock fakeSMTP by running `java -jar MockMock.jar -p 1025 -h 8085` in the root folder. Change 1025 and 8085 to the outgoing port on PC and the port to access the email UI respectively
- Modify `src/main/java/org.phenotips.endtoendtests.pageobjects/BasePage.java` to specify the login credentials and the address of the PC instance (`HOMEPAGE_URL`, `ADMIN_USERNAME`, `ADMIN_PASS`, etc.).
- Navigate to `src/main/java/org.phenotips.endtoendtests.testcases.java` and run either `CreatePatientTest` or `LoginPageTest`. The run button is located on the class declaration line beside the line number.
	- Alternatively, right click on `MultipleClasses.xml` and then run to run both test classes

## Limitations
- UI/UX issues can't be detected  effectively. Ex. Element is present but is in 1 pt font or hard to see.
- Can't handle native operating system dialoge boxes. Ex. Browser produces a non-web based warning dialgoue. Selenium has no control over those.
- Need to use a third party SMTP browser based service to test emails. Ex. https://github.com/tweakers/MockMock might work.
	- Clone and in release folder run `java -jar MockMock.jar -p 1025 -h 8085`
- A seperate machine or server is highly recommended for test stability. 
