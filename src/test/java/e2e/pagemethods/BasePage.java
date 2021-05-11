package e2e.pagemethods;

import com.aventstack.extentreports.Status;
import e2e.drivers.DriverFactory;
import e2e.framework.Reporter;
import e2e.framework.support.ConfigProp;
import e2e.libraries.ObjectLookup;
import e2e.libraries.WebObj;
import e2e.libraries.Wrapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage extends ObjectLookup {

    private String pageName = "base";


    void waitElemLoad(WebObj webObj) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            WebDriverWait wait = new WebDriverWait(driver, 180);
            wait.until(ExpectedConditions.visibilityOfElementLocated(webObj.by));
            //Wrapper.getAttribute(webObj, "class");
            Wrapper.intentionalSleep(1);
            System.out.println("Waiting for page load to complete: " + Wrapper.getAttribute(webObj, "class"));
            wait.until(driver1 -> !(Wrapper.getAttribute(webObj, "class").contains("p_AFBusy")));
            System.out.println("After Wait: " + Wrapper.getAttribute(webObj, "class"));
        } catch (Exception e) {
            e.printStackTrace();
            Reporter.log(Status.ERROR, "Page taking too long to load");
        }
    }

    public void launchURL() {
        try {
            String url;
            if (currentLocDriver().equalsIgnoreCase("iosapp") ||
                    currentLocDriver().equalsIgnoreCase("andapp")) {
                Wrapper.sendAppBackground();
            } else {
                url = ConfigProp.getPropertyValue("url");
                Wrapper.launchbaseURL(url);
            }
        } catch (Exception E) {
            E.printStackTrace();
            Reporter.log(Status.ERROR, "unable to launch the URL/App: Error - " + E.getMessage());
        }
    }

}
