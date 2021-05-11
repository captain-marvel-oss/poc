package e2e.drivers;

import com.aventstack.extentreports.Status;
import e2e.framework.Reporter;
import e2e.framework.support.ConfigProp;
import e2e.framework.support.DriverDataHolder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.URL;

class WebBrowserDriver {

    enum BrowserName {
        chrome, safari, ie11, firefox, edge;
    }

    WebDriver getNewDriver() {
        WebDriver driver = null;
        try {
            Boolean isgrid = Boolean.valueOf(ConfigProp.getPropertyValue("runingrid"));

            if (isgrid) {
                driver = getRemoteDriver();
            } else {
                driver = getLocalDriver();
            }

            if (!DriverDataHolder.getValue("Res_Width").isEmpty()
                    && !DriverDataHolder.getValue("Res_Height").isEmpty()) {
                driver.manage().window().setSize(new Dimension(Integer.parseInt(DriverDataHolder.getValue("Res_Width")),
                        Integer.parseInt(DriverDataHolder.getValue("Res_Height"))));
                /*
                 * Reporter.assignCategory(Integer.parseInt(DriverDataHolder.getValue(
                 * "Res_Width")) + "x" +
                 * Integer.parseInt(DriverDataHolder.getValue("Res_Height")));
                 */
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Reporter.log(Status.INFO, "Error launching new Desktop Driver");
            e.printStackTrace();
            Reporter.log(Status.FATAL, e.getLocalizedMessage() + "\n" + e.getMessage());
        }
        return driver;
    }

    private WebDriver getLocalDriver() {
        // BrowserName browser =
        // BrowserName.valueOf(ConfigProp.getPropertyValue("browser").toLowerCase());
        BrowserName browser = BrowserName.valueOf(DriverDataHolder.getValue("Browser").toLowerCase());
        DesiredCapabilities cap = new DesiredCapabilities();
        WebDriver driver = null;
        switch (browser) {
            case chrome:
            	//adding driver location
            	System.setProperty("webdriver.chrome.driver", "D:/chromedriver_win32/chromedriver.exe");
                ChromeOptions options = new ChromeOptions();
               // options.addArguments("--headless");
                driver = new ChromeDriver(options);
                break;
            case safari:
                driver = new SafariDriver();
                break;
        }
        return driver;
    }

    private RemoteWebDriver getRemoteDriver() {
        // BrowserName browser =
        // BrowserName.valueOf(ConfigProp.getPropertyValue("browser").toLowerCase());
        BrowserName browser = BrowserName.valueOf(DriverDataHolder.getValue("Browser").toLowerCase());
        DesiredCapabilities cap = new DesiredCapabilities();
        RemoteWebDriver remotedriver = null;
        switch (browser) {
            case chrome:
                cap.setBrowserName("chrome");
                cap.setPlatform(Platform.MAC);
                // cap.setVersion("70");
                break;

            case safari:
                cap.setBrowserName("safari");
                break;

            case edge:
                cap.setBrowserName("MicrosoftEdge");
                cap.setJavascriptEnabled(true);
                cap.setAcceptInsecureCerts(true);
                break;
        }
        try {
            remotedriver = new RemoteWebDriver(new URL("http://" + ConfigProp.getPropertyValue("gridhost") + ":"
                    + ConfigProp.getPropertyValue("deskport") + "/wd/hub"), cap);
        } catch (Exception e) {
            Reporter.log(Status.FATAL, "Unable to start Remote Web Driver" + e.getMessage());
            Reporter.log(Status.ERROR, e.getMessage());
        }
        return remotedriver;
    }
}
