package e2e.tests;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import e2e.drivers.DriverFactory;
import e2e.framework.Reporter;
import e2e.framework.support.ConfigProp;
import e2e.framework.support.DriverDataHolder;
import e2e.framework.support.TestDataHolder;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.GsonBuilder;
import gherkin.deps.com.google.gson.JsonElement;
import gherkin.deps.com.google.gson.JsonParser;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class BddTestNG {

    public static String test_name_screenshot;
    int count = 0;

    @io.cucumber.java.Before
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        String suiteName = context.getCurrentXmlTest().getSuite().getName();
        //System.setProperty("webdriver.chrome.driver","/Users/sundar.siva/Downloads/chromedriver_5");
        Reporter.initReport(suiteName);
    }

    @io.cucumber.java.Before
    @BeforeMethod(alwaysRun = true)
    public void setUp(Object[] testArgs, ITestContext context, Method method) {
        DriverDataHolder.init((Map<String, String>) testArgs[0]);
        TestDataHolder.init((Map<String, String>) testArgs[1]);
        String testName = method.getName() + "-" + DriverDataHolder.getValue("Browser");
        System.out.println("TestName: " + testName);
        try {
            testName = TestDataHolder.getData("DESCRIPTION");
            test_name_screenshot = testName + "_" + RandomStringUtils.random(5, true, true);
            // Reporter.initTest(context.getCurrentXmlTest().getSuite().getName(),
            // testName);
            if (count == 0) {
                // Reporter.startFeature(method.getName());
                // Reporter.initTest(context.getCurrentXmlTest().getSuite().getName(),
                // testName);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @io.cucumber.java.After
    @AfterMethod(alwaysRun = true)
    public void closeDriver(ITestContext context) {
        DriverFactory.closeDriver();
        DriverDataHolder.endThreadLocal();
        TestDataHolder.endThreadLocal();
    }

    @io.cucumber.java.After
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        prepareCucumberReport();
        prepareAllureReport();
        prepareJiraIntegration();
        if (ConfigProp.getPropertyValue("jiraIntegration").equalsIgnoreCase("true"))
            getFailedCucumberReport();

    }

    private void prepareJiraIntegration() {
        try {
            if (ConfigProp.getPropertyValue("jiraIntegration").equalsIgnoreCase("true")) {
                JSONParser jsonParser = new JSONParser();
                FileReader reader = new FileReader(
                        new File(System.getProperty("basedir", ".") + File.separator + "atom_environment.json"));
                JSONObject obj = (JSONObject) jsonParser.parse(reader);
                JSONArray valueArray = (JSONArray) obj.get("values");
                for (int i = 0; i < valueArray.size(); i++) {
                    JSONObject each = (JSONObject) valueArray.get(i);
                    String key = each.get("key").toString();
                    if (key.equalsIgnoreCase("accessKey")) {
                        each.put("value", ConfigProp.getPropertyValue("accessKey"));
                    }
                    if (key.equalsIgnoreCase("secretKey")) {
                        each.put("value", ConfigProp.getPropertyValue("secretKey"));
                    }
                    if (key.equalsIgnoreCase("accountId")) {
                        each.put("value", ConfigProp.getPropertyValue("accountId"));
                    }
                    if (key.equalsIgnoreCase("file")) {
                        String abosultePath = System.getProperty("basedir", ".") + File.separator
                                + ConfigProp.getPropertyValue("file");
                        each.put("value", abosultePath);
                    }
                    if (key.equalsIgnoreCase("projectKey")) {
                        each.put("value", ConfigProp.getPropertyValue("projectKey"));
                    }
                    if (key.equalsIgnoreCase("versionName")) {
                        each.put("value", ConfigProp.getPropertyValue("versionName"));
                    }
                    if (key.equalsIgnoreCase("folderName")) {
                        each.put("value", System.getProperty("folder"));
                    }
                    if (key.equalsIgnoreCase("buildNo")) {
                        each.put("value", System.getProperty("buildNumber"));
                    }
                }
                FileWriter file = new FileWriter(
                        new File(System.getProperty("basedir", ".") + File.separator + "atom_environment.json"));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(obj.toString());
                String prettyJsonString = gson.toJson(je);
                file.write(prettyJsonString);
                file.flush();
                file.close();
            }

        } catch (Exception ie) {
            ie.printStackTrace();
        }
    }

    private void prepareAllureReport() {
        File dir = new File(System.getProperty("basedir", ".") + File.separator + "allure-results");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.getName().contains("result.json")) {
                    // System.out.println("File Name: " + child.getName());
                    JSONParser jsonParser = new JSONParser();
                    try (FileReader reader = new FileReader(child.getAbsolutePath())) {
                        String status = "passed";
                        Object obj = jsonParser.parse(reader);
                        JSONArray stepslist = (JSONArray) ((JSONObject) obj).get("steps");
                        int j = stepslist.size();

                        if (j == 0)
                            ((JSONObject) obj).put("status", "failed");
                        else {
                            for (int k = 0; k < j; k++) {
                                JSONArray attachments = (JSONArray) ((JSONObject) stepslist.get(k)).get("attachments");
                                // System.out.println("ATTACHMENT: " + attachments);
                                if (attachments.size() == 3) {
                                    attachments.remove(2);
                                    attachments.remove(1);
                                }
                            }

                            for (int i = 0; i < j; i++) {
                                String inner = (String) ((JSONObject) stepslist.get(i)).get("stage");
                                String istatus = (String) ((JSONObject) stepslist.get(i)).get("status");
                                // System.out.println("INNER: " + inner);
                                if (inner.equals("running")) {
                                    stepslist.remove(i);
                                    i--;
                                    j--;
                                } else {
                                    if (!istatus.equals("passed")) {
                                        status = "failed";
                                    }
                                }
                            }
                            ((JSONObject) obj).remove("steps");
                            ((JSONObject) obj).put("steps", stepslist);
                            ((JSONObject) obj).put("status", status);
                        }
                        try (FileWriter file = new FileWriter(child.getAbsolutePath())) {
                            file.write(obj.toString());
                            file.flush();
                        } catch (Exception ie) {
                            ie.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Error updating the result files");
        }
    }

    private void prepareCucumberReport() {
        try {
            try {
                JSONParser jsonParser = new JSONParser();
                FileReader reader;
                reader = new FileReader(new File(System.getProperty("basedir", ".") + File.separator + "target"
                        + File.separator + "cucumber-reports" + File.separator + "CucumberTestReport.json"));

                Object obj = jsonParser.parse(reader);
                JSONArray stepslist = (JSONArray) obj;
                org.json.simple.JSONObject jsonOnj = ((org.json.simple.JSONObject) stepslist.get(0));
                JSONArray elementArray = (JSONArray) jsonOnj.get("elements");
                for (int i = 0; i < elementArray.size(); i++) {
                    org.json.simple.JSONObject jsonObj = ((org.json.simple.JSONObject) elementArray.get(i));
                    if (((JSONArray) jsonObj.get("steps")).size() == 0) {
                        elementArray.remove(i);
                        i--;
                    } else {
                        JSONArray stepsArray = (JSONArray) jsonObj.get("steps");
                        for (int j = 0; j < stepsArray.size(); j++) {
                            org.json.simple.JSONObject object = ((org.json.simple.JSONObject) stepsArray.get(j));
                            if (object.get("result") == null) {
                                stepsArray.remove(j);
                                j--;
                            } else {
                                if ((JSONArray) object.get("embeddings") != null) {

                                    if (((JSONArray) object.get("embeddings")).size() == 3) {
                                        JSONArray embedArray = (JSONArray) object.get("embeddings");
                                        embedArray.remove(0);
                                        embedArray.remove(1);

                                    }
                                }
                            }
                        }
                    }

                    if ((JSONArray) jsonObj.get("before") != null) {
                        JSONArray beforeArray = (JSONArray) jsonObj.get("before");
                        for (int k = 0; k < beforeArray.size(); k++) {
                            org.json.simple.JSONObject object = ((org.json.simple.JSONObject) beforeArray.get(k));
                            if (object.get("result") == null) {
                                beforeArray.remove(k);
                                k--;
                            } else {
                                if ((JSONArray) object.get("embeddings") != null) {

                                    if (((JSONArray) object.get("embeddings")).size() == 3) {
                                        JSONArray embedArray = (JSONArray) object.get("embeddings");
                                        embedArray.remove(0);
                                        embedArray.remove(1);

                                    }
                                }
                            }
                        }

                    }
                    if ((JSONArray) jsonObj.get("after") != null) {
                        JSONArray beforeArray = (JSONArray) jsonObj.get("after");
                        for (int k = 0; k < beforeArray.size(); k++) {
                            org.json.simple.JSONObject object = ((org.json.simple.JSONObject) beforeArray.get(k));
                            if (object.get("result") == null) {
                                beforeArray.remove(k);
                                k--;
                            } else {
                                if ((JSONArray) object.get("embeddings") != null) {
                                    if (((JSONArray) object.get("embeddings")).size() == 3) {
                                        JSONArray embedArray = (JSONArray) object.get("embeddings");
                                        embedArray.remove(0);
                                        embedArray.remove(1);
                                    }
                                }
                            }
                        }

                    }
                }
                FileWriter file = new FileWriter(new File(System.getProperty("basedir", ".") + File.separator + "target"
                        + File.separator + "cucumber-reports" + File.separator + "CucumberTestReport.json"));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(obj.toString());
                String prettyJsonString = gson.toJson(je);
                file.write(prettyJsonString);
                file.flush();
            } catch (Exception ie) {
                ie.printStackTrace();
            }

        } catch (Exception ie) {
            ie.printStackTrace();
        }
    }

    private void getFailedCucumberReport() {
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader;
            reader = new FileReader(new File(System.getProperty("basedir", ".") + File.separator + "target"
                    + File.separator + "cucumber-reports" + File.separator + "CucumberTestReport.json"));
            Object obj = jsonParser.parse(reader);
            JSONArray stepslist = (JSONArray) obj;
            JSONObject jsonOnj = ((JSONObject) stepslist.get(0));
            JSONArray elementArray = (JSONArray) jsonOnj.get("elements");
            for (int i = 0; i < elementArray.size(); i++) {
                JSONObject jsonObj = ((JSONObject) elementArray.get(i));
                JSONArray stepsArray = (JSONArray) jsonObj.get("steps");
                String scenarioName = jsonObj.get("name").toString();
                for (int j = 0; j < stepsArray.size(); j++) {
                    JSONObject object = ((JSONObject) stepsArray.get(j));
                    JSONObject resultobject = (JSONObject) object.get("result");
                    if (resultobject.get("status").toString().equalsIgnoreCase("failed")) {
                        String errorMessage = resultobject.get("error_message").toString();
                        checkDefectExists(scenarioName, errorMessage);
                    }
                }
            }
        } catch (Exception ie) {
            ie.printStackTrace();
        }
    }

    private void checkDefectExists(String scenarioName, String errorMessage) {
        Unirest.config().verifySsl(false);
        String jiraUrl = ConfigProp.getPropertyValue("jiraUrl");
        String username = ConfigProp.getPropertyValue("jiraUsername");
        String apiToken = ConfigProp.getPropertyValue("jiraAPIToken");
        String projectKey = ConfigProp.getPropertyValue("projectKey");

        HttpResponse<JsonNode> response = Unirest
                .get(jiraUrl + "/rest/api/2/search?jql=project=" + projectKey
                        + "&fields=key,summary,description,issuetype&maxResults=100")
                .basicAuth(username, apiToken).asJson();
        System.out.println(response.getBody().toPrettyString());
        kong.unirest.json.JSONObject jsonObj = response.getBody().getObject();
        kong.unirest.json.JSONArray issueArray = jsonObj.getJSONArray("issues");
        String issueKey = "";
        String testKey = "Test not present";
        boolean defectExists = false;
        for (int i = 0; i < issueArray.length(); i++) {
            kong.unirest.json.JSONObject each = issueArray.getJSONObject(i);
            testKey = each.getString("key");
            kong.unirest.json.JSONObject fields = each.getJSONObject("fields");
            kong.unirest.json.JSONObject issueType = fields.getJSONObject("issuetype");
            String summary = fields.getString("summary");
            if (issueType.getString("name").equalsIgnoreCase("Test")) {
                if (summary.equalsIgnoreCase(scenarioName)) {
                    break;
                }
            }
        }
        for (int i = 0; i < issueArray.length(); i++) {
            kong.unirest.json.JSONObject each = issueArray.getJSONObject(i);
            issueKey = each.getString("key");
            kong.unirest.json.JSONObject fields = each.getJSONObject("fields");
            kong.unirest.json.JSONObject issueType = fields.getJSONObject("issuetype");
            String summary = fields.getString("summary");
            if (issueType.getString("name").equalsIgnoreCase("Bug")) {
                if (summary.equalsIgnoreCase(scenarioName)) {
                    defectExists = true;
                    break;
                }
            }
        }
        if (defectExists) {
            updateDefectComment(issueKey);
        } else {
            createNewDefect(scenarioName, errorMessage, testKey);
        }
    }

    private void updateDefectComment(String issueKey) {
        Unirest.config().verifySsl(false);
        String jiraUrl = ConfigProp.getPropertyValue("jiraUrl");
        String username = ConfigProp.getPropertyValue("jiraUsername");
        String apiToken = ConfigProp.getPropertyValue("jiraAPIToken");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dateString = formatter.format(date);
        String comment = "Issue still exists in Cycle: Build" + System.getProperty("buildNumber") + " executed at :"
                + dateString;
        HttpResponse<String> response = Unirest.post(jiraUrl + "/rest/api/2/issue/" + issueKey + "/comment")
                .basicAuth(username, apiToken).header("Content-Type", "application/json")
                .body("{    \"body\": \"" + comment + "\"}").asString();

        if (response.getStatus() == 201) {
            System.out.println("Defect comment updated -" + issueKey);
        }
    }

    private void createNewDefect(String scenarioName, String errorMessage, String testKey) {
        Unirest.config().verifySsl(false);
        String jiraUrl = ConfigProp.getPropertyValue("jiraUrl");
        String username = ConfigProp.getPropertyValue("jiraUsername");
        String apiToken = ConfigProp.getPropertyValue("jiraAPIToken");
        String projectKey = ConfigProp.getPropertyValue("projectKey");
        String label = System.getProperty("folder");
        errorMessage = errorMessage.replaceAll("[\\n\\r\\t]+", "");
        errorMessage = StringEscapeUtils.unescapeJava(errorMessage);
        errorMessage = errorMessage.replaceAll("\"", "\'");
        System.out.println(errorMessage);
        String description = "Test failed due to below reason :" + errorMessage;
        String body = "{\"fields\":{\"summary\":\"" + scenarioName
                + "\",\"issuetype\":{\"id\":\"10004\"},\"project\":{\"key\":\"" + projectKey + "\"},\"labels\":[\""
                + label
                + "\"],\"versions\":[{\"id\":\"10000\"}],\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\",\"content\":[{\"text\":\""
                + description + "\",\"type\":\"text\"}]}]}}}";
        HttpResponse<JsonNode> response = Unirest.post(jiraUrl + "/rest/api/3/issue").basicAuth(username, apiToken)
                .header("Content-Type", "application/json").body(body).asJson();
        System.out.println(response.getBody().toPrettyString());
        if (response.getStatus() == 201) {
            kong.unirest.json.JSONObject jsonObj = response.getBody().getObject();
            System.out.println("Defect created: " + jsonObj.getString("key"));
            if (!testKey.equalsIgnoreCase("Test not present")) {
                linkDefectwithTest(testKey, jsonObj.getString("key"));
            }
        }
    }

    private void linkDefectwithTest(String testKey, String issueKey) {
        Unirest.config().verifySsl(false);
        String jiraUrl = ConfigProp.getPropertyValue("jiraUrl");
        String username = ConfigProp.getPropertyValue("jiraUsername");
        String apiToken = ConfigProp.getPropertyValue("jiraAPIToken");
        HttpResponse<String> response = Unirest.post(jiraUrl + "/rest/api/3/issueLink").basicAuth(username, apiToken)
                .header("Content-Type", "application/json")
                .body("{\n  \"outwardIssue\": {\n    \"key\": \"" + testKey
                        + "\"\n  },\n  \"inwardIssue\": {\n    \"key\": \"" + issueKey
                        + "\"\n  },\n  \"type\": {\n    \"name\": \"Blocks\"\n  }\n}")
                .asString();
        if (response.getStatus() == 201) {
            System.out.println("Issue linked successfully");
        }
    }

}