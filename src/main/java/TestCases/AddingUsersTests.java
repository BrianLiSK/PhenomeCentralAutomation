package TestCases;

import org.testng.annotations.Test;

import PageObjects.HomePage;
import net.bytebuddy.utility.RandomString;

/**
 * Test case for admin adding a new user, will expand later.
 */
public class AddingUsersTests extends BaseTest
{
    final private String randomChars = RandomString.make(5);

    final HomePage aHomePage = new HomePage(theDriver);

    @Test
    public void adminAddAndApproveUser()
    {
        aHomePage.navigateToLoginPage().loginAsAdmin()
            .navigateToAdminSettingsPage().navigateToAdminUsersPage()
            .addUser("AutoAdded1" + randomChars, "AutoAdded1Last", "123456",
                "AutoAdded1@somethingsomething.cjasdfj", "someaffilation",
                "some referrer", "some reason")
            .navigateToPendingUsersPage()
            .approveNthPendingUser(1);

        aHomePage.logOut()
            .loginAs("AutoAdded1" + randomChars + "AutoAdded1Last", "123456")
            .navigateToAllPatientsPage();

        aHomePage.logOut();
    }

    // Creates the two users used by the automation.
    @Test(enabled = false)
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
//        aHomePage.unconditionalWaitNs(15);
    }

}
