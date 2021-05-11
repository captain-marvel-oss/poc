package e2e.libraries;

import com.aventstack.extentreports.Status;
import e2e.drivers.DriverFactory;
import e2e.framework.Reporter;
import e2e.framework.support.ConfigProp;
import e2e.framework.support.DriverDataHolder;
import io.appium.java_client.MobileBy;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class ObjectLookup {

    enum Locator {
        id, mobile_id, xpath, AccessibilityId, classname, css, iOSClassChain, predicate, linktext, partiallinktext
    }

    public static WebElement invisibilityElem(String driverName, String filename, String locatorName,
            String elemDescription) {

        WebElement elem = null;
        WebDriver domDriver = DriverFactory.getDriver();
        WebObj webobj = getLocatorValue(locatorName, filename);
        try {
            WebDriverWait wait = new WebDriverWait(domDriver, 30);
            wait.until(driver1 -> ExpectedConditions.invisibilityOf(domDriver.findElement(webobj.by)));
            System.out.println("Logs: An attempt to locate an element is successful ||| Element/Step description: "
                    + elemDescription);
        } catch (Exception e) {
            System.out.println("Element is not available : " + elemDescription);
            e.printStackTrace();
        }
        return elem;
    }

    public static WebElement style_tobenone(String driverName, String filename, String locatorName,
            String elemDescription) {

        WebElement elem = null;
        WebDriver domDriver = DriverFactory.getDriver();
        WebObj webobj = getLocatorValue(locatorName, filename);
        try {
            WebDriverWait wait = new WebDriverWait(domDriver, 30);
            wait.until(driver2 -> ExpectedConditions.presenceOfElementLocated(webobj.by));
            JavascriptExecutor executor = (JavascriptExecutor) domDriver;
            wait.until(driver1 ->
            // domDriver.findElement(buildBy(loc)).getAttribute("style").contains("none"));
            executor.executeScript(
                    "var items = {}; " + "for (index = 0; index < arguments[0].attributes.length; ++index) "
                            + "{ items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; "
                            + "return items;",
                    domDriver.findElement(webobj.by)).toString().contains("style"));
            wait.until(driver1 -> domDriver.findElement(webobj.by).getAttribute("style").contains("none"));
            System.out.println("Logs: An attempt to locate an element is successful ||| Element/Step description: "
                    + elemDescription);
        } catch (Exception e) {
            System.out.println("Element is not available : " + elemDescription);
            e.printStackTrace();
        }
        return elem;
    }

    public static WebElement elemAttributeToBe(String driverName, String filename, String locatorName, String attribute,
            String value) {

        WebElement elem = null;
        WebDriver domDriver = DriverFactory.getDriver();
        WebObj webobj = getLocatorValue(locatorName, filename);
        try {
            WebDriverWait wait = new WebDriverWait(domDriver,
                    Integer.parseInt(ConfigProp.getPropertyValue("elementtimeout")));
            wait.until(driver2 -> ExpectedConditions.presenceOfElementLocated(webobj.by));
            wait.until(driver1 -> domDriver.findElement(webobj.by).getAttribute(attribute).contains(value));
            // System.out.println("Logs: An attempt to locate an element is successful |||
            // Element/Step description: " + elemDescription);
        } catch (Exception e) {
            // System.out.println("Element is not available : " + elemDescription);
            e.printStackTrace();
        }
        return elem;
    }

    public static WebElement elemhasText(String filename, String locatorName, String value) {

        WebElement elem = null;
        WebDriver domDriver = DriverFactory.getDriver();
        WebObj webobj = getLocatorValue(locatorName, filename);
        try {
            WebDriverWait wait = new WebDriverWait(domDriver,
                    Integer.parseInt(ConfigProp.getPropertyValue("elementtimeout")));
            wait.until(driver2 -> ExpectedConditions.presenceOfElementLocated(webobj.by));
            System.out.println(domDriver.findElement(webobj.by).getText());
            wait.until(driver1 -> domDriver.findElement(webobj.by).getText().equals(value));
            System.out.println(domDriver.findElement(webobj.by).getText());
            // System.out.println("Logs: An attempt to locate an element is successful |||
            // Element/Step description: " + elemDescription);
        } catch (Exception e) {
            // System.out.println("Element is not available : " + elemDescription);
            e.printStackTrace();
        }
        return elem;
    }

    public static By buildBy(String[] loc) {
        Locator l = Locator.valueOf(loc[0]);
        String elemfind = loc[1];
        By by = null;
        switch (l) {
            case id:
                return By.id(elemfind);
            case mobile_id:
                return MobileBy.id(elemfind);
            case xpath:
                return By.xpath(elemfind);
            case AccessibilityId:
                return MobileBy.AccessibilityId(elemfind);
            case classname:
                return By.className(elemfind);
            case iOSClassChain:
                return MobileBy.iOSClassChain(elemfind);
            case linktext:
                return MobileBy.linkText(elemfind);
            case partiallinktext:
                return MobileBy.partialLinkText(elemfind);
            case predicate:
                return MobileBy.iOSNsPredicateString(elemfind);
            case css:
                return By.cssSelector(elemfind);
            default:
                return By.xpath("");
        }
        // return by;
    }

    public static WebObj getLocatorValue(String locatorName, String filename) {

        JSONParser parser = new JSONParser();
        try {
            URL resource = ObjectLookup.class.getResource("/" + filename + ".json");
            // System.out.println("JSON: " + resource);
            Object obj = parser.parse(new FileReader(new File(resource.toURI())));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject elemjson = (JSONObject) jsonObject.get(locatorName);
            JSONObject locjson = (JSONObject) elemjson.get(currentLocDriver());
            String desc = (String) elemjson.get("description");
            String by = (String) locjson.get("by");
            String value = (String) locjson.get("value");
            buildBy(new String[] { by, value });
            // System.out.println("Locator: " + by+value+desc);
            return new WebObj(buildBy(new String[] { by, value }), desc);
        } catch (Exception e) {
            e.printStackTrace();
            Reporter.log(Status.FATAL, "Error getting the locator value from the file " + filename
                    + "for the locator name " + locatorName);
        }
        return null;
    }

    public static String currentLocDriver() {
        if (DriverDataHolder.getValue("Platform").toLowerCase().equals("desktop")) {
            if (!DriverDataHolder.getValue("Res_Width").isEmpty()) {
                if (Integer.parseInt(DriverDataHolder.getValue("Res_Width")) < 500) {
                    return "mobileweb";
                } else {
                    return "web";
                }
            } else {
                return "web";
            }
        } else if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("app")
                && DriverDataHolder.getValue("OS").equalsIgnoreCase("ios")) {
            return "iosapp";
        } else if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("app")
                && DriverDataHolder.getValue("OS").equalsIgnoreCase("Android")) {
            return "andapp";
        } else {
            return "mobileweb";
        }
    }
}
