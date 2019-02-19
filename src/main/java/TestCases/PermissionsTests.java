package TestCases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageobjects.AdminMatchNotificationPage;
import pageobjects.AdminRefreshMatchesPage;
import pageobjects.HomePage;
import pageobjects.ViewPatientPage;
import net.bytebuddy.utility.RandomString;

/**
 * This class tests that when permissions are modified, patients matching behaviour is modified.
 * These tests must be run as a class.
 */
public class PermissionsTests extends BaseTest
{
    HomePage aHomePage = new HomePage(theDriver);

    ViewPatientPage aViewPatientPage = new ViewPatientPage(theDriver);

    AdminRefreshMatchesPage anAdminRefreshMatchesPage = new AdminRefreshMatchesPage(theDriver);

    AdminMatchNotificationPage anAdminMatchNotificationPage = new AdminMatchNotificationPage(theDriver);

    final private String randomChars = RandomString.make(5);

    /**
     * Creates two patients with identical phenotypes and genotypes.
     * One is "Private" and the other is "Matchable". Assert that:
     * - 1 Patients Processed during refresh of matches
     * - No match is found on Admin's match page
     */
    @Test()
    public void noMatchPrivatePatient()
    {
        List<String> loPhenotypesToAdd = new ArrayList<String>(Arrays.asList(
            "Nausea and vomiting", "Poikilocytosis", "Swollen lip", "Narcolepsy", "Eye poking"));

        final String patientUniqueIdentifier = "NoPermissionForMatch " + randomChars;

        String createdPatient1;
        String createdPatient2;

        aHomePage.navigateToLoginPage()
            .loginAsAdmin()
            .navigateToAdminSettingsPage()
            .navigateToRefreshMatchesPage()
            .refreshMatchesSinceLastUpdate() // Refresh Matches so that "Since last update" goes to 0 first
            .logOut()

            .loginAsUser()
            .navigateToCreateANewPatientPage()
            .toggleFirstFourConsentBoxes()
            .updateConsent()
            .setIdentifer(patientUniqueIdentifier)
            .setDOB("02", "2002")
            .setGender("Female")
            .expandSection(CommonInfoEnums.SECTIONS.ClinicalSymptomsSection)
            .addPhenotypes(loPhenotypesToAdd)
            .expandSection(CommonInfoEnums.SECTIONS.ClinicalSymptomsSection)
            .expandSection(CommonInfoEnums.SECTIONS.GenotypeInfoSection)
            .addGene("SH2B3", "Confirmed causal", "Sequencing")
            .saveAndViewSummary();

        aViewPatientPage.setGlobalVisibility("Private");

        createdPatient1 = aViewPatientPage.getPatientID();

        aViewPatientPage.logOut()
            .loginAsUserTwo()
            .navigateToCreateANewPatientPage()
            .toggleFirstFourConsentBoxes()
            .updateConsent()
            .setIdentifer(patientUniqueIdentifier + "Matchee")
            .setDOB("03", "2003")
            .setGender("Male")
            .expandSection(CommonInfoEnums.SECTIONS.ClinicalSymptomsSection)
            .addPhenotypes(loPhenotypesToAdd)
            .expandSection(CommonInfoEnums.SECTIONS.ClinicalSymptomsSection)
            .expandSection(CommonInfoEnums.SECTIONS.GenotypeInfoSection)
            .addGene("SH2B3", "Confirmed causal", "Sequencing")
            .saveAndViewSummary();

        createdPatient2 = aViewPatientPage.getPatientID();

        aViewPatientPage.logOut()
            .loginAsAdmin()
            .navigateToAdminSettingsPage()
            .navigateToRefreshMatchesPage()
            .refreshMatchesSinceLastUpdate();

        Assert.assertEquals(anAdminRefreshMatchesPage.getNumberOfLocalPatientsProcessed(), "1");

        anAdminRefreshMatchesPage.navigateToAdminSettingsPage()
            .navigateToMatchingNotificationPage()
            .filterByID(createdPatient1);

        Assert.assertFalse(anAdminMatchNotificationPage.doesMatchExist(createdPatient1, createdPatient2));
    }

    /**
     * Ensure that the matchable patient created by User2Dos cannot be seen by User1Uno.
     * Asserts that the unauthorized action error message page is presented.
     */
    @Test()
    public void cannotSeeOtherPatients()
    {
        String unauthorizedActionMsgCheck = "You are not allowed to view this page or perform this action.";

        aHomePage.navigateToLoginPage()
            .loginAsUserTwo()
            .navigateToAllPatientsPage()
            .sortPatientsDateDesc()
            .viewFirstPatientInTable()
            .logOut()
            .loginAsUser();

        Assert.assertEquals(aHomePage.getUnauthorizedErrorMessage(), unauthorizedActionMsgCheck);
    }
}
