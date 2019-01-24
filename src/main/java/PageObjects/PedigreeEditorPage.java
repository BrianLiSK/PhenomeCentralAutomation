package PageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * This is the pedigree editor page, i.e. http://localhost:8083/edit/Families/FAMxxxxxxx
 */
public class PedigreeEditorPage extends BasePage
{
    public PedigreeEditorPage(WebDriver aDriver) {
        super(aDriver);

        // Try to click on the default Proband template. If there is no template modal present, catch the error
        //   and just assume that there was no modal in the first place.
        try {
            clickOnElement(probandTemplate);
            waitForElementToBeClickable(hoverBox);
        } catch (TimeoutException e) {
            System.out.println("Seems like we are editing an existing pedigree, no template dialogue found.");
        }
    }

    private final By probandTemplate = By.cssSelector("div[title=Proband]");

    private final By closeEditor = By.id("action-close");
    private final By saveAndQuitBtn = By.id("OK_button");
    private final By dontSaveAndQuitBtn = By.cssSelector("input.button[value=\" Don't save and quit \"]");
    private final By keepEditingPedigreeBtn = By.cssSelector("input.button[value=\" Keep editing pedigree \"]");

    private final By hoverBox = By.cssSelector("#work-area > #canvas > svg > rect.pedigree-hoverbox");
    // Actually, no, maybe we shouldn't have all rectangle classes. I think circles would cause it to break.
    // We should figure out how to traverse up the structure of nodes.
    // It looks like its just a linear list of nodes for now...
    // TODO: Think some more, probably will need JS

    private final By personalTab = By.cssSelector(
        "div.person-node-menu > div.tabholder > dl.tabs > dd:nth-child(1)");
    private final By clinicalTab = By.cssSelector(
        "div.person-node-menu > div.tabholder > dl.tabs > dd:nth-child(2)");
    private final By cancersTab = By.cssSelector(
        "div.person-node-menu > div.tabholder > dl.tabs > dd:nth-child(3)");

    private final By linkPatientBox = By.cssSelector("input.suggest-patients");
    private final By linkPatientFirstSuggestion = By.cssSelector("span.suggestValue"); // First suggestion
    private final By createNewPatientBtn = By.cssSelector("span.patient-create-button");


    private final By maleGenderBtn = By.cssSelector("input[value=M]");
    private final By femaleGenderBtn = By.cssSelector("input[value=F]");
    private final By otherGenderBtn = By.cssSelector("input[value=O]");
    private final By unknownGenderBtn = By.cssSelector("input[value=U]");

    private final By ethnicitiesList = By.cssSelector(
        "div.field-ethnicity > ul.accepted-suggestions > li");
    private final By ethnicitiesBox = By.cssSelector(
        "#tab_Personal > div.field-ethnicity > input[name=ethnicity]");

    private final By dobYearDrp = By.cssSelector(
        "div.field-date_of_birth > div > div > span > select[title=year]");
    private final By dobMonthDrp = By.cssSelector(
      "div.field-date_of_birth > div > div > span > select[title=month]");

    private final By phenotypesList = By.cssSelector(
        "div.field-hpo_positive > ul.accepted-suggestions > li > label.accepted-suggestion > span.value");
    private final By phenotypeBox = By.cssSelector("input.suggest-hpo");

    private final By candidateGeneBox = By.cssSelector(
        "div.field-candidate_genes > input.suggest-genes");
    private final By candidateGeneList = By.cssSelector(
        "div.field-candidate_genes > ul.accepted-suggestions > li > label.accepted-suggestion > span.value");

    private final By causalGeneBox = By.cssSelector(
        "div.field-causal_genes > input.suggest-genes");
    private final By causalGeneList = By.cssSelector(
        "div.field-causal_genes > ul.accepted-suggestions > li > label.accepted-suggestion > span.value");

    private final By carrierGeneBox = By.cssSelector(
        "div.field-carrier_genes > input.suggest-genes");
    private final By carrierGeneList = By.cssSelector(
        "div.field-carrier_genes > ul.accepted-suggestions > li > label.accepted-suggestion > span.value");



    /**
     * Closes the editor and handles the warning dialogue if it appears. Requires that no modals are
     * blocking the pedigree toolbar beforehand (ex. the template selection modal).
     * @param saveChoice is String representing the choice of save.
     *          It must be exactly one of "Save", "Don't Save", "Keep Editing". Defaults to "Save" on invalid string.
     * @return Navigates back to the patient creation page so a return new instance of that.
     */
    public CreatePatientPage closeEditor(String saveChoice)
    {
        clickOnElement(closeEditor);

        if (isElementPresent(saveAndQuitBtn)) {
            switch (saveChoice) {
                case "Save": clickOnElement(saveAndQuitBtn); break;
                case "Don't Save": clickOnElement(dontSaveAndQuitBtn); break;
                case "Keep Editing": clickOnElement(keepEditingPedigreeBtn); break;
                default: System.out.println("Invalid saveChoice in closeEditor, default to Save");
                clickOnElement(saveAndQuitBtn); break;
            }
        }

        waitForElementToBePresent(logOutLink); // We should wait for this to appear.

        return new CreatePatientPage(superDriver);
    }

