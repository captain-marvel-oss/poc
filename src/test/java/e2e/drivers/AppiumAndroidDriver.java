package e2e.drivers;

import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableMap;

import e2e.framework.Reporter;
import e2e.framework.support.ConfigProp;
import e2e.framework.support.DriverDataHolder;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class AppiumAndroidDriver {

    public WebDriver getNewDriver() {
        WebDriver remoteDriver = null;

        try {
        	
          //	System.setProperty("webdriver.chrome.driver", "D:/Softwares/chromedriver_win32/chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");

          /*   AppiumDriverLocalService service=null;

            service = AppiumDriverLocalService.buildDefaultService();*/
//            DesiredCapabilities capabilities = new DesiredCapabilities();

            /*options.setCapability("newCommandTimeout", 600);
            options.setCapability("launchTimeout", 90000);
            options.setCapability("deviceName", "8c0435a9");
            options.setCapability("platformVersion", "10.0");
            options.setCapability("udid", "8c0435a9");
            options.setCapability("platformName", "Android");
            options.setCapability("automationName", "uiautomator2");
            options.setCapability("systemPort", "9900");*/
            
            options.setCapability("deviceName", "Android Emulator");
            options.setCapability("udid", "emulator-5554");
            options.setCapability("platformName", "Android");
            options.setCapability("platformVersion", "9.0");
           // options.setCapability("appPackage", "com.android.chrome");
           // options.setCapability("appActivity", "com.google.android.apps.chrome.Main");
            
            //capabilities.setBrowserName("Chrome");
            options.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
            //remoteDriver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:4723/wd/hub"), options);

            remoteDriver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), options);
        } catch (Exception e) {
            e.printStackTrace();
            Reporter.log(Status.FATAL, "Unable to Launch Android Driver");
            Reporter.log(Status.ERROR, e.getMessage());

        }

        return remoteDriver;
    }
}
