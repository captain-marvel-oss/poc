package e2e.framework.support;

import java.util.Map;

public class TestDataHolder {
    private static ThreadLocal<Map<String, String>> testDataMap = new ThreadLocal<Map<String, String>>();

    public static void init(Map<String, String> configMap) {
        TestDataHolder.testDataMap.set(configMap);
    }

    public static String getData(String key) {
        String Nkey = "";
        Nkey = key;

        if (TestDataHolder.testDataMap.get().containsKey(Nkey)) {
            return TestDataHolder.testDataMap.get().get(Nkey);
        } else {
            return TestDataHolder.testDataMap.get().get(key);
        }

    }

    public static void endThreadLocal() {
        testDataMap.remove();
    }
}