    /**
     * Switches the tab on the current patient info modal.
     * @param infoTab is one of three Strings: "Personal", "Clinical", "Cancers", each corresponding
     * to a tab on the modal. Upon invalid string entry, goes to the Personal tab.
     * @return stay on the same page so return same object
     */
    public PedigreeEditorPage switchToTab(String infoTab)
    {
        switch (infoTab) {
            case "Personal": clickOnElement(personalTab); waitForElementToBePresent(ethnicitiesBox); break;
            case "Clinical": clickOnElement(clinicalTab); waitForElementToBePresent(phenotypeBox); break;
            case "Cancers": clickOnElement(cancersTab); break; // Should add wait here
            default: clickOnElement(personalTab); waitForElementToBePresent(ethnicitiesBox); break;
        }
        return this;
    }

    /**
     * Returns the current gender that is selected in the radio button options.
     * @return a String representing the gender, one of: "Male", "Female", "Other", "Unknown"
     */
    public String getGender() {
        if (superDriver.findElement(maleGenderBtn).isSelected()) {
            return "Male";
        }
        else if (superDriver.findElement(femaleGenderBtn).isSelected()) {
            return "Female";
        }
        else if (superDriver.findElement(otherGenderBtn).isSelected()) {
            return "Other";
        }
        else {
            return "Unknown";
        }
    }

    /**
     * Retrieves the ethnicities of the patient listed in the pedigree editor in the "Personal" tab.
     * Requires a patient's information modal to be present.
     * @return A, possibly empty, list of Strings representing the ethnicities that were found.
     */
    public List<String> getEthnicities()
    {
        switchToTab("Personal");
        return getLabelsFromList(ethnicitiesList);

    }

    /**
     * Retrieves a list of entered phenotypes within the "Clinical" tab.
     * Requires the patient information modal to be open.
     * @return A, possibly empty, list of Strings containing the names of the phenotypes.
     */
    public List<String> getPhenotypes()
    {
        switchToTab("Clinical");
        return getLabelsFromList(phenotypesList);
    }

    /**
     * Retrieves a list of entered genes from the "Clinical" tab of the specified status.
     * @param status is the gene status so it is one of "Candidate", "Confirmed Causal", "Carrier"
     * @return a List of Strings, possibly empty, representing the text label for each entered gene.
     */
    public List<String> getGenes(String status)
    {
        switchToTab("Clinical");
        switch(status) {
            case "Candidate": return getLabelsFromList(candidateGeneList);
            case "Confirmed Causal": return getLabelsFromList(causalGeneList);
            case "Carrier": return getLabelsFromList(carrierGeneList);
            default: return getLabelsFromList(candidateGeneList);
        }

    }

    /**
     * Opens the edit patient modal for the first patient it can find on the editor.
     * @return stay on the same page so return same object.
     * TODO: Figure out how to traverse and search the possible nodes for a patient,
     *      The js might might make this interesting...
     *      Ideas: Differentiate via width and height. rect.pedigree-hoverbox[width=180, height=243] for
     *      people, rect.pedigree-hoverbox[width=52, height=92] for relationship node.
     */
    public PedigreeEditorPage openEditModal()
    {
        unconditionalWaitNs(5); // Figure out how to wait for animation to finish
        waitForElementToBePresent(hoverBox);
        Actions action = new Actions(superDriver);
        action.click(superDriver.findElement(hoverBox)).build().perform();

        //forceClickOnElement(hoverBox);
        waitForElementToBePresent(personalTab);
        return this;
    }

    /**
     * Links the currently focused node to an existing patient via the "Link to an existing patient record"
     * box in the "Personal" tab.
     * Requires a patient's information modal to be open.
     * @param patientID is either "New" to indicate "Create New" should be clicked
     *          or a patient ID in form of Pxxxxxxx
     * @return Stay on the same page so return same object.
     */
    public PedigreeEditorPage linkPatient(String patientID)
    {
        if (patientID.equals("New")) {
            clickOnElement(createNewPatientBtn);
            waitForElementToBeClickable(personalTab);
        }
        else {
            clickAndTypeOnElement(linkPatientBox, patientID);
            clickOnElement(linkPatientFirstSuggestion);
        }

        return this;
    }

}
