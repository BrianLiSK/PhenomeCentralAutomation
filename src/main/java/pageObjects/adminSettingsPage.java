package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class adminSettingsPage extends BasePage
{
    By matchingNotificationMenu = By.id("vertical-menu-Matching Notification");

    public adminSettingsPage(WebDriver aDriver)
    {
        super(aDriver);
    }

    public AdminMatchNotificationPage navigateToMatchingNotificationPage()
    {
        clickOnElement(matchingNotificationMenu);
        return new AdminMatchNotificationPage(superDriver);
    }
}
