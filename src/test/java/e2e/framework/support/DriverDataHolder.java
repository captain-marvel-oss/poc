package e2e.framework.support;

import java.util.Map;

public class DriverDataHolder {
    private static ThreadLocal<Map<String, String>> configMap = new ThreadLocal<Map<String, String>>();

    public static void init(Map<String, String> configMap) {
        DriverDataHolder.configMap.set(configMap);
    }

    public static String getValue(String key) {
        return DriverDataHolder.configMap.get().get(key);
    }

    public static void endThreadLocal() {
        configMap.remove();
    }
}
