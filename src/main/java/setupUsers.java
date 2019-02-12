import org.testng.annotations.Test;

import PageObjects.HomePage;
import TestCases.BaseTest;

public class setupUsers extends BaseTest
{
    HomePage aHomePage = new HomePage(theDriver);

    // Creates the two users used by the automation.
    @Test(priority = 1)
    public void setupAutomationUsers()
    {
        aHomePage.navigateToLoginPage()
            .loginAsAdmin()
            .navigateToAdminSettingsPage()
            .navigateToAdminUsersPage()
            .addUser("TestUser1" , "Uno", "123456",
                "testuser1uno@jksjfljsdlfj.caksjdfjlkg", "none",
                "Test server", "Some reason")
            .addUser("TestUser2" , "Dos", "123456",
                "testuser2dos@kljaskljdfljlfd.casdfjjg", "none",
                "Test server", "Some reason")
            .navigateToPendingUsersPage()
            .approvePendingUser("TestUser1Uno")
            .approvePendingUser("TestUser2Dos")
            .logOut()
            .loginAsUser()
            .logOut()
            .loginAsUserTwo()
            .logOut();
        System.out.println("Created Two users for automation");
    }

    @Test(priority = 2)
    public void setEmailPort()
    {
        aHomePage.navigateToLoginPage().loginAsAdmin().navigateToAdminSettingsPage()
            .navigateToMailSendingSettingsPage()
            .setEmailPort(1025)
            .navigateToHomePage()
            .logOut();
    }
}
