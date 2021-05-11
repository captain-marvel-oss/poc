package e2e.libraries;

import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableMap;
import e2e.drivers.DriverFactory;
import e2e.framework.Reporter;
import e2e.framework.support.ConfigProp;
import e2e.framework.support.DriverDataHolder;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.TestNG;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wrapper {

	static void logger(Status status, String description) {
		// TestNG.logger.info(description);
		Reporter.log(status, description);
	}

	/**
	 * Clicks on a Webelement
	 *
	 * @param element         WebElement (Web/Mobile)
	 * @param elemDescription Description of Element for reporting
	 */
	// @Step("Clicking Element")
	public static void clickElement(WebElement element, String elemDescription) {
		try {
			element.click();
			logger(Status.PASS, "Click successful: " + elemDescription);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, "Click not successful: " + elemDescription);
		}
	}

	/**
	 * Clicks on a Webelement
	 *
	 * @param obj WebObj (Web/Mobile)
	 */
	// @Step("Clicking Element {obj.by} {obj.desc}")
	public static void clickElement(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElementNoLog(obj);
			if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("safari")) {
				clickJs(obj);
				// logger(Status.PASS, "Click successful: " + obj.desc);
			} else {
				driver.findElement(obj.by).click();
				logger(Status.PASS, "Click successful: " + obj.desc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, "Click not successful: " + obj.desc);
		}
	}

	// @Step("Clicking Element")
	public static void clickElement(By elementby, String elemDescription) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			driver.findElement(elementby).click();
			logger(Status.PASS, "Click successful: " + elemDescription);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, "Click not successful: " + elemDescription);
		}
	}

	/**
	 * Moves to an element and triggers Java script Click
	 *
	 * @param webObj WebObj (Web/Mobile)
	 */
	// @Step("Clicking Element {webObj.by} {webObj.desc}")
	public static void clickJs(WebObj webObj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement element = driver.findElement(webObj.by);
			intentionalSleep(1);
			clickElementJS(element, webObj.desc);
			// logger(Status.PASS, "Click successful: " + webObj.desc);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Click not successful: " + webObj.desc);
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Moves to an element and triggers Java script Click
	 *
	 * @param webObj WebObj (Web/Mobile)
	 */
	// @Step("Clicking Element {webObj.by} {webObj.desc}")
	public static void clickWithCoordinates(WebObj webObj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement element = driver.findElement(webObj.by);
			int x = element.getRect().x;
			int y = element.getRect().y;
			int h = element.getRect().height;
			int w = element.getRect().width;
			Actions actions = new Actions(driver);
			actions.moveByOffset((int) (x + (w * .5)), (int) (y + (h * .75))).click().perform();
			intentionalSleep(1);
			// clickElementJS(element, webObj.desc);
			// logger(Status.PASS, "Click successful: " + webObj.desc);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Click not successful: " + webObj.desc);
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Moves to an element and triggers Java script Click
	 *
	 * @param webObj WebObj (Web/Mobile)
	 */
	// @Step("Clicking Element {webObj.by} {webObj.desc}")
	public static void clickElementJSRadio(WebObj webObj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement element = driver.findElement(webObj.by);
			intentionalSleep(1);
			clickElementJSRadio(element, webObj.desc);
			logger(Status.PASS, "Click successful: " + webObj.desc);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Click not successful: " + webObj.desc);
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Moves to an element and triggers Java script Click
	 *
	 * @param webObj WebObj (Web/Mobile)
	 */
	public static void moveToElem(WebObj webObj) {
		if (!DriverDataHolder.getValue("Browser").equalsIgnoreCase("safari"))
			try {
				WebDriver driver = DriverFactory.getDriver();
				// WebElement element = driver.findElement(webObj.by);
				// intentionalSleep(1);
				Actions actions = new Actions(driver);
				actions.moveToElement(driver.findElement(webObj.by)).build().perform();
				// logger(Status.PASS, "Click successful: " + webObj.desc);
			} catch (Exception e) {
				e.printStackTrace();
				logger(Status.FAIL, "Click not successful: " + webObj.desc);
				logger(Status.ERROR, e.getMessage());
			}
	}

	/**
	 * Moves to an element and triggers Java script Click
	 *
	 * @param webObj WebObj (Web/Mobile)
	 */
	public static void moveToElem(WebElement webElement) {
		if (!DriverDataHolder.getValue("Browser").equalsIgnoreCase("safari"))
			try {
				WebDriver driver = DriverFactory.getDriver();
				// WebElement element = driver.findElement(webObj.by);
				// intentionalSleep(1);
				Actions actions = new Actions(driver);
				actions.moveToElement(webElement).build().perform();
				// logger(Status.PASS, "Click successful: " + webObj.desc);
			} catch (Exception e) {
				e.printStackTrace();
				logger(Status.ERROR, e.getMessage());
			}
	}

	public static void moveToElem(By by) {
		if (!DriverDataHolder.getValue("Browser").equalsIgnoreCase("safari"))
			try {
				WebDriver driver = DriverFactory.getDriver();
				// WebElement element = driver.findElement(webObj.by);
				// intentionalSleep(1);
				Actions actions = new Actions(driver);
				actions.moveToElement(driver.findElement(by)).build().perform();
				// logger(Status.PASS, "Click successful: " + webObj.desc);
			} catch (Exception e) {
				e.printStackTrace();
				logger(Status.ERROR, e.getMessage());
			}
	}

	/**
	 * Java Script click on the supplied Element
	 *
	 * @param element         WebElement (Web/Mobile)
	 * @param elemDescription Description of Element for reporting
	 */
	// @Step("Clicking Element")
	public static void clickElementJS(WebElement element, String elemDescription) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			logger(Status.PASS, "Click successful: " + elemDescription);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Click not successful: " + elemDescription);
			logger(Status.ERROR, e.getMessage());
		}
	}

	// @Step("Clicking Element {by}")
	public static void clickElementJS(By by, String elemDescription) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", driver.findElement(by));
			logger(Status.PASS, "Click successful: " + elemDescription);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Click not successful: " + elemDescription);
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Java Script click on the supplied Element
	 *
	 * @param element         WebElement (Web/Mobile)
	 * @param elemDescription Description of Element for reporting
	 */
	// @Step("Clicking Radio Element")
	public static void clickElementJSRadio(WebElement element, String elemDescription) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].checked = true;", element);
			logger(Status.PASS, "Click successful: " + elemDescription);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Click not successful: " + elemDescription);
			logger(Status.ERROR, e.getMessage());
		}
	}

	public static void selectText(WebObj webObj, String text) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElementNoLog(webObj);
			intentionalSleep(2);
			verifyElementNoLog(webObj);
			Select choose = new Select(driver.findElement(webObj.by));
			choose.selectByVisibleText(text);
			blurElement(webObj);
			logger(Status.PASS, "Value selected successfully: " + text);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Dropdown not selected: " + webObj.desc + " " + text);
			logger(Status.ERROR, e.getMessage());
		}
	}

	public static void selectTextPartial(WebObj webObj, String text) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElementNoLog(webObj);
			intentionalSleep(2);
			verifyElementNoLog(webObj);
			Select choose = new Select(driver.findElement(webObj.by));
			int count = 0;
			for (WebElement valueofchoose : choose.getOptions()) {
				String innertext = valueofchoose.getText();
				System.out.println("INNERTEXT :" + innertext);
				if (innertext.toLowerCase().contains(text.toLowerCase())) {
					choose.selectByVisibleText(innertext);
					count++;
					break;
				}
			}
			if (count == 0)
				logger(Status.FAIL, "Value not selected successfully: " + text);
			else
				logger(Status.PASS, "Value selected successfully: " + text);
			blurElement(webObj);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Dropdown not selected: " + webObj.desc + " " + text);
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Scrolls down to the bottom of the page
	 */
	public static void scrolltoPageEnd() {
		WebDriver driver = DriverFactory.getDriver();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, document.body.scrollHeight);");
	}

	/**
	 * Scrolls down to the bottom of the page
	 */
	public static void scrolltoPageMid() {
		WebDriver driver = DriverFactory.getDriver();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, document.body.scrollHeight/2);");
	}

	/**
	 * Scrolls down to the bottom of the page
	 */
	public static void scrolltoPageQuat() {
		WebDriver driver = DriverFactory.getDriver();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, document.body.scrollHeight/4);");
	}

	/**
	 * Enters the supplied text to the text box
	 *
	 * @param element WebElement (Web/Mobile)
	 * @param text    text to enter
	 */
	// @Step("Entering Text To Element")
	public static void enter_Text(WebElement element, String text) {
		try {
			element.clear();
			element.sendKeys(text);

			logger(Status.PASS, "Text is entered successfully: " + text);
		} catch (Exception e) {

			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Enters the supplied text to the text box
	 *
	 * @param obj         WebObj (Web/Mobile)
	 * @param text_to_key text to enter
	 */
	// @Step("Entering Text {text_to_key} - To Element {obj.desc}")
	public static void enter_Text(WebObj obj, String text_to_key) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement elem = driver.findElement(obj.by);
			Wrapper.focusElement(obj);
			if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("app")) {
				verifyElementClickable(obj);
				elem.click();
				elem.sendKeys(text_to_key);
				logger(Status.PASS, "Text is entered successfully: " + text_to_key);
			} else {
				elem = verifyElementNoLog(obj);
				// elem.click();
				scrolltoPageEnd();
				elem.clear();
				elem.sendKeys(text_to_key);
				logger(Status.PASS, "Text is entered successfully: " + text_to_key);
				try {
					elem = driver.findElement(obj.by);
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].focus(); arguments[0].blur(); return true", elem);
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Enters the supplied text to the text box
	 *
	 * @param obj         WebObj (Web/Mobile)
	 * @param text_to_key text to enter
	 */
	// @Step("Entering Text {text_to_key} - To Element {obj.desc}")
	public static void enter_Text_press_enter(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement elem = driver.findElement(obj.by);
			Wrapper.focusElement(obj);

			elem = verifyElementNoLog(obj);
			// elem.click();
			scrolltoPageEnd();
			elem.clear();
			elem.sendKeys(Keys.ENTER);
		} catch (Exception e) {

			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Enters the supplied text to the text box
	 *
	 * @param obj         WebObj (Web/Mobile)
	 * @param text_to_key text to enter
	 */
	// @Step("Entering Text {text_to_key} - To Element {obj.desc}")
	public static void enter_TextJs(WebObj obj, String text_to_key) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement elem = driver.findElement(obj.by);
			Wrapper.focusElement(obj);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// js.executeScript("document.getElementById('elementID').setAttribute('value',
			// 'new value for element')");
			js.executeScript("arguments[0].setAttribute('value',arguments[1])", elem, text_to_key);

		} catch (Exception e) {

			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Enters the supplied text to the text box
	 *
	 * @param obj         WebObj (Web/Mobile)
	 * @param text_to_key text to enter
	 */
	// @Step("Entering Text {text_to_key} - To Element {obj.desc}")
	public static void enter_Text_onlySend(WebObj obj, String text_to_key) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement elem = driver.findElement(obj.by);
			if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("app")) {
				elem.sendKeys(text_to_key);
				logger(Status.PASS, "Text is entered successfully: " + text_to_key);
			} else {
				elem.sendKeys(text_to_key);
				logger(Status.PASS, "Text is entered successfully: " + text_to_key);
				try {
					elem = driver.findElement(obj.by);
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].focus(); arguments[0].blur(); return true", elem);
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	// @Step("Entering Text To Element")
	public static void enter_Text_onlySend(By by, String text_to_key, String desc) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement elem = driver.findElement(by);
			if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("app")) {
				elem.sendKeys(text_to_key);
				logger(Status.PASS, "Text is entered successfully: " + text_to_key);
			} else {
				elem.sendKeys(text_to_key);
				logger(Status.PASS, "Text is entered successfully: " + text_to_key);
				try {
					elem = driver.findElement(by);
					JavascriptExecutor js = (JavascriptExecutor) driver;
					js.executeScript("arguments[0].focus(); arguments[0].blur(); return true", elem);
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Enters the supplied text to the text box
	 *
	 * @param obj         WebObj (Web/Mobile)
	 * @param text_to_key text to enter
	 */
	// @Step("Entering Text To Element")
	public static void enter_Text_Js(WebObj obj, String text_to_key) {
		try {
			verifyElementClickable(obj);
			WebDriver driver = DriverFactory.getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("document.getElementById('pt1:mfu2:fcItr:0:sf1:if11').value='" + text_to_key + "';");
		} catch (Exception e) {

			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Enters the supplied text to the text box
	 *
	 * @param obj         WebObj (Web/Mobile)
	 * @param text_to_key text to enter
	 */
	// @Step("Entering Text {text_to_key} - To Element {obj.desc}")
	public static void enter_Text_AutoFill(WebObj obj, String text_to_key) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement elem = driver.findElement(obj.by);
			if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("app")) {
				verifyElementNoLog(obj);
				elem.click();
				elem.sendKeys(text_to_key);
				logger(Status.PASS, "Text is entered successfully: " + text_to_key);
			} else {
				elem = verifyElementNoLog(obj);
				// elem.clear();
				// elem.click();
				elem.sendKeys(text_to_key);
				logger(Status.PASS, "Text is entered successfully: " + text_to_key);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].focus(); arguments[0].blur(); return true", elem);
				// intentionalSleep(1);
			}
		} catch (Exception e) {

			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	public static void enterSearch() {
		try {
			WebDriver driver = DriverFactory.getDriver();
			((AppiumDriver<?>) driver).executeScript("mobile: performEditorAction",
					ImmutableMap.of("action", "Search"));
		} catch (Exception e) {

		}
	}

	public static void hideKeyboard() {
		try {
			if (ObjectLookup.currentLocDriver().equals("andapp")) {
				WebDriver driver = DriverFactory.getDriver();
				((AppiumDriver<?>) driver).hideKeyboard();
			}
		} catch (Exception e) {

		}
	}

	public static void blurElement(WebObj webObj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement elem = verifyElement(webObj);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].focus(); arguments[0].blur(); return true", elem);
		} catch (Exception e) {
			//
		}
	}

	public static void focusElement(WebObj webObj) {
		try {
			verifyElement(webObj);
			WebDriver driver = DriverFactory.getDriver();
			WebElement elem = verifyElement(webObj);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].focus(); return true", elem);
		} catch (Exception e) {
			//
		}
	}

	public static void focusElement(WebElement webElement) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].focus(); return true", webElement);
		} catch (Exception e) {
			//
		}
	}

	public static void hoverElement(WebObj webObj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement elem = verifyElement(webObj);
			// JavascriptExecutor js = (JavascriptExecutor) driver;
			// js.executeScript("arguments[0].hover(); return true", elem);

			String strJavaScript = "var element = arguments[0];"
					+ "var mouseEventObj = document.createEvent('MouseEvents');"
					+ "mouseEventObj.initEvent( 'mouseover', true, true );" + "element.dispatchEvent(mouseEventObj);";

			((JavascriptExecutor) driver).executeScript(strJavaScript, elem);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Enters text only if the value of the element is empty
	 *
	 * @param element WebElement (Web/Mobile)
	 * @param text    text to enter
	 */
	public static void enter_Text_if_empty(WebElement element, String text) {
		try {
			if (element.getText().isEmpty()) {
				enter_Text(element, text);
			}
		} catch (Exception e) {
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Enters text only if the value of the element is empty
	 *
	 * @param webObj WebObj (Web/Mobile)
	 * @param text   text to enter
	 */
	public static void enter_Text_if_empty(WebObj webObj, String text) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(webObj);
			WebElement element = driver.findElement(webObj.by);
			if (element.getText().isEmpty()) {
				enter_Text(element, text);
			} else
				logger(Status.INFO, "Text already present");
		} catch (Exception e) {
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Clears text from the specified element
	 *
	 * @param element         WebElement (Web/Mobile)
	 * @param elemdescription Description of the element for report
	 */
	public static void clear_Text(WebElement element, String elemdescription) {
		try {
			element.clear();
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	public static void clear_Text(WebObj webObj) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			verifyElement(webObj);
			driver.findElement(webObj.by).clear();
		} catch (Exception e) {

			e.printStackTrace();
			logger(Status.FAIL, "Text is not entered successfully");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Waits for the page load with 2 mins as timeout
	 */
	// @Step("Wait for page Load")
	public static void waitForPageLoad() {
		WebDriver driver = DriverFactory.getDriver();
		WebDriverWait wait = new WebDriverWait(driver, 120);
		if (!DriverDataHolder.getValue("Browser").equalsIgnoreCase("app"))
			try {

				wait.until(driver1 -> String
						.valueOf(((JavascriptExecutor) driver1).executeScript("return document.readyState;"))
						.equals("complete"));

				Wrapper.intentionalSleepSafari(2);
			} catch (Exception e) {
				e.printStackTrace();
				logger(Status.FAIL, "Page Load Unsuccessful" + e.getLocalizedMessage());
			}
	}

	/**
	 * Invokes thread to sleep for 5 seconds
	 */
	public static void intentionalSleep() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Invokes thread to sleep for specified seconds
	 *
	 * @param i seconds
	 */
	public static void intentionalSleep(int i) {
		try {
			Thread.sleep(i * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Invokes thread to sleep for specified seconds only in safari browser
	 *
	 * @param i seconds
	 */
	public static void intentionalSleepSafari(int i) {
		try {
			if (DriverDataHolder.getValue("Browser").equalsIgnoreCase("Safari"))
				Thread.sleep(i * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Screen capture for Reports
	 *
	 * @return Path of screenshot to embed
	 */
	public static String captureScreen() {
		String path;
		File trgtPath = null;
		try {
			WebDriver driver = DriverFactory.getDriver();
			File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			path = Reporter.reportpath + "screens" + File.separator + source.getName();
			trgtPath = new File(path);
			FileUtils.copyFile(source, trgtPath);
		} catch (Exception e) {
			logger(Status.FAIL, "Failed to capture screenshot: " + e.getMessage());
		}
		return trgtPath.getAbsolutePath();

	}

	@Attachment(value = "Page screenshot", type = "image/png")
	public static byte[] saveScreenshotPNG() {
		WebDriver driver = DriverFactory.getDriver();
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	@Attachment
	public static String captureScreen(String test_name_screenshot) {
		String path = null;
		String path_screen = null;
		File trgtPath = null;
		try {
			// WebDriver augmentedDriver = new Augmenter().augment(driverLoc);
			WebDriver driver = DriverFactory.getDriver();
			File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			path = "Reports" + File.separator + Reporter.foldername + File.separator + "screens" + File.separator
					+ test_name_screenshot + File.separator + source.getName();
			path_screen = path;
			trgtPath = new File(path);
			FileUtils.copyFile(source, trgtPath);
			path = "screens" + File.separator + test_name_screenshot + File.separator + source.getName();
		} catch (Exception e) {
			logger(Status.FAIL, "Failed to capture screenshot: " + e.getMessage());
		}
		return path_screen;
	}

	public static String constructReportPath() {
		return "Reports" + File.separator + Reporter.foldername + File.separator;
	}

	/**
	 * Launches the url from the config file
	 *
	 * @param url
	 */
	public static void launchbaseURL(String url) {
		try {

			WebDriver driver = DriverFactory.getDriver();

			driver.get(url);
			logger(Status.PASS, "URL Launched :" + url);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.ERROR, "Error Launching URL: " + e.getMessage());
		}
	}

	public static void sendAppBackground() {
		// WebDriver driver = DriverFactory.getDriver();

		// ((MobileDriver) driver).runAppInBackground(Duration.ofMillis(5));
	}

	public static String getAttribute(WebObj webObj, String attribute_name) {
		WebDriver driver = DriverFactory.getDriver();
		return driver.findElement(webObj.by).getAttribute(attribute_name);
	}

	public static String getAttribute(WebElement element, String attribute_name) {
		return element.getAttribute(attribute_name);
	}

	public static void assertAttributeContains(WebObj webObj, String attr, String link_from_property) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			scrolltoPageEnd();
			String attribute = driver.findElement(webObj.by).getAttribute(attr);
			if (attribute.contains(link_from_property)) {
				logger(Status.PASS, "Validate: " + webObj.desc + " || Attribute contains : " + link_from_property);
			} else {
				logger(Status.INFO, "Actual Value: " + attribute);
				logger(Status.FAIL, "Attribute doesn't contains " + link_from_property);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Attribute doesn't contains " + link_from_property);
		}
	}

	public static void assertAttributeToBe(WebObj webObj, String attr, String value) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.attributeToBe(webObj.by, attr, value));
		} catch (Exception e) {
			e.printStackTrace();
			Reporter.log(Status.FAIL, "Attribute doesn't contains " + value);
		}
	}

	public static void assertAttributeHasValue(WebObj webObj, String attribute) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			String has_attr = driver.findElement(webObj.by).getAttribute(attribute);
			if (has_attr.equalsIgnoreCase("true")) {
				logger(Status.PASS, "Validate: " + webObj.desc + " || Attribute contains : " + attribute);
			} else {
				logger(Status.INFO, "Actual Value: " + attribute);
				logger(Status.FAIL, "Attribute doesn't contains " + attribute);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Attribute doesn't contains " + attribute);
		}
	}

	public static void assertAttributeHasValue(By by, String attribute) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			String has_attr = driver.findElement(by).getAttribute(attribute);
			System.out.println("ATTR: " + has_attr);
			if (has_attr.equalsIgnoreCase("true")) {
				logger(Status.PASS, "Attribute contains : " + attribute);
			} else {
				logger(Status.INFO, "Actual Value: " + attribute);
				logger(Status.FAIL, "Attribute doesn't contains " + attribute);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Attribute doesn't contains " + attribute);
		}
	}

	public static void assertAttributeValueHasData(WebObj webObj, String attribute) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			String has_attr = driver.findElement(webObj.by).getAttribute(attribute);
			if (has_attr.length() > 0) {
				logger(Status.PASS, "Validate: " + webObj.desc + " || Attribute contains : " + attribute);
			} else {
				logger(Status.INFO, "Actual Value: " + attribute);
				logger(Status.FAIL, "Attribute doesn't contains " + attribute);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Attribute doesn't contains " + attribute);
		}
	}

	public static long getETime() {
		return Instant.now().getEpochSecond();
	}

	public static String matchRegex(String s, String regexpattern) {
		Pattern p = Pattern.compile(regexpattern);
		Matcher matcher = p.matcher(s);
		String matched = "";
		while (matcher.find()) {
			matched = matcher.group();
			matched = matched.replace("<", "");
			matched = matched.replace(">", "");
			return matched;
		}
		return matched;
	}

	public static void switchToNativeContext() {
		try {
			WebDriver driver = DriverFactory.getDriver();
			Set<String> contextNames = ((AppiumDriver<?>) driver).getContextHandles();

			for (String name : contextNames) {
				if (name.contains("NATIVE_APP")) {
					((AppiumDriver<?>) driver).context(name);

					break;
				}
			}
		} catch (Exception e) {

		}
	}

	public static void switchToWebviewContext() {
		try {
			WebDriver driver = DriverFactory.getDriver();
			Set<String> contextNames = ((AppiumDriver<?>) driver).getContextHandles();
			for (String name : contextNames) {

				if (name.contains("WEB")) {
					((AppiumDriver<?>) driver).context(name);

					break;
				}
			}

		} catch (Exception e) {

		}
	}

	public static void switchToWebviewContext2() {
		try {
			WebDriver driver = DriverFactory.getDriver();
			Set<String> contextNames = ((AppiumDriver<?>) driver).getContextHandles();
			for (String name : contextNames) {

				if (name.contains("WEB") && name.contains(".2")) {
					((AppiumDriver<?>) driver).context(name);

					break;
				}
			}

		} catch (Exception e) {

		}
	}

	public static void resetApp() {
		WebDriver driver = DriverFactory.getDriver();
		((AppiumDriver<?>) driver).resetApp();
		((AppiumDriver<?>) driver).launchApp();
	}

	public static void waitForElementTextToBePresent(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.textMatches(obj.by, Pattern.compile("[a-zA-Z]+")));
			logger(Status.PASS, obj.desc + "- Element Expected text found");
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "Element Expected text didnt load");
			logger(Status.ERROR, obj.desc + "Element Expected text didnt load");
		}
	}

	public static void waitForElementAttributeValueNotEmpty(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.attributeToBeNotEmpty(driver.findElement(obj.by), "value"));
			logger(Status.PASS, obj.desc + "- Element Expected text found");
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "Element Expected text didnt load");
			logger(Status.ERROR, obj.desc + "Element Expected text didnt load");
		}
	}

	public static void waitForElementAttributeNotPresent(WebObj obj, String attribute, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(driver1 -> !(driver.findElement(obj.by)).getAttribute(attribute).contains(value));
			logger(Status.PASS, obj.desc + "- Element Expected text found");
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "Element Expected text didnt load");
			logger(Status.ERROR, obj.desc + "Element Expected text didnt load");
		}
	}

	public static void waitForElementClickable(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(obj.by)));
			logger(Status.PASS, obj.desc + "- Element Expected text found");
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "Element Expected text didnt load");
			logger(Status.ERROR, obj.desc + "Element Expected text didnt load");
		}
	}

	public static void waitForElementTextToBe(WebObj obj, String text) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			System.out.println("Status.INFO: TEXT -  "+ text);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(obj.by, text));
			logger(Status.PASS, obj.desc + "- Element Expected text found");
			logger(Status.INFO, text);
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "Element Expected text didnt load");
			logger(Status.ERROR, obj.desc + "Element Expected text didnt load");
		}
	}

	public static void waitForElementTextToContain(WebObj obj, String text) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 30);
			logger(Status.INFO, text);
			logger(Status.INFO, driver.findElement(obj.by).getText());
			// wait.until(ExpectedConditions.textToBePresentInElementValue(obj.by, text));
			wait.until(driver1 -> driver.findElement(obj.by).getText().toLowerCase().contains(text.toLowerCase()));
			logger(Status.PASS, obj.desc + "- Element Expected text found");
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "Element Expected text didnt load");
			logger(Status.ERROR, obj.desc + "Element Expected text didnt load");
		}
	}

	public static String getCurrentDate(String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	// @Step("Assertion - Element {webobj.by} not present")
	public static void assertNotPresent(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			if (driver.findElements(obj.by).size() == 0)
				logger(Status.PASS, obj.desc + "- Element Not Present");
			else {
				logger(Status.INFO,
						obj.desc + "- Element is Present" + "| TEXT: | " + driver.findElement(obj.by).getText());
				logger(Status.FAIL, obj.desc + "- Element is Present");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "- Element is Present");

		}
	}

	// @Step("Assertion - Element {by} not present condition , with value {desc}")
	public static void assertNotPresent(By by, String desc) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			if (driver.findElements(by).size() == 0)
				logger(Status.PASS, desc + "- Element Not Present");
			else
				logger(Status.FAIL, desc + "- Element is Present");
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, desc + "- Element is Present");

		}
	}

	public static String getURL() {
		try {
			WebDriver driver = DriverFactory.getDriver();
			return driver.getCurrentUrl();
			// logger(Status.PASS, url);
		} catch (Exception e) {
			logger(Status.FAIL, "Unable to get URL");
		}
		return null;
	}

	public static void switchToWindow(String pagename) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			boolean found = false;
			intentionalSleep(5);
			Set<String> windows = driver.getWindowHandles();
			for (String window_name : windows) {
				driver.switchTo().window(window_name);
				Wrapper.waitForPageLoad();

				if (driver.getTitle().contains(pagename)) {
					found = true;
					break;
				}
			}
			if (found)
				logger(Status.PASS, "Page Switch successful");
			else
				logger(Status.FAIL, "Page Switch unsuccessful");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToFrame(String s) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			intentionalSleep(1);

			driver.switchTo().frame(s);
			logger(Status.PASS, "Frame Switch successful");
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.FAIL, "Frame Switch successful");
		}
	}

	public static void assertStringhasData(String string_data) {
		if (string_data.length() > 0)
			logger(Status.PASS, "String has correct data");
		else
			logger(Status.FAIL, "String has incorrect data");
	}

	/**
	 * Get Title of the page
	 *
	 * @return Page Title
	 */
	public String get_Title() {
		WebDriver driver = DriverFactory.getDriver();
		logger(Status.INFO, "Title:" + driver.getTitle());
		return driver.getTitle();

	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param element WebElement (Web/Mobile)
	 * @param desc    Description of the element for Report
	 */
	public static void asserthastext(WebElement element, String desc) {
		try {
			if (!element.getText().isEmpty())
				logger(Status.PASS, "Elements contains " + desc + " Text");
			else
				logger(Status.FAIL, "Element doesn't have the content: " + desc);
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
		}
	}

	public static void asserthastext(WebObj webobj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElementNoLog(webobj);
			WebElement element = driver.findElement(webobj.by);
			System.out.println(element.getText());
			if (!element.getText().isEmpty())
				logger(Status.PASS, "Elements contains " + webobj.desc + " Text");
			else
				logger(Status.FAIL, "Element doesn't have the content: " + webobj.desc);
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param element WebElement (Web/Mobile)
	 * @param value   Value to compare
	 * @param desc    Description of the element for Report
	 */
	public static void asserttext(WebElement element, String value, String desc) {
		try {
			if (element.getText().contains(value))
				logger(Status.PASS, "Elements contains " + value + " Text :" + desc);
			else
				logger(Status.FAIL, "Element doesn't have the content: " + value);
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} has text condition , with value
	// {value}")
	public static boolean asserttext(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElementNoLog(webobj);
			WebElement element = driver.findElement(webobj.by);

			if (element.getText().equals(value)) {
				logger(Status.PASS, "Elements contains " + value + " Text :" + webobj.desc);
				return true;
			} else {
				logger(Status.INFO, "Element has content: " + element.getText());
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 */
	public static String getText(WebObj webobj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(webobj);
			WebElement element = driver.findElement(webobj.by);
			return element.getText();
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return "";
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 */
	public static String getText(By by) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElementNoLog(by);
			WebElement element = driver.findElement(by);
			return element.getText();
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return "";
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param element WebElement (Web/Mobile)
	 * @param value   Value to compare
	 */
	public static boolean asserttextNoLog(WebElement element, String value) {
		try {

			return element.getText().equals(value);
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param element WebElement (Web/Mobile)
	 * @param value   Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contains text condition , with value
	// {value}")
	public static boolean asserttextContainsNoLog(WebElement element, String value) {
		try {

			return element.getText().contains(value);
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contains text condition ignore case,
	// with value {value}")
	public static boolean asserttextIgnoreCase(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElementNoLog(webobj);
			WebElement element = driver.findElement(webobj.by);

			if (element.getText().equalsIgnoreCase(value)) {
				logger(Status.PASS, "Elements contains " + value + " Text :" + webobj.desc);
				return true;
			} else {
				logger(Status.INFO, "Element has content: " + element.getText());
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contains text condition , with value
	// {value}")
	public static boolean assertTextContains(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebElement element = driver.findElement(webobj.by);
			if (element.getText().replaceAll("\\s{2,}", " ").trim().contains(value)) {
				logger(Status.PASS, "Elements contains " + value + " Text :" + webobj.desc);
				return true;
			} else {
				logger(Status.INFO, "Element has content: " + element.getText());
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param element Webelement (Web/Mobile)
	 * @param value   Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contains text condition , with value
	// {value}")
	public static boolean assertTextContains(WebElement element, String value) {
		try {
			focusElement(element);
			if (element.getText().contains(value)) {
				logger(Status.PASS, "Elements contains " + value);
				return true;
			} else {
				logger(Status.INFO, "Element has content: " + element.getText());
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param element Webelement (Web/Mobile)
	 * @param value   Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contains text condition , with value
	// {value}")
	public static boolean assertTextContainsNoLog(WebElement element, String value) {
		try {
			return element.getText().contains(value);
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contains text condition ignore case,
	// with value {value}")
	public static boolean assertTextContainsIgnorecase(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(webobj);
			WebElement element = driver.findElement(webobj.by);
			if (element.getText().toLowerCase().replaceAll("\\s{2,}", " ").trim().contains(value.toLowerCase())) {
				logger(Status.PASS, "Elements contains " + value + " Text :" + webobj.desc);
				return true;
			} else {
				logger(Status.INFO, "Element has content: " + element.getText());
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	public static boolean assertTextContains(String elemText, String value, String elemTitle) {
		try {
			if (!(value == null)) {
				if (elemText.contains(value)) {
					return true;
				} else {
					return false;
				}
			} else {
				logger(Status.SKIP, "Element doesn't have any value for column - " + elemTitle);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contains text condition ignorecase,
	// with value {value}")
	public static boolean assertTextContainsIgnoreCase(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(webobj);
			WebElement element = driver.findElement(webobj.by);
			if (element.getText().toLowerCase().contains(value.toLowerCase())) {
				logger(Status.PASS, "Elements contains " + value + " Text :" + webobj.desc);
				return true;
			} else {
				logger(Status.INFO, "Element has content: " + element.getText());
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contains text condition ignorecase,
	// with value {value}")
	public static boolean assertTextContainsIgnoreCase(WebElement element, String value, String desc) {
		try {
			if (element.getText().toLowerCase().contains(value.toLowerCase())) {
				logger(Status.PASS, "Elements contains " + value + " Text :" + desc);
				return true;
			} else {
				logger(Status.INFO, "Element has content: " + element.getText());
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} starts with text condition , with
	// value {value}")
	public static boolean asserttextStartsWith(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(webobj);
			WebElement element = driver.findElement(webobj.by);

			if (element.getText().startsWith(value)) {
				logger(Status.PASS, "Elements Starts with " + value);
				return true;
			} else {
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contins text condition , with value
	// {value}")
	public static boolean asserttextContains(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(webobj);
			WebElement element = driver.findElement(webobj.by);
			focusElement(webobj);
			String elemtext = element.getText().replaceAll("\\s{2,}", " ").trim();
			elemtext = elemtext.replace("\n", "");
			if (elemtext.toLowerCase().contains(value.toLowerCase().replaceAll("\\s{2,}", " ").trim())) {
				logger(Status.PASS, "Elements contains " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return true;
			} else {
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} contins text condition , with value
	// {value}")
	public static boolean asserttextContainsSPL(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(webobj);
			WebElement element = driver.findElement(webobj.by);
			// moveToElem(webobj);
			String elemtext = element.getText().replaceAll("\\s{2,}", " ").trim();
			elemtext = elemtext.replace("\n", "");
			elemtext = elemtext.replace("  ", "");
			if (elemtext.toLowerCase().contains(value.toLowerCase().replaceAll("\\s{2,}", " ").trim())) {
				logger(Status.PASS, "Elements contains " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return true;
			} else {
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	// @Step("Assertion - Element {by} contains text condition , with value
	// {value}")
	public static boolean asserttextContains(By by, String desc, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(by, desc);
			WebElement element = driver.findElement(by);
			String elemtext = element.getText().replaceAll("\\s{2,}", " ").trim();
			elemtext = elemtext.replace("\n", "");
			if (elemtext.toLowerCase().contains(value.toLowerCase().replaceAll("\\s{2,}", " ").trim())) {
				logger(Status.PASS, "Elements contains " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return true;
			} else {
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webElement WebElement (Web/Mobile)
	 * @param value      Value to compare
	 */
	public static boolean asserttextContains(WebElement webElement, String value) {
		try {
			String elemtext = webElement.getText();
			if (elemtext.contains(value)) {
				logger(Status.PASS, "Elements contains " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return true;
			} else {
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} donot contains text condition , with
	// value {value}")
	public static boolean asserttextDonotContains(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(webobj);
			WebElement element = driver.findElement(webobj.by);
			moveToElem(webobj);
			String elemtext = element.getText().replaceAll("\\s{2,}", " ").trim();
			if (!elemtext.toLowerCase().contains(value.toLowerCase().replaceAll("\\s{2,}", " ").trim())) {
				logger(Status.PASS, "Element doesn't have the content " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return true;
			} else {
				logger(Status.FAIL, "Element has the content: " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts Element has correct text
	 *
	 * @param webobj WebObj (Web/Mobile)
	 * @param value  Value to compare
	 */
	// @Step("Assertion - Element {webobj.by} ends with text condition , with value
	// {value}")
	public static boolean asserttextEndsWith(WebObj webobj, String value) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			verifyElement(webobj);
			WebElement element = driver.findElement(webobj.by);
			moveToElem(webobj);
			String elemtext = element.getText().replaceAll("\\s{2,}", " ").trim();
			if (elemtext.toLowerCase().endsWith(value.toLowerCase().replaceAll("\\s{2,}", " ").trim())) {
				logger(Status.PASS, "Elements Ends with " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return true;
			} else {
				logger(Status.FAIL, "Element doesn't have the content: " + value);
				logger(Status.INFO, "Full Data of Element " + elemtext);
				return false;
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
			return false;
		}
	}

	/**
	 * Asserts if element is in the page
	 *
	 * @param element WebElement (Web/Mobile)
	 * @param desc    Description of the element for Report
	 */
	public static void verifyElement(WebElement element, String desc) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(ConfigProp.getPropertyValue("pagetimeout")));
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			logger(Status.FAIL, desc + "NOT - Available in the page");
			logger(Status.ERROR, e.getMessage());

		}
	}

	/**
	 * Asserts if element is in the page
	 *
	 * @param obj WebObj (Web/Mobile)
	 * @return Webelement
	 */
	// @Step("Verifying presence of element {obj.desc}")
	public static WebElement verifyElement(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(ConfigProp.getPropertyValue("pagetimeout")));
			WebElement elem;
			if ((!DriverDataHolder.getValue("Browser").equalsIgnoreCase("app"))
					|| DriverDataHolder.getValue("Driver").equalsIgnoreCase("appium")) {
				elem = wait.until(ExpectedConditions.presenceOfElementLocated(obj.by));
				// elem = wait.until(ExpectedConditions.elementToBeClickable(obj.by));
			} else {
				elem = wait.until(ExpectedConditions.presenceOfElementLocated(obj.by));
			}
			return elem;
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "NOT - Available in the page");

		}
		return null;
	}

	/**
	 * Asserts if element is in the page
	 *
	 * @param obj WebObj (Web/Mobile)
	 * @return Webelement
	 */
	public static WebElement verifyElementClickable(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			WebElement elem;
			if ((!DriverDataHolder.getValue("Browser").equalsIgnoreCase("app"))
					|| DriverDataHolder.getValue("Driver").equalsIgnoreCase("appium")) {
				elem = wait.until(ExpectedConditions.elementToBeClickable(obj.by));

			} else {
				elem = wait.until(ExpectedConditions.presenceOfElementLocated(obj.by));
			}

			logger(Status.PASS, obj.desc + "- Available in the page");
			return elem;
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "NOT - Available in the page");

		}
		return null;
	}

	/**
	 * Asserts if element is in the page
	 *
	 * @param by WebObj by (Web/Mobile)
	 * @return Webelement
	 */
	public static WebElement verifyElementClickable(By by, String text) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			WebElement elem;
			if ((!DriverDataHolder.getValue("Browser").equalsIgnoreCase("app"))
					|| DriverDataHolder.getValue("Driver").equalsIgnoreCase("appium")) {
				elem = wait.until(ExpectedConditions.elementToBeClickable(by));

			} else {
				elem = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			}

			logger(Status.PASS, text + "- Available in the page");
			return elem;
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, text + "NOT - Available in the page");

		}
		return null;
	}

	public static WebElement verifyElement(By by, String desc) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 30);
			WebElement elem;
			elem = wait.until(ExpectedConditions.presenceOfElementLocated(by));

			logger(Status.PASS, desc + "- Available in the page");
			return elem;
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, desc + "NOT - Available in the page");

		}
		return null;
	}

	public static WebElement verifyElementNoLog(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 5);
			WebElement elem;
			elem = wait.until(ExpectedConditions.presenceOfElementLocated(obj.by));
			return elem;
		} catch (Exception e) {
			return null;
		}
	}

	public static WebElement verifyElementNoLog(By by) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 5);
			WebElement elem;
			if ((!DriverDataHolder.getValue("Browser").equalsIgnoreCase("app"))
					|| DriverDataHolder.getValue("Driver").equalsIgnoreCase("appium"))
				elem = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			else
				elem = wait.until(ExpectedConditions.presenceOfElementLocated(by));
			return elem;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isElementPresent(WebObj obj) {
		try {
			WebElement elem = verifyElementNoLog(obj);
			return elem != null;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isElementPresent(By by) {
		try {
			WebElement elem = verifyElementNoLog(by);
			return elem != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Asserts if element is in the page
	 *
	 * @param obj WebObj (Web/Mobile)
	 * @return Webelement
	 */
	public static List<WebElement> verifyElements(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			List<WebElement> elem = null;
			if ((!DriverDataHolder.getValue("Browser").equalsIgnoreCase("app"))
					|| DriverDataHolder.getValue("Driver").equalsIgnoreCase("appium")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(obj.by));
				elem = driver.findElements(obj.by);
			} else {
				wait.until(ExpectedConditions.presenceOfElementLocated(obj.by));
				elem = driver.findElements(obj.by);
			}
			logger(Status.PASS, obj.desc + "- Available in the page");
			return elem;
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "NOT - Available in the page");

		}
		return null;
	}

	/**
	 * Asserts if element is in the page
	 *
	 * @param obj WebObj (Web/Mobile)
	 * @return Webelement
	 */
	public static List<WebElement> findAllElements(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.presenceOfElementLocated(obj.by));
			List<WebElement> elem = driver.findElements(obj.by);
			logger(Status.PASS, obj.desc + "- Available in the page");
			return elem;
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "NOT - Available in the page");

		}
		return null;
	}

	/**
	 * Asserts if element is in the page
	 *
	 * @param obj WebObj (Web/Mobile)
	 * @return Webelement
	 */
	public static WebElement waitForElement(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(ConfigProp.getPropertyValue("elementtimeout")));
			WebElement elem;
			if ((!DriverDataHolder.getValue("Browser").equalsIgnoreCase("app"))
					|| DriverDataHolder.getValue("Driver").equalsIgnoreCase("appium"))
				elem = wait.until(ExpectedConditions.visibilityOfElementLocated(obj.by));
			else
				elem = wait.until(ExpectedConditions.presenceOfElementLocated(obj.by));
			// logger(Status.PASS, obj.desc + "- Available in the page");
			return elem;
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "NOT - Available in the page");

		}
		return null;
	}

	/**
	 * Asserts if element is in the page
	 *
	 * @param obj WebObj (Web/Mobile)
	 * @return Webelement
	 */
	public static void waitForElementInvisibility(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 300);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(obj.by));
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "NOT - Available in the page");

		}
	}

	/**
	 * Asserts if element is in the page
	 *
	 * @param obj WebObj (Web/Mobile)
	 * @return Webelement
	 */
	public static void waitForElementVisibility(WebObj obj) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			WebDriverWait wait = new WebDriverWait(driver, 300);
			wait.until(ExpectedConditions.visibilityOfElementLocated(obj.by));
		} catch (Exception e) {
			e.printStackTrace();
			logger(Status.INFO, e.getMessage());
			logger(Status.FAIL, obj.desc + "NOT - Available in the page");

		}
	}

	/**
	 * Asserts if the element has text that starts with correct value
	 *
	 * @param element       WebElement (Web/Mobile)
	 * @param desc          Description of the element for Report
	 * @param startWithText Text to start with
	 */
	public static void startwith_Text(WebElement element, String desc, String startWithText) {
		try {
			if (element.getText().startsWith(startWithText) == true)
				logger(Status.PASS, desc + " - Text start with" + startWithText);
			else
				logger(Status.FAIL, "Text is not start with");
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Returns text of a particular element
	 *
	 * @param element WebElement (Web/Mobile)
	 * @return text
	 */
	public String get_Text(WebElement element) {
		String Text = null;
		try {
			return element.getText();
		} catch (Exception e) {
			logger(Status.FAIL, "Text is not found");
			logger(Status.ERROR, e.getMessage());
		}
		return Text;
	}

	/**
	 * Accepts Alert Message
	 */
	public static void accept_Alert() {
		WebDriver driver = DriverFactory.getDriver();

		try {
			driver.switchTo().alert().accept();
			logger(Status.PASS, "Alert is Accepted");
		} catch (Exception e) {
			logger(Status.FAIL, "Alert is not found");
			logger(Status.ERROR, e.getMessage());

		}
	}

	/**
	 * Dismiss alert message
	 */
	public static void dismiss_Alert() {
		WebDriver driver = DriverFactory.getDriver();
		try {
			driver.switchTo().alert().dismiss();
			logger(Status.PASS, "Alert dismissed");
		} catch (Exception e) {
			logger(Status.FAIL, "Alert is not found");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Get alert message
	 *
	 * @return Alert Message
	 */
	public String get_Alert_Text() {
		WebDriver driver = DriverFactory.getDriver();
		String alertText = null;
		try {
			alertText = driver.switchTo().alert().getText();
			logger(Status.PASS, "Alert Text get successful");
		} catch (Exception e) {
			logger(Status.FAIL, "Alert is not found");
			logger(Status.ERROR, e.getMessage());
		}
		return alertText;

	}

	/**
	 * Verify the link has the correct text
	 *
	 * @param element     WebElement (Web/Mobile)
	 * @param compareText text to be validated in the link
	 */
	public static void verify_LinkText(WebElement element, String compareText) {
		try {
			String s = element.getAttribute("href");
			if (s.equalsIgnoreCase(compareText))
				logger(Status.PASS, "Link Text is verified: " + s);
			else
				logger(Status.FAIL, "Link Text is not same: " + s);
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Verify the link has the correct text
	 *
	 * @param webObj      WebElement (Web/Mobile)
	 * @param compareText text to be validated in the link
	 */
	public static void verify_LinkText(WebObj webObj, String compareText) {
		try {
			WebDriver driver = DriverFactory.getDriver();
			String s = driver.findElement(webObj.by).getAttribute("href");
			if (s.equalsIgnoreCase(compareText)) {
				logger(Status.PASS, "Link Text is verified: " + s);
				logger(Status.INFO, "Element validated: " + webObj.desc);
			} else {
				logger(Status.FAIL, "Link Text is not same " + s);
				logger(Status.INFO, "Element validated: " + webObj.desc);
			}
		} catch (Exception e) {
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Scroll to paricular element
	 *
	 * @param element WebElement (Web/Mobile)
	 */
	public static void scroll_Into_Element(WebElement element) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			// logger(Status.INFO, "Page Scrolldown is now completed");
		} catch (Exception e) {
			logger(Status.FAIL, "Page Scrolldown is not completed");
			logger(Status.ERROR, "Page Scrolldown is not completed" + e.getMessage());
		}
	}

	/**
	 * Scroll to paricular element
	 *
	 * @param obj WebElement (Web/Mobile)
	 */
	public static void scroll_Into_Element(WebObj obj) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", driver.findElement(obj.by));
			logger(Status.INFO, "Page Scrolldown is now completed");
		} catch (Exception e) {
			logger(Status.FAIL, "Page Scrolldown is not completed");
			logger(Status.ERROR, "Page Scrolldown is not completed" + e.getMessage());
		}
	}

	public static void scrollDownInPage() {
		WebDriver driver = DriverFactory.getDriver();
		try {
			Dimension dimension = driver.manage().window().getSize();
			int scrollStart = (int) (dimension.getHeight() * 0.5);
			int scrollEnd = (int) (dimension.getHeight() * 0.2);

			new TouchAction((PerformsTouchActions) driver).press(PointOption.point(0, scrollStart))
					.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1))).moveTo(PointOption.point(0, scrollEnd))
					.release().perform();
			intentionalSleep();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Scroll by coordinates
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean scrolling_By_CoordinatesofAPage(int x, int y) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollBy(" + x + "," + y + "");
			logger(Status.PASS, "Page Scrolldown is completed");
		} catch (Exception e) {
			logger(Status.FAIL, "Page Scrolldown is not completed");
			logger(Status.ERROR, "Page Scrolldown is not completed" + e.getMessage());
		}
		return true;
	}

	/**
	 * Retrieve cookie value by name
	 *
	 * @param name
	 * @return
	 */
	public String getValueOfCookieNamed(String name) {
		WebDriver driver = DriverFactory.getDriver();
		String Cookie_name = null;
		try {
			Cookie_name = driver.manage().getCookieNamed(name).getValue();
			logger(Status.PASS, "Cookie value get completed");
		} catch (Exception e) {
			logger(Status.FAIL, "Cookie value is not get completed");
			logger(Status.ERROR, e.getMessage());
		}
		return Cookie_name;

	}

	/**
	 * Add a cookie
	 *
	 * @param name
	 * @param value
	 * @param domain
	 * @param path
	 * @param expiry
	 */
	public static void addCookie(String name, String value, String domain, String path, Date expiry) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			driver.manage().addCookie(new Cookie(name, value, domain, path, expiry));
			logger(Status.PASS, "Cookie is added");
		} catch (Exception e) {
			logger(Status.FAIL, "Cookie is not added");
			logger(Status.ERROR, e.getMessage());
		}

	}

	/**
	 * Deletes all cookies
	 */
	public static void deleteAllCookies() {
		WebDriver driver = DriverFactory.getDriver();
		try {
			driver.manage().deleteAllCookies();
			logger(Status.PASS, "Cookie is deleted");
		} catch (Exception e) {
			logger(Status.FAIL, "Cookie is not deleted");
			logger(Status.ERROR, e.getMessage());
		}
	}

	/**
	 * Deletes a particular cookie
	 *
	 * @param name
	 */
	public static void deleteCookieNamed(String name) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			driver.manage().deleteCookieNamed(name);
			logger(Status.PASS, "Cookie deleted by name");
		} catch (Exception e) {
			logger(Status.FAIL, "Cookie is not deleted by name");
			logger(Status.ERROR, e.getMessage());
		}
	}

	public static Set<Cookie> getAllCookies() {
		WebDriver driver = DriverFactory.getDriver();
		Set<Cookie> Cookie_name = null;
		try {
			Cookie_name = driver.manage().getCookies();
			for (Cookie cookiename : Cookie_name) {
				// if(cookiename.getDomain().contains("businessq2.socalgas.com")) {
				logger(Status.WARNING, "Cookies Name" + cookiename.getName());
				logger(Status.INFO, "Cookies Value" + cookiename.getValue());
				logger(Status.INFO, "Cookies Expiry" + cookiename.getExpiry());
				// }
			}
		} catch (Exception e) {
			logger(Status.FAIL, "Not able to retrieve the cookies");
			logger(Status.ERROR, e.getMessage());
		}

		return Cookie_name;
	}

}
