package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This page corresponds to the Users page from Administrator Settings. We can add users here.
 * I.e. http://localhost:8083/admin/XWiki/XWikiPreferences?section=Users
 */
public class AdminUsersPage extends AdminSettingsPage
{
    private final By newUserBtn = By.id("addNewUser");
    private final By firstNameBox = By.id("register_first_name");
    private final By lastNameBox = By.id("register_last_name");
    private final By userNameBox = By.id("xwikiname");
    private final By passwordBox = By.id("register_password");
    private final By confirmPasswordBox = By.id("register2_password");
    private final By emailBox = By.id("register_email");
    private final By affiliationBox = By.id("register_affiliation");
    private final By referralBox = By.id("register_referral"); // "How did you hear about / Who referred you to PhenomeCentral?"
    private final By reasoningBox = By.id("register_comment"); // Why are you requesting access to PhenomeCentral?

    // Checkboxes
    private final By professionalCheckbox = By.id("confirmation_clinician");
    private final By liabilityCheckbox = By.id("confirmation_research");
    private final By nonIdentificationCheckbox = By.id("confirmation_identify");
    private final By cooperationCheckbox = By.id("confirmation_publication");
    private final By acknoledgementCheckbox = By.id("confirmation_funding");

    private final By saveBtn = By.cssSelector("input[value='Save']");
    private final By cancelBtn = By.cssSelector("input[value='Cancel changes since last save']");

    public AdminUsersPage(WebDriver aDriver)
    {
        super(aDriver);
    }

    /**
     * Adds a user to the PC instance. Only adds it to the Users table, does not confirm and authorize the user.
     * Uses the default username that is suggested by XWiki.
     * @param firstName First Name as a String.
     * @param lastName Last name as a String.
     * @param password is password as a String.
     * @param email is email for the user as a String. Should be either a dummy address or something that we can access.
     * @param affiliation is the value for the Affiliation box as a String.
     * @param referral value for the "How did you hear about/  Who referred you" box as a String.
     * @param justification value for the "Why are you requesting access" box as a String.
     * @return Stay on the same page so return the same object.
     */
    public AdminUsersPage addUser(String firstName, String lastName, String password,
        String email, String affiliation, String referral, String justification)
    {
        clickOnElement(newUserBtn);
        clickAndTypeOnElement(firstNameBox, firstName);
        clickAndTypeOnElement(lastNameBox, lastName);
        clickOnElement(userNameBox);
        clickAndTypeOnElement(passwordBox, password);
        clickAndTypeOnElement(confirmPasswordBox, password);
        clickAndTypeOnElement(emailBox, email);
        clickAndTypeOnElement(affiliationBox, affiliation);
        clickAndTypeOnElement(referralBox, referral);
        clickAndTypeOnElement(reasoningBox, justification);

        clickOnElement(professionalCheckbox);
        clickOnElement(liabilityCheckbox);
        clickOnElement(nonIdentificationCheckbox);
        clickOnElement(cooperationCheckbox);
        clickOnElement(acknoledgementCheckbox);

        clickOnElement(saveBtn);
        return this;
    }


}
