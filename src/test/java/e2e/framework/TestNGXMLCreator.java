package e2e.framework;

import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TestNGXMLCreator implements ExcelScanner {

    private static Map<String, Map<String, Map<String, ArrayList<Map<String, String>>>>> testsComb = new HashMap<String, Map<String, Map<String, ArrayList<Map<String, String>>>>>();
    private static XmlSuite suiteD, suiteM, suiteiOSApp, suiteAndApp;

    private static XmlTest test;

    private static ArrayList<String> classesList = new ArrayList<>();

    private static XmlClass xmlClaz;
    private static List<XmlInclude> methodIncludes = new ArrayList<XmlInclude>();
    private static List<XmlClass> xmlClassesList = new ArrayList<XmlClass>();

    private static String basepkg = "e2e.tests";

    public static void main(String[] args) {
        System.out.println("XML GENERATOR KICKOFF");
        xmlInit();
        ExcelReader xlReader = new ExcelReader(new TestNGXMLCreator());
        xlReader.scanExcel();
        writeTestComb();
        suiteGen();
        String xmlD = getXMLD();
        writeToXMLD(xmlD);

        String xmlM = getXMLM();
        writeToXMLM(xmlM);

        String xmlMapp = getXMLMapp();
        writeToXMLMapp(xmlMapp);

        String xmlMappAnd = getXMLMappAnd();
        writeToXMLMappAnd(xmlMappAnd);
    }

    private static void xmlInit() {
        suiteD = new XmlSuite();
        suiteD.setParallel(XmlSuite.DEFAULT_PARALLEL);
        suiteD.setThreadCount(1);
        suiteD.setDataProviderThreadCount(2);

        suiteM = new XmlSuite();
        suiteM.setParallel(XmlSuite.DEFAULT_PARALLEL);
        suiteM.setThreadCount(12);
        suiteM.setDataProviderThreadCount(1);

        suiteiOSApp = new XmlSuite();
        suiteiOSApp.setParallel(XmlSuite.DEFAULT_PARALLEL);
        suiteiOSApp.setThreadCount(12);
        suiteiOSApp.setDataProviderThreadCount(1);

        suiteAndApp = new XmlSuite();
        suiteAndApp.setParallel(XmlSuite.ParallelMode.TESTS);
        suiteAndApp.setThreadCount(12);
        suiteAndApp.setDataProviderThreadCount(1);

        System.out.println("Initiated Suite XML : " + suiteD);
        System.out.println(suiteM);
        System.out.println(suiteiOSApp);
        System.out.println(suiteAndApp);
    }

    private static void suiteGen() {

        for (Entry<String, Map<String, Map<String, ArrayList<Map<String, String>>>>> tests : testsComb.entrySet()) {
            String testName = tests.getKey();
            System.out.println("Test Comb" + testName);
            System.out.println("Test Comb" + testsComb.entrySet());
            if (testName.equalsIgnoreCase("Browser")) {
                test = new XmlTest(suiteD);
                test.setName(testName);
                test.setThreadCount(2);
            } else if (testName.contains("MApp") && testName.contains("Android")) {
                test = new XmlTest(suiteAndApp);
                test.setName(testName);
                test.setThreadCount(1);
            } else if (testName.contains("MApp") && testName.contains("iOS")) {
                test = new XmlTest(suiteiOSApp);
                test.setName(testName);
                test.setThreadCount(1);
            } else {
                test = new XmlTest(suiteM);
                test.setName(testName);
                test.setThreadCount(1);
            }
            // test.setParallel(XmlSuite.DEFAULT_PARALLEL);
            test.setParallel(XmlSuite.ParallelMode.METHODS);
            xmlClassesList = new ArrayList<XmlClass>();
            for (Entry<String, Map<String, ArrayList<Map<String, String>>>> classes : tests.getValue().entrySet()) {
                String className = classes.getKey();
                xmlClaz = new XmlClass(basepkg + "." + className);
                for (Entry<String, ArrayList<Map<String, String>>> methods : classes.getValue().entrySet()) {
                    methodIncludes = xmlClaz.getIncludedMethods();
                    String methodName = methods.getKey();
                    methodIncludes.add(new XmlInclude(methodName));
                }
                xmlClassesList.add(xmlClaz);
            }
            test.setXmlClasses(xmlClassesList);
        }

    }

    private static void writeTestComb() {
        String serPath = System.getProperty("basedir", ".") + File.separator + "TestComb.json";
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(serPath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(testsComb);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String getXMLD() {
        return suiteD.toXml();
    }

    private static String getXMLM() {
        return suiteM.toXml();
    }

    private static String getXMLMapp() {
        return suiteiOSApp.toXml();
    }

    private static String getXMLMappAnd() {
        return suiteAndApp.toXml();
    }

    private static void writeToXMLD(String xml) {
        // System.out.println(xml);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("TestNgD.xml"), "utf-8"))) {
            writer.write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToXMLM(String xml) {
        // System.out.println(xml);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("TestNgM.xml"), "utf-8"))) {
            writer.write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToXMLMapp(String xml) {
        // System.out.println(xml);
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("TestNgMapp.xml"), "utf-8"))) {
            writer.write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToXMLMappAnd(String xml) {
        // System.out.println(xml);
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("TestNgMappAnd.xml"), "utf-8"))) {
            writer.write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void assignSuiteName(String suiteName) {
        suiteD.setName("Desktop");
        suiteM.setName("Mobile");
        suiteiOSApp.setName("iOS_APP");
        suiteAndApp.setName("Android_APP");
    }

    @Override
    public void assignGroupIncludes(String groupName) {
        test.addIncludedGroup(groupName);
    }

    @Override
    public void assignGroupExcludes(String groupName) {
        test.addExcludedGroup(groupName);

    }

    @Override
    public void addToClasses(String className) {
        classesList.add(className);
    }

    @Override
    public boolean checkClassList(String className) {
        return classesList.contains(className);
    }

    @Override
    public void setTestCombo(Map<String, Map<String, Map<String, ArrayList<Map<String, String>>>>> testsComb) {
        TestNGXMLCreator.testsComb = testsComb;
    }

}
