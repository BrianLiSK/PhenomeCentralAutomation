package TestCases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObjects.CreatePatientPage;
import PageObjects.HomePage;
import PageObjects.PedigreeEditorPage;
import net.bytebuddy.utility.RandomString;

/**
 * Tests for the Pedigree Editor page and the sync with the patient info page.
 */
public class PedigreePageTest extends BaseTest implements CommonInfoEnums
{
    final private HomePage aHomePage = new HomePage(theDriver);

    final private PedigreeEditorPage aPedigreeEditorPage = new PedigreeEditorPage(theDriver);

    final private CreatePatientPage aCreatePatientPage = new CreatePatientPage(theDriver);

    final private String randomChars = RandomString.make(5);

    // Creates a patient with phenotypes and genes. Asserts that they are reflected
    //   in the pedigree editor after a save. This tests the pedigree editor when one patient/node is present.
    //   Checks that Patient Form Info -> Pedigree Editor Info
    @Test
    public void basicPedigree()
    {
        final List<String> checkPhenotypes = new ArrayList<String>(Arrays.asList(
            "Prominent nose", "Macrocephaly at birth", "Narcolepsy", "Large elbow", "Terminal insomnia", "Small hand"));
        final List<String> checkCandidateGenes = new ArrayList<String>(Arrays.asList("IV", "IVD"));
        final List<String> checkConfirmedCausalGenes = new ArrayList<String>(Arrays.asList("IVL"));
        final List<String> checkCarrierGenes = new ArrayList<String>(Arrays.asList("OR6B1"));

        aHomePage.navigateToLoginPage()
            .loginAsUser()
            .navigateToCreateANewPatientPage()
            .toggleNthConsentBox(1)
            .toggleNthConsentBox(2)
            .toggleNthConsentBox(3)
            .toggleNthConsentBox(4)
            .updateConsent()
            .setIdentifer("Pedigree Editor " + randomChars)
            .setDOB("05", "2000")
            .setGender("Female")
            .setOnset("Congenital onset ")
            .expandSection(SECTIONS.ClinicalSymptomsSection)
            .addPhenotype("Prominent nose")
            .addPhenotype("Large elbow")
            .addPhenotype("Small hand")
            .addPhenotype("Macrocephaly at birth")
            .addPhenotype("Narcolepsy")
            .addPhenotype("Terminal insomnia ")
            .expandSection(SECTIONS.ClinicalSymptomsSection)
            .expandSection(SECTIONS.GenotypeInfoSection)
            .addGene("IV", "Candidate", "Sequencing")
            .addGene("IVD", "Candidate", "Sequencing")
            .addGene("IVL", "Confirmed causal", "Sequencing")
            .addGene("OR6B1", "Carrier", "Sequencing")
            .saveAndViewSummary()
            .editThisPatient()
            .expandSection(SECTIONS.FamilyHistorySection)
            .navigateToPedigreeEditor("")
            .openNthEditModal(1);

        List<String> loPhenotypesFound = aPedigreeEditorPage.getPhenotypes();
        List<String> loCandidateGenesFound = aPedigreeEditorPage.getGenes("Candidate");
        List<String> loConfirmedCausalGenesFound = aPedigreeEditorPage.getGenes("Confirmed Causal");
        List<String> loCarrierGenesFound = aPedigreeEditorPage.getGenes("Carrier");
        String patientGender = aPedigreeEditorPage.getGender();

        Assert.assertEquals(loPhenotypesFound, checkPhenotypes);
        Assert.assertEquals(loCandidateGenesFound, checkCandidateGenes);
        Assert.assertEquals(loConfirmedCausalGenesFound, checkConfirmedCausalGenes);
        Assert.assertEquals(loCarrierGenesFound, checkCarrierGenes);
        Assert.assertEquals(patientGender, "Female");

        aPedigreeEditorPage.closeEditor("Save")
            .saveAndViewSummary()
            .editThisPatient()
            .expandSection(SECTIONS.FamilyHistorySection)
            .navigateToPedigreeEditor("")
            .closeEditor("")
            .saveAndViewSummary()
            .logOut();
    }

    // Creates a child for the most recently created patient via the Pedigree editor.
    // Asserts that two new patient nodes are created.
    @Test
    public void createChild()
    {
        aHomePage.navigateToLoginPage()
            .loginAsAdmin()
            .navigateToAllPatientsPage()
            .sortPatientsDateDesc()
            .viewFirstPatientInTable()
            .editThisPatient()
            .expandSection(SECTIONS.FamilyHistorySection)
            .navigateToPedigreeEditor("");
        aPedigreeEditorPage.createChild("male");

        Assert.assertEquals(aPedigreeEditorPage.getNumberOfTotalPatientsInTree(), 3);
        Assert.assertEquals(aPedigreeEditorPage.getNumberOfPartnerLinks(), 1);

        aPedigreeEditorPage.createSibling(3);

        Assert.assertEquals(aPedigreeEditorPage.getNumberOfTotalPatientsInTree(), 4);
        Assert.assertEquals(aPedigreeEditorPage.getNumberOfPartnerLinks(), 1);

        aPedigreeEditorPage.closeEditor("Don't Save")
            .logOut();
    }

