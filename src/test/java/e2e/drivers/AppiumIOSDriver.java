package e2e.drivers;

import e2e.framework.support.ConfigProp;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class AppiumIOSDriver {

    public WebDriver getNewDriver() {
        WebDriver remoteDriver = null;

        try {
            Boolean isgrid = Boolean.valueOf(ConfigProp.getPropertyValue("runingrid"));

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Sempra iPhone");
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
            capabilities.setCapability("platformVersion", "13.7");
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "safari");
            capabilities.setCapability(MobileCapabilityType.UDID, "36E044BE-0913-49F1-9EF9-C1398B221DC5");
            capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
            capabilities.setCapability("usePrebuiltWDA", true);
            capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
            capabilities.setCapability("newCommandTimeout", "720");
            capabilities.setCapability("wdaConnectionTimeout", "720000");

            if (isgrid)
                remoteDriver = new IOSDriver(new URL("http://" + ConfigProp.getPropertyValue("gridhost") + ":"
                        + ConfigProp.getPropertyValue("mobport") + "/wd/hub"), capabilities);
            else
                remoteDriver = new IOSDriver(new URL("http://127.0.0.1:4725/wd/hub"), capabilities);
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return remoteDriver;
    }
}
