package TestCases;

import org.testng.annotations.Test;

import PageObjects.CreatePatientPage;
import PageObjects.HomePage;

public class PatientCreationOptionsTests extends BaseTest implements CommonInfoEnums
{
    private HomePage aHomePage = new HomePage(theDriver);

    private CreatePatientPage aCreationPage = new CreatePatientPage(theDriver);

    @Test
    public void cycleThroughInfoOptions() {
        aHomePage.navigateToLoginPage()
            .loginAsUser()
            .navigateToCreateANewPatientPage()
            .toggleNthConsentBox(1)
            .toggleNthConsentBox(2)
            .toggleNthConsentBox(3)
            .toggleNthConsentBox(4)
            .updateConsent()
            .setIdentifer("Auto Cycling Options")
            .setGender("male")
            .setGender("female")
            .setGender("other")
            .setGender("unknown")
            .setGender("male");
        for (int i = 1; i <= 9; ++i) {
            aCreationPage.setDOB("0" + String.valueOf(i), "2019");
        }
        for (int i = 10; i <= 12; ++i) {
            aCreationPage.setDOB(String.valueOf(i), "2019");
        }
        aCreationPage.cycleThroughAgeOfOnset().cycleThroughModeOfInheritance();

    }
}
