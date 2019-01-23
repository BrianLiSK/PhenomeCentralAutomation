package PageObjects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PedigreeEditorPage extends BasePage
{
    public PedigreeEditorPage(WebDriver aDriver) {
        super(aDriver);

        // Try to click on the default Proband template. If there is no template modal present, catch the error
        //   and just assume that there was no modal in the first place.
        try {
            clickOnElement(probandTemplate);
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
            case "Personal": clickOnElement(personalTab); break;
            case "Clinical": clickOnElement(clinicalTab); break;
            case "Cancers": clickOnElement(cancersTab); break;
            default: clickOnElement(personalTab); break;
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
     * Retrieves the ethnicities of the patient listed in the pedigree editor.
     * @return
     */
    public List<String> getEthnicities()
    {
        List<String> loEthnicities = new ArrayList<>();
        List<WebElement> loFoundEthnicities = superDriver.findElements(ethnicitiesList);

        for (WebElement e : loFoundEthnicities) {
            loEthnicities.add(e.getText());
        }

        return loEthnicities;

    }

}
