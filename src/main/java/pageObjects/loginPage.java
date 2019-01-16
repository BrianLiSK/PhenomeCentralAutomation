package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

// Represents the page http://localhost:8083/PhenomeCentral/login
public class loginPage extends BasePage
{
    private final By userNameField = By.id("j_username");

    private final By passField = By.id("j_pasword"); // TODO: There might be a typo there

    private final By loginButton = By.cssSelector("input.button");

    public loginPage(WebDriver aDriver)
    {
        super(aDriver);
    }

    public homePage loginAs(String username, String password)
    {
        clickAndTypeOnElement(userNameField, username);
        clickAndTypeOnElement(passField, password);

        clickOnElement(loginButton);

        return new homePage(superDriver);
    }

    public homePage loginAsAdmin()
    {
        return loginAs(ADMIN_USERNAME, ADMIN_PASS);
    }

    public homePage loginAsUser()
    {
        return loginAs(USER_USERNAME, USER_PASS);
    }

    public homePage loginAsUserTwo()
    {
        return loginAs(USER_USERNAME2, USER_PASS2);
    }
}
