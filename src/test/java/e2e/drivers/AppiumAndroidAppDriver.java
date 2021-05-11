package e2e.drivers;

import com.aventstack.extentreports.Status;
import e2e.framework.Reporter;
import e2e.framework.support.ConfigProp;
import e2e.framework.support.DriverDataHolder;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;

public class AppiumAndroidAppDriver {

	public WebDriver getNewDriver() {
		WebDriver remoteDriver = null;

		try {

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("pCloudy_Username", "manjunath.p@infosys.com");
			capabilities.setCapability("pCloudy_ApiKey", "8h32npsym8pjnb6cs3nhvgsq");
			capabilities.setCapability("pCloudy_DurationInMinutes", 10);
			capabilities.setCapability("newCommandTimeout", 600);
			capabilities.setCapability("launchTimeout", 90000);
			capabilities.setCapability("pCloudy_DeviceFullName", "SAMSUNG_GalaxyJ7Pro_Android_8.1.0");
			capabilities.setCapability("platformVersion", "8.1.0");
			capabilities.setCapability("pCloudy_DeviceVersion", "8.1.0");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("automationName", "uiautomator2");
			capabilities.setCapability("pCloudy_ApplicationName", "pocappvNew.apk");
			capabilities.setCapability("appPackage", "tsel.www.myapplication");
			capabilities.setCapability("appActivity", "MainActivity");
			capabilities.setCapability("pCloudy_WildNet", "false");
			remoteDriver = new AndroidDriver<WebElement>(new URL("https://telkomsel.pcloudy.com/appiumcloud/wd/hub"),
					capabilities);

			// remoteDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"),
			// capabilities);
			// ((AndroidDriver) remoteDriver).launchApp();

		} catch (Exception e) {
			e.printStackTrace();
			Reporter.log(Status.ERROR, e.getMessage());
			Reporter.log(Status.FATAL, "Unable to Launch Android Driver");
		}

		return remoteDriver;
	}
}
