package e2e.pagemethods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.Status;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import e2e.drivers.DriverFactory;
import e2e.framework.DatabaseConnection;
import e2e.framework.Reporter;
import e2e.framework.support.ConfigProp;
import e2e.libraries.ObjectLookup;
import e2e.libraries.WebObj;
import e2e.libraries.Wrapper;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;

public class SingtelHomePage extends BasePage {

    private String pageName = "pocaaHomePage";

    // POC log in locators
    private WebObj searchIcon = getLocatorValue("searchIcon", pageName);
    private WebObj hamburgerMenu = getLocatorValue("hamburgerMenu", pageName);
    private WebObj searchField = getLocatorValue("searchField", pageName);
    private WebObj searchResult = getLocatorValue("searchResult", pageName);
    private WebObj searchResultPageHeader = getLocatorValue("searchResultPageHeader", pageName);
    private WebObj shopButton = getLocatorValue("shopButton", pageName);
    private WebObj brandFilter = getLocatorValue("brandFilter", pageName);
    private WebObj dealFilter = getLocatorValue("dealFilter", pageName);
    private WebObj productNameSearch = getLocatorValue("productNameSearch", pageName);
    private WebObj productPriceSearch = getLocatorValue("productPriceSearch", pageName);
    private WebObj selectButtonSearch = getLocatorValue("selectButtonSearch", pageName);
    private WebObj productHeader = getLocatorValue("productHeader", pageName);
    private WebObj productColor = getLocatorValue("productColor", pageName);
    private WebObj productStorage = getLocatorValue("productStorage", pageName);
    private WebObj nextButton = getLocatorValue("nextButton", pageName);
    private WebObj postPaidPlan = getLocatorValue("postPaidPlan", pageName);
    private WebObj preRegisterButton = getLocatorValue("preRegisterButton", pageName);
    private WebObj preRegisterationHeader = getLocatorValue("preRegisterationHeader", pageName);
    private WebObj personalText = getLocatorValue("personalText", pageName);
    private WebObj personalRadioButton = getLocatorValue("personalRadioButton", pageName);
    private WebObj nextButtonRegisterPage = getLocatorValue("nextButtonRegisterPage", pageName);
    private WebObj nricTextBox = getLocatorValue("nricTextBox", pageName);

    public void clickSearchIcon() {
        Wrapper.waitForPageLoad();
        if (ObjectLookup.currentLocDriver().equalsIgnoreCase("mobileweb")) {
            Wrapper.waitForPageLoad();
            Wrapper.intentionalSleepSafari(5);
            Wrapper.verifyElement(hamburgerMenu);
            Wrapper.clickWithCoordinates(hamburgerMenu);

        } else {
            Wrapper.verifyElement(searchIcon);
            Wrapper.clickElement(searchIcon);
        }

    }

    public void enterSearchText() {
        String searchText = ConfigProp.getPropertyValue("searchText");
        Wrapper.verifyElement(searchField);
        Wrapper.enter_Text(searchField, searchText);
        List<WebElement> searchResultList = Wrapper.findAllElements(searchResult);
        for (WebElement e : searchResultList) {
            Wrapper.assertTextContainsIgnoreCase(e, "iphone", "search text in search suggestions");
        }
        Wrapper.clickElement(searchResultList.get(0), "First search term in search results");
        Wrapper.intentionalSleep(5);
    }

    public void verifySearchResultsPage() {
        Wrapper.verifyElement(preRegisterButton);
        Wrapper.clickElement(preRegisterButton);
        Wrapper.verifyElement(preRegisterationHeader);
        Wrapper.verifyElement(personalText);
        Wrapper.clickElement(nextButtonRegisterPage);
        Wrapper.verifyElement(nricTextBox);
    }

    public void clickShopButton() {
        Wrapper.verifyElement(shopButton);
        Wrapper.clickElement(shopButton);
    }

    public void selectFilter() {
        Wrapper.intentionalSleep(5);
        Wrapper.selectText(brandFilter, "Apple");
        Wrapper.selectText(dealFilter, "Latest iPhone");
        Wrapper.verifyElement(productNameSearch);
        Wrapper.verifyElement(productPriceSearch);
        Wrapper.scrolltoPageQuat();
        Wrapper.clickElement(selectButtonSearch);
    }

    public void verifyProductDetails() {
        Wrapper.verifyElement(productHeader);
        Wrapper.verifyElement(productColor);
        Wrapper.clickElement(productColor);
        Wrapper.verifyElement(productStorage);
        Wrapper.clickElement(productStorage);
        Wrapper.verifyElement(nextButton);
        Wrapper.clickElement(nextButton);
        Wrapper.verifyElement(postPaidPlan);

    }

}
