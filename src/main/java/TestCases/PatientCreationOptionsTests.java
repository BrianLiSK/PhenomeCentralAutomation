package TestCases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
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

    private final List<String> checkOnsetLabels = new ArrayList<String>(Arrays.asList(
        "Unknown", "Congenital onset", "Antenatal onset", "Embryonal onset", "Fetal onset", "Neonatal onset",
        "Infantile onset", "Childhood onset", "Juvenile onset", "Adult onset", "Young adult onset",
        "Middle age onset", "Late onset"));

    private final List<String> checkInheritanceLabels = new ArrayList<String>(Arrays.asList(
        "Sporadic", "Autosomal dominant inheritance", "Sex-limited autosomal dominant",
        "Male-limited autosomal dominant", "Autosomal dominant somatic cell mutation",
        "Autosomal dominant contiguous gene syndrome", "Autosomal recessive inheritance",
        "Gonosomal inheritance", "X-linked inheritance", "X-linked dominant inheritance",
        "X-linked recessive inheritance", "Y-linked inheritance", "Multifactorial inheritance",
        "Digenic inheritance", "Oligogenic inheritance", "Polygenic inheritance",
        "Mitochondrial inheritance"));

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
            .setLifeStatus("Deceased");

        for (int i = 1; i <= 12; ++i) {
            if (i < 10) {
                aCreationPage.setDOB("0" + String.valueOf(i), "2019");
                aCreationPage.setDateOfDeath("0" + String.valueOf(i), "2019");
            }
            else {
                aCreationPage.setDOB(String.valueOf(i), "2019");
                aCreationPage.setDateOfDeath(String.valueOf(i), "2019");
            }
        }

        aCreationPage.setLifeStatus("Alive")
            .setGender("Male")
            .setGender("Female")
            .setGender("Other")
            .setGender("Unknown")
            .setGender("Male");

        List<String> loAgeOnsetLabels = aCreationPage.cycleThroughAgeOfOnset();
        List<String> loModeOfInheritanceLabels = aCreationPage.cycleThroughModeOfInheritance();

        Assert.assertEquals(loAgeOnsetLabels, checkOnsetLabels);
        Assert.assertEquals(loModeOfInheritanceLabels, checkInheritanceLabels);

        aCreationPage.cycleThroughModeOfInheritance();
        aCreationPage.setIndicationForReferral("Now cycle through the other sections...")
            .expandSection(SECTIONS.FamilyHistorySection);

        aCreationPage.navigateToPedigreeEditor("")
            .closeEditor("save")
            .logOut();

    }

    @Test
    public void cycleThroughFamilialConditions()
    {
        final List<String> checkFamilialConditionsLabels = new ArrayList<String>(Arrays.asList(
            "Other affected relatives", "Consanguinity", "Parents with at least 3 miscarriages"));

        aHomePage.navigateToLoginPage()
            .loginAsUser()
            .navigateToAllPatientsPage()
            .sortPatientsDateDesc()
            .viewFirstPatientInTable()
            .editThisPatient()
            .expandSection(SECTIONS.FamilyHistorySection);

        List<String> loFamilialConditions = aCreationPage.cycleThroughFamilialHealthConditions();
        Assert.assertEquals(loFamilialConditions, checkFamilialConditionsLabels);

        aCreationPage.logOut();
    }

    @Test
    public void cycleThroughPrenatalHistory()
    {
        final List<String> checkFamilialConditionsLabels = new ArrayList<String>(Arrays.asList(
            "Multiple gestation", "Conception after fertility medication", "Intrauterine insemination (IUI)",
            "In vitro fertilization", "Intra-cytoplasmic sperm injection", "Gestational surrogacy",
            "Donor egg", "Donor sperm", "Hyperemesis gravidarum (excessive vomiting)",
            "Maternal hypertension", "Maternal diabetes", "Maternal fever in pregnancy",
            "Maternal seizures", "Maternal teratogenic exposure", "Toxemia of pregnancy",
            "Abnormal maternal serum screening", "Intrauterine growth retardation",
            "Oligohydramnios", "Polyhydramnios", "Decreased fetal movement",
            "Increased fetal movement", "Premature birth", "Neonatal respiratory distress",
            "Prolonged neonatal jaundice", "Poor suck", "Neonatal hypoglycemia",
            "Neonatal sepsis", "Abnormal delivery (Non-NSVD)", "Small for gestational age (<-2SD)",
            "Large for gestational age (>+2SD)", "Small birth length (<-2SD)", "Large birth length (>+2SD)",
            "Congenital microcephaly (<-3SD)", "Congenital macrocephaly (>+2SD)"));

        aHomePage.navigateToLoginPage()
            .loginAsUser()
            .navigateToAllPatientsPage()
            .filterByPatientID("P0000008") // TODO: Change to someone else, ie, just first patient in table.
            .viewFirstPatientInTable()
            .editThisPatient()
            .expandSection(SECTIONS.PrenatalHistorySection);

        List<String> loPrenatalYesNoBoxes = aCreationPage.cycleThroughPrenatalHistory();
        Assert.assertEquals(loPrenatalYesNoBoxes, checkFamilialConditionsLabels);
        System.out.println(loPrenatalYesNoBoxes);

        aCreationPage.logOut();
    }


}
