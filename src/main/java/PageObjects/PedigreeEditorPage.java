package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

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

        return new CreatePatientPage(superDriver);
    }

}
