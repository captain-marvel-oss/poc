package e2e.tests;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aventstack.extentreports.Status;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import e2e.framework.ExcelReader;
import e2e.framework.Reporter;
import gherkin.pickles.PickleStep;
import io.cucumber.testng.CucumberFeatureWrapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleEventWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

@CucumberOptions(features = "Features/", glue = { "e2e.tests.stepdef" }, tags = {
        "@mobileapp" }, strict = true, monochrome = true, plugin = { "pretty",
                "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm", "html:target/cucumber-reports/cucumber-pretty",
                "json:target/cucumber-reports/CucumberTestReport.json", "rerun:target/cucumber-reports/rerun.txt" })
public class MobileAppTests extends BddTestNG {

    private TestNGCucumberRunner testNGCucumberRunner;
    private Set<String> feature_name = new HashSet<>();

    @BeforeClass(alwaysRun = true)
    public void setUpClass(ITestContext context) {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        try {
            testNGCucumberRunner.finish();
        } catch (Exception e) {
            // e.printStackTrace();
            e.getMessage();
        }
    }

    @DataProvider(name = "test_bdd", parallel = true)
    public Object[][] GlobalTestData1(Method method, ITestContext context) {
        System.out.println("INto DP");
        ArrayList<Map<String, String>> browArray = new ArrayList<Map<String, String>>();
        ArrayList<Map<String, String>> DataArray = new ArrayList<Map<String, String>>();

        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        browArray = ExcelReader.getBrowsersDeviceComb(context.getCurrentXmlTest().getName(), className, methodName);
        DataArray = ExcelReader.getTestDataIterator(methodName, className);

        System.out.println("Data Read from Test Pack Selector: " + browArray);
        System.out.println("Data Read from Test Data File: " + DataArray);
        int i = 0;
        int count = browArray.size() * DataArray.size();

        System.out.println("Total Number of Combination to be ran over test: " + count);

        Object[][] test = new Object[count][2];
        try {
            System.out.println("CHECK: " + Arrays.stream(testNGCucumberRunner.provideScenarios()).iterator());
            Iterator it = Arrays.stream(testNGCucumberRunner.provideScenarios()).iterator();
            while (it.hasNext())
                System.out.println(it.next().getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Map<String, String> elem : browArray) {
            for (Map<String, String> entryD : DataArray) {
                test[i][0] = elem;
                test[i][1] = entryD;
                i++;
            }
        }

        Object[][] comb = testNGCucumberRunner.provideScenarios();

        List<Object[]> objectCodesList = new LinkedList<Object[]>();
        for (Object[] o : test) {
            for (Object[] o2 : comb) {
                objectCodesList.add(concatAll(o, o2));
            }
        }
        return objectCodesList.toArray(new Object[0][0]);
        // return test;

    }

    @SafeVarargs
    static <T> T[] concatAll(T[] first, T[]... rest) {
        // calculate the total length of the final object array after the concat
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        // copy the first array to result array and then copy each array completely to
        // result
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    @Test(dataProvider = "test_bdd")
    public void DemoMobile(Map<String, String> driverDetails, Map<String, String> data, PickleEventWrapper pickleEvent,
            CucumberFeatureWrapper cucumberFeature) {
        try {
            System.out.println("Inside Tests");
            System.out.println("URI: " + pickleEvent.getPickleEvent().pickle.getName());
            System.out.println("URI: " + pickleEvent.getPickleEvent().pickle.getLanguage());
            for (PickleStep i : pickleEvent.getPickleEvent().pickle.getSteps()) {
                System.out.println(i.getText());
            }
            System.out.println(pickleEvent.getPickleEvent().uri);
            String featureref = pickleEvent.getPickleEvent().uri;
            if (!feature_name.contains(featureref)) {
                feature_name.add(featureref);
            }
            testNGCucumberRunner.runScenario(pickleEvent.getPickleEvent());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Reporter.log(Status.FAIL, "Error");
            Assert.fail();
        }
    }

}
