# **Selenium - Appium Framework**: __MyAccount Automation__
## **Framework Structure:**
### 1. **Package Details**

**Hierarchy:**
    
      src/test/java/e2e/
        drivers
        framework
        libraries
        pagemethodflows
        pagemethods
        tests
        teststeps
        
**Static Files:**
       
      src/test/resources
        objects
        properties
        {env}Data

   __Packages that should not be modified__ #unless core framework setup requires change
   
    * drivers   ->  selenium/appium webdriver setup
    * framework ->  core solution libraries
    * libraries ->  Object lookup libraries
        
   __Packages involved in the test design__
   
    * tests             ->  TestNG Test configurations
                        ->  Call a corresponding method in teststeps package 
    * teststeps         ->  High Level Steps involved in an end to end test flow
                        ->  Each step is a method call to pagemethodflows
    * pagemethodflows   ->  Creates an object at page level and calls list of steps 
                        ->  Each step is a method call to pagemethods
    * pagemethods       ->  Each method calls low level Selenium, Appium libraries from libraries package
    
   __Example__
   
    tests:
        @Feature("Login")
        @Description("Login Positive Tests")
        @Test(dataProvider = "GlobalTestData", groups = {"login", "smoke"})
        public void Log_TC001(Map<String, String> driverDetails, Map<String, String> data, ITestContext ctx) {
            LoginTestsSteps steps = new LoginTestsSteps();
            steps.testLoginPositive();
        }
    
    teststeps:
        public void testLoginPositive() {
            BaseFlow.loadBaseUrl();
            LoginFlow.loginPositive();
            MyAccountFlow.waitMyAccLoad();
            LogOutFlow.logout();
            LoginFlow.loginLoad();
        }
        
    pagemethodflows:
        public static void loginPositive() {
            String[] data = getBrowserSpecData("registered").split(",");
            LoginPage page = new LoginPage();
            page.waitForPageLoad();
            page.enterUserName(data[2]);
            page.enterPassword(data[3]);
            page.clickRemindMe();
            page.clickLoginButton();
        }
        
    pagemethods:
        public void enterUserName(String username) {
            Wrapper.verifyElement(loginid);
            waitElemLoad(load);
            Wrapper.enter_Text(loginid, username);
        }
        
    Wrapper Class:
        public static void enter_Text(WebObj obj, String text_to_key) {
            WebDriver driver = DriverFactory.getDriver();
            WebElement elem = driver.findElement(obj.by);
            elem.click();
            elem.sendKeys(text_to_key);
        }
    

**Static File Details**

   a. __objects__
    
    File Format: json files 
    Strategy: individual files per page
    Format:
        "object_name": {
            "description": "Object Description",
            "web": {
              "by": "locator_name",
              "value": "locator_value"
            },
            "mobileweb": {
              "by": "locator_name",
              "value": "locator_value"
            }
        }
    Format Legend:
        object_name -> variable name referred by java classes
        locator_name -> id / xpath / classname / linktext / partiallinktext/ css
        locator_value -> corresponding value to locate the web elements
            
   b. __properties__
    
    File Format: .Properties files 
    Strategy: individual files per page
    What is present here?
        1. Application level static contents
    What should not be here?
        1. Test Data
              
   c. __{env}Data__
    
    File Format: json files 
    Strategy: individual files per page
    What is present here?
        1. One time data setup
        2. Currently 4 sets of data configuration is done
        3. Each configuration is for one Platform x Browser combination 
    What should not be done here?
        1. Change the current file structure without having the understanding of the methods involved 
        in remapping the same    

### 2. **Run Commands:**

Project is based of maven. Below are the commands for execution.

    a. Compile:
        
        compiler:testCompile exec:java -DResFolder={resourceFolder} -Dconfig={PropertiesFile} -Dgroups=no -DrunWithGroups=no  -DbuildNumber={Jenkins_build_number}
    
    b. Run:
    
        test -DResFolder={resourceFolder} -Dconfig={PropertiesFile} -Dgroups=no -DrunWithGroups=no -DbuildNumber={Jenkins_build_number}

### 3. **Run Commands for Testresults update in JIRA:**

Prerequesties : Install newman command line tool using below command:
      
      npm install -g newman

Run command: 
    
      newman run telkomsel.json -e atom_environment.json