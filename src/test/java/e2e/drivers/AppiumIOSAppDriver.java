package e2e.drivers;

import e2e.framework.support.ConfigProp;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class AppiumIOSAppDriver {

    public static WebDriver getNewDriver() {
        WebDriver remoteDriver = null;

        System.out.println("NEW DRiver iOS APp");
        try {
            Boolean isgrid = Boolean.valueOf(ConfigProp.getPropertyValue("runingrid"));

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Sempra iPhone");
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
            capabilities.setCapability("platformVersion", "12.0.1");
            capabilities.setCapability(MobileCapabilityType.APP, "com.aa.AmericanAirlines");
            capabilities.setCapability(MobileCapabilityType.UDID, "73af531d62e55f2d9d3c5b48da3c0a22aee4ed18");
            capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
            capabilities.setCapability("usePrebuiltWDA", true);
            capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
            capabilities.setCapability("newCommandTimeout", "720");
            capabilities.setCapability("wdaConnectionTimeout", "720000");

            if (isgrid)
                remoteDriver = new IOSDriver(
                        new URL(
                                "http://" +
                                        ConfigProp.getPropertyValue("gridhost") +
                                        ":" +
                                        ConfigProp.getPropertyValue("mobport") +
                                        "/wd/hub"),
                        capabilities
                );
            else
                remoteDriver = new IOSDriver(new URL("http://127.0.0.1:4727/wd/hub"), capabilities);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return remoteDriver;
    }
}
