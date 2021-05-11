package e2e.pagemethodflows;

import com.aventstack.extentreports.Status;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import e2e.framework.Reporter;
import e2e.framework.support.TestDataHolder;
import e2e.libraries.Wrapper;
import e2e.pagemethods.BasePage;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.Map;

public class BaseFlow {

    @Step("Load url")
    public static void loadBaseUrl() {
        BasePage steps = new BasePage();
        steps.launchURL();
        Wrapper.waitForPageLoad();
    }

}
