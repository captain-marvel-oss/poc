package e2e.pagemethods;

import e2e.drivers.DriverFactory;
import e2e.framework.support.ConfigProp;
import e2e.libraries.ObjectLookup;
import e2e.libraries.WebObj;
import e2e.libraries.Wrapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TraHomePage extends BasePage{

    private final String pageName = "duHomePage";
    private final String enURL = ConfigProp.getPropertyValue("url");
    private final String aeURL = ConfigProp.getPropertyValue("aeurl");

    private final WebObj devicesNavLink = getLocatorValue("devicesNavLink", pageName);
    private final WebObj lifeStyleNavLink = getLocatorValue("lifeStyleNavLink", pageName);
    private final WebObj hamburgerMenu = getLocatorValue("hamburgerMenu", pageName);
    private final WebObj device_tabMobile = getLocatorValue("device_tabMobile", pageName);
    private final WebObj device_tabTablet = getLocatorValue("device_tabTablet", pageName);
    private final WebObj device_tabAccessories = getLocatorValue("device_tabAccessories", pageName);
    private final WebObj device_tabBroadband = getLocatorValue("device_tabBroadband", pageName);
    private final WebObj cardTitle = getLocatorValue("cardTitle", pageName);
    private final WebObj rebrand = getLocatorValue("rebrand", pageName);

    private final WebObj traInNumLink = getLocatorValue("traInNumLink", pageName);
    private final WebObj marInfoOption = getLocatorValue("marInfoOption", pageName);
    private final WebObj marInfoSubOption = getLocatorValue("marInfoSubOption", pageName);
    private final WebObj preloader = getLocatorValue("preloader", pageName);

    private final WebObj search = getLocatorValue("search", pageName);
    private final WebObj search_key = getLocatorValue("search_key", pageName);
    private final WebObj search_options = getLocatorValue("search_options", pageName);

    public void loadHomePage() {
        String testUrl = enURL;
        Wrapper.launchbaseURL(testUrl);
        if (ObjectLookup.currentLocDriver().equalsIgnoreCase("mobileweb")) {
            waitFor500ms();
            Wrapper.launchbaseURL(testUrl);
        }
        Wrapper.waitForPageLoad();
        Wrapper.waitForElementInvisibility(preloader);
        Wrapper.waitForPageLoad();
    }

    public void navigateToDeviceSection() {
        Wrapper.waitForPageLoad();
        Wrapper.waitForElementVisibility(devicesNavLink);
        Wrapper.clickElement(devicesNavLink);
    }

    public void selectSubsection(String subsection) {
        WebObj element;
        switch (subsection) {
            case "mobile":
                element = device_tabMobile;
                break;
            case "tablet":
                element = device_tabTablet;
                break;
            case "accessories":
                element = device_tabAccessories;
                break;
            case "broadband":
                element = device_tabBroadband;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + subsection);
        }
        Wrapper.waitForElementVisibility(element);
        Wrapper.clickElement(element);
    }

    public void validateDeviceInSubsection(String subsection, String device) {
        Wrapper.waitForPageLoad();
        Wrapper.intentionalSleep(5);
        WebDriver driver = DriverFactory.getDriver();
        By by = By.xpath("//*[text()='"+device+"']");
        driver.findElement(by);
    }

    public void navigateToLifeStyleSection() {
        Wrapper.waitForPageLoad();
        if (ObjectLookup.currentLocDriver().equalsIgnoreCase("mobileweb"))
            Wrapper.clickElement(hamburgerMenu);
        Wrapper.waitForElementVisibility(lifeStyleNavLink);
        Wrapper.clickElement(lifeStyleNavLink);
    }

    public void validateLifeStyleSection() {
        Wrapper.waitForElementVisibility(rebrand);
    }


    public void navigateToTRAInNumbers() {
        if (ObjectLookup.currentLocDriver().equalsIgnoreCase("mobileweb"))
            Wrapper.scrolltoPageMid();
        else {
            Wrapper.waitForElementVisibility(traInNumLink);
            Wrapper.clickElement(traInNumLink);
        }
        waitFor500ms();
    }

    private void waitFor500ms() {
        try {
            Thread.sleep(250);
        } catch (Exception ignored) {}
    }

    public void selectMarketInfoOption() {
        Wrapper.waitForElementVisibility(marInfoOption);
        Wrapper.scroll_Into_Element(marInfoOption);
        Wrapper.clickElement(marInfoOption);
        waitFor500ms();
    }

    public void markInfoSubOption() {
        Wrapper.waitForElementVisibility(marInfoSubOption);
    }

    public void clickOnSearchIcon() {
        Wrapper.waitForElementVisibility(search);
        Wrapper.clickElement(search);
    }

    public void validateSearchOption() {
        Wrapper.waitForElementVisibility(search_options);
    }

    public void enterSearchText() {
        Wrapper.waitForElementVisibility(search_key);
        Wrapper.enter_Text(search_key, "FEDNet");
        Wrapper.intentionalSleep(5);
    }
}
