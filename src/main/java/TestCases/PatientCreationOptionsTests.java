package TestCases;

import org.testng.annotations.Test;

import PageObjects.CreatePatientPage;
import PageObjects.HomePage;

/**
 * This class of tests will eventually cycle through the possible options when creating a patient via
 * manual input.
 * If a change causes a section or some options to disappear, it should fail due to missing selectors.
 */
public class PatientCreationOptionsTests extends BaseTest implements CommonInfoEnums
{
    private HomePage aHomePage = new HomePage(theDriver);

    private CreatePatientPage aCreationPage = new CreatePatientPage(theDriver);

    // Cycle through all the options on the "Patient Information" Section.
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
            .setLifeStatus("Alive")
            .setLifeStatus("Deceased")
            .setLifeStatus("Alive");

        for (int i = 1; i <= 12; ++i) {
            if (i < 10) {
                aCreationPage.setDOB("0" + String.valueOf(i), "2019");
            }
            else {
                aCreationPage.setDOB(String.valueOf(i), "2019");
            }
        }

        aCreationPage.setGender("Male")
            .setGender("Female")
            .setGender("Other")
            .setGender("Unknown")
            .setGender("Male");

        aCreationPage.cycleThroughAgeOfOnset()
            .cycleThroughModeOfInheritance()
            .cycleThroughModeOfInheritance()
            .setIndicationForReferral("Now cycle through the other sections...");

    }
}
