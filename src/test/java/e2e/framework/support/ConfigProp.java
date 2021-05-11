package e2e.framework.support;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigProp {

    public static Properties prop = new Properties();
    public static String filepath;

    static {
        try {
            System.out.println("Config :" + System.getProperty("basedir", ".") + File.separator + System.getProperty("config"));
            filepath = System.getProperty("basedir", ".") + File.separator + System.getProperty("config");
            prop.load(new FileInputStream(filepath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPropertyValue(String key) {
        return prop.getProperty(key);
    }
}
