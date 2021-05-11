package e2e.framework;

import java.util.ArrayList;
import java.util.Map;

public interface ExcelScanner {

    void assignSuiteName(String suiteName);

    void assignGroupIncludes(String groupName);

    void assignGroupExcludes(String groupName);

    void addToClasses(String className);

    boolean checkClassList(String className);

    void setTestCombo(Map<String, Map<String, Map<String, ArrayList<Map<String, String>>>>> testsComb);
}