package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

// Represents the http://localhost:8083/AllData page, where Browse -> Browse patients is clicked on
public class allPatientsPage extends BasePage
{
    By importJSONLink = By.id("phenotips_json_import");

    By JSONBox = By.id("import");

    By importBtn = By.id("import_button");

    By sortCreationDate = By.cssSelector("th.xwiki-livetable-display-header-text:nth-child(4) > a:nth-child(1)");

    By firstPatientRowLink = By.cssSelector("#patients-display > tr:nth-child(1) > td:nth-child(1) > a:nth-child(1)");
        // Click twice

    public allPatientsPage(WebDriver aDriver)
    {
        super(aDriver);
    }

    public allPatientsPage importJSONPatient(String theJSON)
    {
        clickOnElement(importJSONLink);
        clickAndTypeOnElement(JSONBox, theJSON);
        clickOnElement(importBtn);
        unconditionalWaitNs(5);
        return this;
    }

    public allPatientsPage sortPatientsDateDesc()
    {
        clickOnElement(sortCreationDate);
        clickOnElement(sortCreationDate);
        unconditionalWaitNs(5);
        return this;
    }

    public viewPatientPage viewFirstPatientInTable()
    {
        clickOnElement(firstPatientRowLink);
        return new viewPatientPage(superDriver);
    }
}
