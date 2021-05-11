package e2e.pagemethods;

import e2e.libraries.WebObj;
import e2e.libraries.Wrapper;

public class PocAAResultPage extends BasePage {

    private String pageName = "pocaaChooseFlight";

    private WebObj flightsearchpage = getLocatorValue("flightsearchpage", pageName);
    private WebObj result1mobile = getLocatorValue("result1mobile", pageName);
    private WebObj flightinfo = getLocatorValue("flightinfo", pageName);
    private WebObj flightinfoprice = getLocatorValue("flightinfoprice", pageName);
    private WebObj flightinfocloseMobile = getLocatorValue("flightinfocloseMobile", pageName);

    public void verifyPageLoad() {
        Wrapper.waitForPageLoad();
        Wrapper.verifyElement(flightsearchpage);
    }

    public void mobileClickFirstPage() {
        if(!currentLocDriver().equalsIgnoreCase("web")) {
            Wrapper.intentionalSleep(5);
            Wrapper.clickElement(result1mobile);
        }
    }

    public void assertFlightInfo() {
        Wrapper.verifyElement(flightinfo);

        if(!currentLocDriver().equalsIgnoreCase("web"))
            Wrapper.asserttextContains(flightinfo, "AA 118");
        else
            Wrapper.asserttextContains(flightinfo, "AA  118");
    }

    public void mobileCloseFlightInfo() {
        if(!currentLocDriver().equalsIgnoreCase("web")) {
            Wrapper.verifyElement(flightinfocloseMobile);
            Wrapper.clickElement(flightinfocloseMobile);
        }
    }

    public void assertFlightPrice() {
        Wrapper.verifyElement(flightsearchpage);
        Wrapper.verifyElement(flightinfoprice);
        Wrapper.asserttextContains(flightinfoprice, "434");
    }
}
