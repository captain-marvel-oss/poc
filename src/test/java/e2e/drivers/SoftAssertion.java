package e2e.drivers;

import org.testng.asserts.SoftAssert;

public class SoftAssertion {

    private static ThreadLocal<SoftAssert> softAssert = new ThreadLocal<>();

    public static void beginSoftAssertion() {
        softAssert.set(new SoftAssert());
    }

    public static void addFailure(String str) {
        SoftAssert sa = softAssert.get();
        // sa.assertFalse(true, str);
        sa.fail(str);
    }

    public static void endSoftAssertion() {
        softAssert.get().assertAll();
        softAssert.remove();
    }
}
