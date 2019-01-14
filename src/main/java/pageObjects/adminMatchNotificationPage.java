package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class adminMatchNotificationPage extends basePage {
    public adminMatchNotificationPage(WebDriver aDriver) { super(aDriver); }

    By patientIDContainsBox = By.id("external-id-filter");
    By reloadMatchesBtn = By.id("show-matches-button");

    public adminMatchNotificationPage filterByID(String identifier) {
        clickAndTypeOnElement(patientIDContainsBox, identifier);
        clickOnElement(reloadMatchesBtn);
        unconditionalWaitNs(2);
        return this;
    }

}
