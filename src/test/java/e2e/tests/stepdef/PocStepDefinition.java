package e2e.tests.stepdef;

import e2e.pagemethods.TraHomePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import e2e.drivers.DriverFactory;
import e2e.drivers.SoftAssertion;
import e2e.framework.support.ConfigProp;
import e2e.libraries.ObjectLookup;
import e2e.libraries.Wrapper;
import e2e.pagemethods.SingtelHomePage;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;
// import e2e.tests.BddTests;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.qameta.allure.Step;

public class PocStepDefinition {

    public Scenario thisscenario;

    @Before
    public void beforeScenario(Scenario scenario) {
        thisscenario = scenario;
        SoftAssertion.beginSoftAssertion();
    }

    public void addScreenshot() {
        try {
            // System.out.println("WHERE AM I?: " + scenario.getId());
            WebDriver driver = DriverFactory.getDriver();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            thisscenario.embed(screenshot, "image/png");
            // System.out.println(screenshot.toString());
        } catch (Exception cce) {
            cce.printStackTrace();
        }
        SoftAssertion.endSoftAssertion();
        SoftAssertion.beginSoftAssertion();
    }

    @Step("Web App Loads Successfully")
    @Given("Web App Loads Successfully")
    public void loadWebApp() {
        DriverFactory.getDriver();
        if (!ObjectLookup.currentLocDriver().equalsIgnoreCase("andapp")) {
            Wrapper.launchbaseURL(ConfigProp.getPropertyValue("url"));
        }
        addScreenshot();
    }

    @Step("Click on search button")
    @And("Click on search button")
    public void clickSearchButton() {
        SingtelHomePage singtelHomePage = new SingtelHomePage();
        singtelHomePage.clickSearchIcon();
        addScreenshot();
    }

    @Step("I am able to enter SearchTerm")
    @And("I am able to enter SearchTerm")
    public void enterSearchTerm() {
        SingtelHomePage singtelHomePage = new SingtelHomePage();
        singtelHomePage.enterSearchText();
        addScreenshot();
    }

    @Step("Verify search results page")
    @And("Verify search results page")
    public void verifySearchResultsPage() {
        SingtelHomePage singtelHomePage = new SingtelHomePage();
        singtelHomePage.verifySearchResultsPage();
        addScreenshot();
    }

    @Step("Click on shop button")
    @And("Click on shop button")
    public void clickShopButton() {
        SingtelHomePage singtelHomePage = new SingtelHomePage();
        singtelHomePage.clickShopButton();
        addScreenshot();
    }

    @Step("Select product from filter")
    @And("Select product from filter")
    public void selectProduct() {
        SingtelHomePage singtelHomePage = new SingtelHomePage();
        singtelHomePage.selectFilter();
        addScreenshot();
    }

    @Step("Verify product details page")
    @And("Verify product details page")
    public void verifyProduct() {
        SingtelHomePage singtelHomePage = new SingtelHomePage();
        singtelHomePage.verifyProductDetails();
        addScreenshot();
    }

    @Given("Maxis public website is loaded successfully")
    public void maxisPublicWebsiteIsLoadedSuccessfully() {
        TraHomePage traHomePage = new TraHomePage();
        DriverFactory.getDriver();
//        traHomePage.loadHomePage(url);
    }

    @When("I as a consumer user navigates to the Devices Section")
    public void aConsumerUserNavigatesToTheDevicesSection() {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.navigateToDeviceSection();
    }

    @Then("I am able to view the devices subsection {string}")
    public void iAmAbleToViewTheDevicesSubsectionSubsection(String subsection) {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.selectSubsection(subsection);
    }

    @When("I click or tap on the {string} the corresponding {string} is displayed")
    public void iClickTapOnTheTheCorrespondingIsDisplayed(String subsection, String device) {
        System.out.println("Subsection " + subsection);
        System.out.println("Device " + device);
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.validateDeviceInSubsection(subsection, device);
    }

    @When("I as a consumer user navigates to the Lifestyle Section")
    public void iAsAConsumerUserNavigatesToTheLifestyleSection() {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.navigateToLifeStyleSection();
    }

    @Then("I am able to view Lifestlye Section")
    public void iAmAbleToViewLifestlyeSection() {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.validateLifeStyleSection();
    }

    @Given("TRA public website is loaded successfully")
    public void TRAPublicWebsiteIsLoadedSuccessfully() {
        TraHomePage traHomePage = new TraHomePage();
        DriverFactory.getDriver();
        traHomePage.loadHomePage();
    }

    @When("I as a public user navigates to the TRA in Numbers Section")
    public void iAsAPublicUserNavigatesToTheTRAInNumbersSection() {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.navigateToTRAInNumbers();
    }

    @And("I select Market Information Option")
    public void iSelectMarketInformationOption() {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.selectMarketInfoOption();
    }

    @Then("I am able to see the Market Information sub options")
    public void iAmAbleToSeeTheMarketInformationSubOptions() {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.markInfoSubOption();
    }

    @When("I as a public user click on the search icon")
    public void iAsAPublicUserClickOnTheSearchIcon() {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.clickOnSearchIcon();
    }

    @And("I am able to view the search options")
    public void iAmAbleToViewTheSearchOptions() {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.validateSearchOption();
    }

    @And("I am able to enter the key in the search text")
    public void iAmAbleToEnterTheKeyInTheSearchText() {
        TraHomePage traHomePage = new TraHomePage();
        traHomePage.enterSearchText();
    }
}