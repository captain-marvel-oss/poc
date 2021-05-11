package e2e.drivers;

import e2e.framework.Reporter;
import e2e.framework.support.DriverDataHolder;
import e2e.framework.support.TestDataHolder;
import io.qameta.allure.Step;

import org.openqa.selenium.WebDriver;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driverinstance = new ThreadLocal<>();

    enum DriverName {
        android, ios, web, win, mac, android_app, ios_app
    }

    private static String getPlatformName() {
        return DriverDataHolder.getValue("OS").toLowerCase();
    }

    private static String getBrowserName() {
        if (DriverDataHolder.getValue("Platform").toLowerCase().equals("mobile"))
            return DriverDataHolder.getValue("OS").toLowerCase() + "_"
                    + DriverDataHolder.getValue("Browser").toLowerCase();
        else
            return DriverDataHolder.getValue("Browser").toLowerCase();
    }

    @Step("GET DRIVER")
    public static WebDriver getDriver() {
        WebDriver driver = driverinstance.get();
        // System.out.println("DRIVER: " + driver);
        if (driver == null) { // || driver.toString().contains("null")) {
            // System.out.println("Initiating new driver");
            initDriver();
        }
        return driver;
    }

    private static void initDriver() {

        DriverName d = DriverName.valueOf(getPlatformName().toLowerCase());

        System.out.println("INITIATING DRIVER");
        switch (d) {
            case android:
                System.out.println("INITIATING ANDROID DRIVER");
                Reporter.assignCategory("Device_ID", DriverDataHolder.getValue("Device_ID"));
                if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("app"))
                    driverinstance.set(new AppiumAndroidAppDriver().getNewDriver());
                else
                    driverinstance.set(new AppiumAndroidDriver().getNewDriver());
                break;
            case ios:
                System.out.println("INITIATING iOS DRIVER");
                Reporter.assignCategory("Device_ID", DriverDataHolder.getValue("Device_ID"));
                if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("app"))
                    driverinstance.set(new AppiumIOSAppDriver().getNewDriver());
                else
                    driverinstance.set(new AppiumIOSDriver().getNewDriver());
                break;
            case web:
            case mac:
            case win:
                System.out.println("INITIATING WEB DRIVER");
                Reporter.assignCategory("BrowserName", getBrowserName());
                driverinstance.set(new WebBrowserDriver().getNewDriver());
                break;
            default:
                System.out.println("Driver Details not defined");
                System.out.println("INITIATING WEB DRIVER");
                Reporter.assignCategory("BrowserName", getBrowserName());
                driverinstance.set(new WebBrowserDriver().getNewDriver());
                break;
        }
    }

    public static void closeDriver() {
        WebDriver driver = driverinstance.get();
        System.out.println("Closing Driver");
        boolean check = (DriverDataHolder.getValue("Browser").equalsIgnoreCase("safari")
                && DriverDataHolder.getValue("Platform").equalsIgnoreCase("desktop"))
                || DriverDataHolder.getValue("Browser").equalsIgnoreCase("app");
        try {
            // driver.quit();
            if (check)
                driver.quit();
            else {
                driver.close();
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        driverinstance.remove();
    }
}