    @Test
    public void editorToPatientForm()
    {
        List<String> loPhenotypesToAdd = new ArrayList<>(Arrays.asList("Small hand", "Large knee", "Acne"));

        aHomePage.navigateToLoginPage()
            .loginAsUser()
            .navigateToCreateANewPatientPage()
            .toggleNthConsentBox(1)
            .toggleNthConsentBox(2)
            .toggleNthConsentBox(3)
            .toggleNthConsentBox(4)
            .updateConsent()
            .expandSection(SECTIONS.FamilyHistorySection)
            .navigateToPedigreeEditor("");

        Assert.assertEquals(aPedigreeEditorPage.getNumberOfTotalPatientsInTree(), 1);
        Assert.assertEquals(aPedigreeEditorPage.getNumberOfPartnerLinks(), 0);

        aPedigreeEditorPage.openNthEditModal(1)
            .addPhenotypes(loPhenotypesToAdd)
            .closeEditor("Save")
            .expandSection(SECTIONS.ClinicalSymptomsSection);

        List<String> foundPhenotypesOnPatientForm = aCreatePatientPage.getPresentPhenotypes();

        System.out.println("Before: " + foundPhenotypesOnPatientForm);
        System.out.println("Before loAdding: " + loPhenotypesToAdd);
        // Must sort alphabetical first before comparison, they will be of a different order.
        loPhenotypesToAdd.sort(String::compareTo);
        foundPhenotypesOnPatientForm.sort(String::compareTo);

        Assert.assertEquals(foundPhenotypesOnPatientForm, loPhenotypesToAdd);
        System.out.println("After: " + foundPhenotypesOnPatientForm);
        System.out.println("After loAdding: " + loPhenotypesToAdd);

        aCreatePatientPage
            .saveAndViewSummary()
            .logOut();
    }

    @Test
    public void createNewPatientViaEditor()
    {
        List<String> loPhenotypesToAdd = new ArrayList<>(Arrays.asList("Small hand", "Large knee", "Acne"));

        aHomePage.navigateToLoginPage()
            .loginAsUser()
            .navigateToAllPatientsPage()
            .sortPatientsDateDesc()
            .viewFirstPatientInTable()
            .editThisPatient()
            .expandSection(SECTIONS.FamilyHistorySection)
            .navigateToPedigreeEditor("");

        Assert.assertEquals(aPedigreeEditorPage.getNumberOfTotalPatientsInTree(), 1);
        Assert.assertEquals(aPedigreeEditorPage.getNumberOfPartnerLinks(), 0);

        aPedigreeEditorPage.createSibling(1);

        Assert.assertEquals(aPedigreeEditorPage.getNumberOfTotalPatientsInTree(), 4);
        Assert.assertEquals(aPedigreeEditorPage.getNumberOfPartnerLinks(), 1);

        aPedigreeEditorPage.openNthEditModal(5)
            .linkPatient("New");

        String createdPatient = aPedigreeEditorPage.getPatientIDFromModal();

        aPedigreeEditorPage.addPhenotypes(loPhenotypesToAdd)
            .addGene("Candidate", "LIN7C")
            .closeEditor("Save")
            .saveAndViewSummary()
            .navigateToAllPatientsPage()
            .filterByPatientID(createdPatient)
            .viewFirstPatientInTable()
            .editThisPatient()
            .toggleNthConsentBox(1)
            .toggleNthConsentBox(2)
            .toggleNthConsentBox(3)
            .toggleNthConsentBox(4)
            .updateConsent()
            .expandSection(SECTIONS.ClinicalSymptomsSection);

        List<String> foundPhenotypesFromPatientPage = aCreatePatientPage.getPresentPhenotypes();

        System.out.println("Before: " + foundPhenotypesFromPatientPage);
        System.out.println("Before loAdding: " + loPhenotypesToAdd);
        // Must sort alphabetical first before comparison, they will be of a different order.
        loPhenotypesToAdd.sort(String::compareTo);
        foundPhenotypesFromPatientPage.sort(String::compareTo);

        Assert.assertEquals(foundPhenotypesFromPatientPage, loPhenotypesToAdd);
        System.out.println("After: " + foundPhenotypesFromPatientPage);
        System.out.println("After loAdding: " + loPhenotypesToAdd);

        aCreatePatientPage
            .saveAndViewSummary()
            .logOut();
    }

}
