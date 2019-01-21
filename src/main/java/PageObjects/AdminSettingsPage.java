package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This is the main Global Administrator settings page. Reached by clicking on "Administrator" (gear icon)
 * link on the top left of the navbar. Ex. http://localhost:8083/admin/XWiki/XWikiPreferences
 */
public class AdminSettingsPage extends BasePage
{
    private final By matchingNotificationMenu = By.id("vertical-menu-Matching Notification");

    private final By refreshMatchesMenu = By.id("vertical-menu-Refresh matches");

    public AdminSettingsPage(WebDriver aDriver)
    {
        super(aDriver);
    }

    /**
     * Navigates to the "Matching Notification" page. On the left accordion menu:
     * PhenoTips -> Matching Notification
     * @return a MatchNotification page object.
     */
    public AdminMatchNotificationPage navigateToMatchingNotificationPage()
    {
        clickOnElement(matchingNotificationMenu);
        return new AdminMatchNotificationPage(superDriver);
    }

    /**
     * Navigates to the "Refresh matches" page. On the left accordion menu:
     * PhenoTips -> Refresh matches
     * @return a AdminRefreshMatches page object as we navigate there.
     */
    public AdminRefreshMatchesPage navigateToRefreshMatchesPage()
    {
        clickOnElement(refreshMatchesMenu);
        return new AdminRefreshMatchesPage(superDriver);
    }
}
