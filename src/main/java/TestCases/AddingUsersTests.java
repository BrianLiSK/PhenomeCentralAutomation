package TestCases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import PageObjects.HomePage;
import PageObjects.UserSignUpPage;
import net.bytebuddy.utility.RandomString;

/**
 * Test case for admin adding a new user, will expand later.
 */
public class AddingUsersTests extends BaseTest
{
    final private String randomChars = RandomString.make(5);

    final HomePage aHomePage = new HomePage(theDriver);

    final UserSignUpPage aUserSignUpPage = new UserSignUpPage(theDriver);

    final String confirmationMessageCheck = "Thank you for your interest in PhenomeCentral. We took note of your request and we will process it shortly.";
    final String pendingApprovalMessageCheck = "Please wait for your account to be approved. Thank you.";

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

    @Test
    public void userSignUpNotApproved()
    {
        aHomePage.navigateToSignUpPage()
            .requestAccount("PublicSignUp" + randomChars , "Auto" + randomChars, "123456",
            "PublicSignUp" + randomChars + "@akjsjdf.cjsjdfn", "none",
            "Test server", "Some reason");

        Assert.assertEquals(aUserSignUpPage.getConfirmationMessage(), confirmationMessageCheck);
        System.out.println("Request Recieved Msg: " + aUserSignUpPage.getConfirmationMessage());

        aHomePage.navigateToLoginPage()
            .loginAs("PublicSignUp" + randomChars + "Auto" + randomChars, "123456");

        System.out.println("Approval Pending Msg: " + aHomePage.getApprovalPendingMessage());
        Assert.assertEquals(aHomePage.getApprovalPendingMessage(), pendingApprovalMessageCheck);
        Assert.assertEquals(aHomePage.navigateToAllPatientsPage().getApprovalPendingMessage(), pendingApprovalMessageCheck);
        Assert.assertEquals(aHomePage.navigateToCreateANewPatientPage().getApprovalPendingMessage(), pendingApprovalMessageCheck);

        aHomePage.logOut();

    }

    @Test
    public void userSignUpApproved()
    {
        String username = "PublicSignUp" + randomChars + "Auto" + randomChars;
        List<String> loSectionTitlesCheck = new ArrayList<>(Arrays.asList("MY MATCHES", "MY PATIENTS\n ",
            "PATIENTS SHARED WITH ME\n ", "MY GROUPS\n ", "PUBLIC DATA\n "));

        aHomePage.navigateToSignUpPage()
            .requestAccount("PublicSignUp" + randomChars , "Auto" + randomChars, "123456",
                "PublicSignUp" + randomChars + "@akjsjdf.cjsjdfn", "none",
                "Test server", "Some reason");

        Assert.assertEquals(aUserSignUpPage.getConfirmationMessage(), confirmationMessageCheck);

        aHomePage.navigateToLoginPage()
            .loginAs(username, "123456");

        System.out.println("Approval Pending Msg: " + aHomePage.getApprovalPendingMessage());
        Assert.assertEquals(aHomePage.getApprovalPendingMessage(), pendingApprovalMessageCheck);

        aHomePage.logOut();
        aHomePage.navigateToLoginPage()
            .loginAsAdmin()
            .navigateToAdminSettingsPage()
            .navigateToPendingUsersPage()
            .approvePendingUser(username)
            .logOut()
            .loginAs(username, "123456")
            .navigateToHomePage();

        System.out.println("Titles Found: " + aHomePage.getSectionTitles());
        Assert.assertEquals(aHomePage.getSectionTitles(), loSectionTitlesCheck);

    }

}
