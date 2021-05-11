package e2e.framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.gherkin.model.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import e2e.drivers.SoftAssertion;
import e2e.framework.support.ConfigProp;
import e2e.framework.support.DriverDataHolder;
import e2e.libraries.Wrapper;
import e2e.tests.BddTestNG;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class Reporter {

    public static String re_testname;
    public static String re_suite;

    public static String foldername = "finalreport";
    public static String reportpath = System.getProperty("basedir", ".") + File.separator + "Reports" + File.separator
            + foldername + File.separator;
    public static String reportconfig = System.getProperty("basedir", ".") + File.separator + "src" + File.separator
            + "test" + File.separator + "resources" + File.separator + "ReportConfig.xml";

    public static void initReport(String suite) {
        re_suite = suite;
        try {
            System.out.println("Report PATH INIT: " + reportpath);
            System.out.println("SUITE: " + suite);
            if (suite.equalsIgnoreCase("Mobile")) {
                Allure.label("Platform", "Mobile Web");
            } else if (suite.equalsIgnoreCase("iOS_APP")) {
                Allure.label("Platform", "iOS Mobile App");
            } else if (suite.equalsIgnoreCase("Android_APP")) {
                Allure.label("Platform", "Android Mobile App");
            } else {
                Allure.label("Platform", "Desktop Web");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void assignCategory(String label, String value) {
        Allure.label(label, value);
    }

    @Step("Log: {step}")
    public static void log(Status logstatus, String step) {
        Path content;
        InputStream is;
        try {
            switch (logstatus) {
                case INFO:
                    // currtest.get().log(logstatus, step);
                    // currtest_withscreen.get().log(logstatus, step);
                    Allure.step(step, io.qameta.allure.model.Status.PASSED);
                    break;
                case SKIP:
                    // currtest.get().log(logstatus, step);
                    // currtest_withscreen.get().log(logstatus, step);
                    Allure.step(step, io.qameta.allure.model.Status.SKIPPED);
                    break;
                case PASS:
                    // currtest.get().log(logstatus, step);
                    // currtest_withscreen.get().log(logstatus, step);
                    Allure.step(step, io.qameta.allure.model.Status.PASSED);
                    // content = Paths.get(Wrapper.captureScreen(BddTestNG.test_name_screenshot));
                    // is = Files.newInputStream(content);
                    // Allure.addAttachment("PASSED : " + step, is);
                    // if (ConfigProp.getPropertyValue("passed").equalsIgnoreCase("yes")) {
                    // Wrapper.saveScreenshotPNG();
                    // }
                    break;
                case WARNING:
                case FAIL:
                    io.qameta.allure.model.Status failed = io.qameta.allure.model.Status.FAILED;
                    // currtest.get().log(logstatus, step);
                    Allure.step(step, failed);
                    content = Paths.get(Wrapper.captureScreen(BddTestNG.test_name_screenshot));
                    is = Files.newInputStream(content);
                    Allure.addAttachment("FAILED : " + step, is);
                    Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(failed));
                    Allure.getLifecycle().stopStep();
                    Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(failed));
                    Allure.getLifecycle().stopStep();
                    Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(failed));
                    Allure.getLifecycle().stopStep();
                    Allure.getLifecycle().updateTestCase(testResult -> testResult.setStatus(failed));
                    SoftAssertion.addFailure(step);
                    // currtest_withscreen.get().log(logstatus, step);
                    // currtest_withscreen.get().info("",
                    // MediaEntityBuilder.createScreenCaptureFromPath(Wrapper.captureScreen(path)).build());
                    break;
                case ERROR:
                case FATAL:
                    failed = io.qameta.allure.model.Status.FAILED;
                    content = Paths.get(Wrapper.captureScreen(BddTestNG.test_name_screenshot));
                    is = Files.newInputStream(content);
                    Allure.addAttachment("FAILED : " + step, is);
                    Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(failed));
                    Allure.getLifecycle().stopStep();
                    Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(failed));
                    Allure.getLifecycle().stopStep();
                    Allure.getLifecycle().updateStep(stepResult -> stepResult.setStatus(failed));
                    Allure.getLifecycle().stopStep();
                    Allure.getLifecycle().updateTestCase(testResult -> testResult.setStatus(failed));
                    Assert.fail(step);
                    break;
                default:
                    throw (new Exception("Missing status configuration"));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}