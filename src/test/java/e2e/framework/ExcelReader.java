package e2e.framework;

import com.aventstack.extentreports.Status;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

public class ExcelReader {


    private String filename;
    private ExcelScanner Xlscanner;
    private static Map<String, Map<String, Map<String, ArrayList<Map<String, String>>>>> testsComb = new HashMap<String, Map<String, Map<String, ArrayList<Map<String, String>>>>>();
//	private static JSONObject testsCombObj = new JSONObject();

    public ExcelReader(ExcelScanner Xlscanner) {
        this.Xlscanner = Xlscanner;
    }

    public ExcelReader(String filename) {
        this.filename = filename;
    }

    public void scanExcel() {
        String folderName = System.getProperty("ResFolder");
        String filename = folderName + File.separator + "TestPack_Selector.xlsx";
        System.out.println(filename);
        Workbook wb;
        String cellText;
        boolean allClasses = false;
        List<String> colNames = new ArrayList<String>();
        Map<Integer, String> platformIndex = new HashMap<Integer, String>();
        Map<Integer, String> osIndex = new HashMap<Integer, String>();
        Map<Integer, String> browserIndex = new HashMap<Integer, String>();
        Map<Integer, String> versionIndex = new HashMap<Integer, String>();
        try {
            wb = WorkbookFactory.create(new FileInputStream(filename));
            Sheet sheet = wb.getSheetAt(0);
            rowloop:
            for (Row row : sheet) {
                Cell cell0 = row.getCell(0);
                if (cell0 == null) {
                    continue;
                }
                cellText = cell0.getStringCellValue();
                //System.out.println(cellText);
                switch (cellText.toLowerCase()) {
                    case "test_name":
                        Xlscanner.assignSuiteName("Working");
                        break;
                    case "groups":
                        if (row.getCell(1).getStringCellValue().equalsIgnoreCase("YES")) {
                            if (row.getCell(2).getStringCellValue().equalsIgnoreCase("INCLUDE")) {
                                if (row.getCell(3).getStringCellValue().equalsIgnoreCase("YES")) {
                                    for (Cell cell : row) {
                                        if (cell.getColumnIndex() > 3) {
                                            Xlscanner.assignGroupIncludes(cell.getStringCellValue());
                                        }
                                    }

                                }
                                row = sheet.getRow(row.getRowNum() + 1);
                                if (row.getCell(2).getStringCellValue().equalsIgnoreCase("EXCLUDE")) {
                                    if (row.getCell(3).getStringCellValue().equalsIgnoreCase("YES")) {
                                        for (Cell cell : row) {
                                            if (cell.getColumnIndex() > 3) {
                                                Xlscanner.assignGroupExcludes(cell.getStringCellValue());
                                            }
                                        }

                                    }
                                }
                            }
                            break rowloop;
                        }
                        break;
                    case "class":

                        if (row.getCell(1).getStringCellValue().equalsIgnoreCase("ALL")
                                && row.getCell(2).getStringCellValue().equalsIgnoreCase("YES")) {
                            allClasses = true;
                        }
                        if (allClasses || row.getCell(2).getStringCellValue().equalsIgnoreCase("YES")) {
                            Xlscanner.addToClasses(row.getCell(1).getStringCellValue());
                        }
                        break;

                    case "tests_def":
                        String[] arrayColNames;
                        int colIndex;
                        String colName = "";
                        List<String> baseNames = Arrays.asList("TESTS_DEF", "CLASSNAME", "TESTNAME", "RUN", "MOB-DRIVER", "Res_Width", "Res_Height");
                        for (Cell cell : row) {
                            colIndex = cell.getColumnIndex();
                            colName = cell.getStringCellValue();
                            colNames.add(colName);
                            if (!baseNames.contains(colName)) {
                                arrayColNames = colName.split("_");
                                platformIndex.put(colIndex, arrayColNames[0]);
                                osIndex.put(colIndex, arrayColNames[1]);
                                browserIndex.put(colIndex, arrayColNames[2]);
                                if (arrayColNames.length > 3) {
                                    versionIndex.put(colIndex, arrayColNames[3]);
                                }
                            }
                        }
                        break;
                    case "tests":
                        String currTest, currDriver, currResHeight, currResWidth;
                        String currClass = row.getCell(1).getStringCellValue();
                        if (Xlscanner.checkClassList(currClass)) {
                            if (row.getCell(3).getStringCellValue().equalsIgnoreCase("YES")) {
                                currTest = row.getCell(2).getStringCellValue();
                                currDriver = row.getCell(4) == null ? "" : row.getCell(4).getStringCellValue();
                                Cell tempCell = row.getCell(5);
                                if (tempCell != null) {
                                    tempCell.setCellType(Cell.CELL_TYPE_STRING);
                                    currResWidth = tempCell.getStringCellValue();
                                } else {
                                    currResWidth = "";
                                }
                                tempCell = row.getCell(6);
                                if (tempCell != null) {
                                    tempCell.setCellType(Cell.CELL_TYPE_STRING);
                                    currResHeight = tempCell.getStringCellValue();
                                } else {
                                    currResHeight = "";
                                }
                                String currCell = "";
                                for (int i = 7; i < row.getLastCellNum(); i++) {
                                    tempCell = row.getCell(i);
                                    if (tempCell == null) {
                                        continue;
                                    }
                                    currCell = tempCell.getStringCellValue();
                                    String[] devices = currCell.split(",");
                                    for (String device : devices)
                                        if (!currCell.equalsIgnoreCase("-") && !currCell.trim().equalsIgnoreCase("")) {
                                            //System.out.println("ADD DATA : " + platformIndex.get(i));
                                            if (platformIndex.get(i).equalsIgnoreCase("Browser")) {
                                                if (currCell.equalsIgnoreCase("YES")) {
                                                    addData(currClass, currTest, "Browser", "", currResHeight, currResWidth, osIndex.get(i), browserIndex.get(i), versionIndex.get(i), "");
//											addData(className, methodName, platform, deviceId, ResHt, ResWtd, os, browser, version, driver);
                                                }
                                            } else {
                                                System.out.println("CURRDRIVER=" + currDriver);
                                                System.out.println(currCell);

                                                device = device.trim();
                                                if (currDriver.equalsIgnoreCase("appium")) {
                                                    addData(currClass, currTest, platformIndex.get(i) + "_" + osIndex.get(i), device, "", "", osIndex.get(i), browserIndex.get(i), "", currDriver);
                                                } else if (!"".equalsIgnoreCase(currDriver.trim()) && !"-".equalsIgnoreCase(currDriver)) {
                                                    addData(currClass, currTest, platformIndex.get(i), device, "", "", osIndex.get(i), browserIndex.get(i), "", currDriver);
                                                }
                                            }
                                        }
                                }
                            }
                        }
                        break;

                    default:
                        break;
                }

            }
//			System.out.println(testsComb);
            Xlscanner.setTestCombo(testsComb);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Reporter.log(Status.ERROR,e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addData(String className, String methodName, String platform, String deviceId, String ResHt, String ResWtd, String os, String browser, String version, String driver) {
        Map<String, Map<String, ArrayList<Map<String, String>>>> testmap = new HashMap<String, Map<String, ArrayList<Map<String, String>>>>();
        Map<String, ArrayList<Map<String, String>>> devicemap = new HashMap<String, ArrayList<Map<String, String>>>();
        ArrayList<Map<String, String>> conf = new ArrayList<Map<String, String>>();
        System.out.println("PLATFORM OUT: " + platform);
        if (platform.equalsIgnoreCase("Browser")) {
            testmap = testsComb.computeIfAbsent(platform, clsNm -> new HashMap<String, Map<String, ArrayList<Map<String, String>>>>());
            devicemap = testmap.computeIfAbsent(className, mtdNm -> new HashMap<String, ArrayList<Map<String, String>>>());
            conf = devicemap.computeIfAbsent(methodName, plt -> new ArrayList<Map<String, String>>());
            Map<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("OS", os);
            tempMap.put("Browser", browser);
            tempMap.put("Version", version);
            tempMap.put("Res_Width", ResWtd);
            tempMap.put("Res_Height", ResHt);
            tempMap.put("Driver", browser);
            tempMap.put("Platform", "Desktop");
            conf.add(tempMap);
        } else {
            System.out.println("PLATFORM: " + platform);
            testmap = testsComb.computeIfAbsent(deviceId + "_" + platform, clsNm -> new HashMap<String, Map<String, ArrayList<Map<String, String>>>>());
            devicemap = testmap.computeIfAbsent(className, mtdNm -> new HashMap<String, ArrayList<Map<String, String>>>());
            conf = devicemap.computeIfAbsent(methodName, plt -> new ArrayList<Map<String, String>>());
            Map<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("OS", os);
            tempMap.put("Platform", platform);
            tempMap.put("Browser", browser);
            tempMap.put("Driver", driver);
            tempMap.put("Device_ID", deviceId);
            conf.add(tempMap);
        }


    }

    public static ArrayList<Map<String, String>> getBrowsersDeviceComb(String testName, String className, String methodName) {
//		if(testsCombObj == null){
//			testsCombObj = loadTestComb();
//		}
//		System.out.println(testsCombObj);
        if (testsComb.isEmpty()) {
            loadTestComb();
        }
        //System.out.println(testsComb);
        //System.out.println(testName + "-" + className + "-" + methodName);
        return testsComb.get(testName).get(className).get(methodName);
    }

    @SuppressWarnings("unchecked")
    private static void loadTestComb() {
//		String contents;
//		try {
//			contents = new Scanner(new File("resources/TestComb.json")).useDelimiter("\\Z").next();
////			testsCombObj = (JSONObject)JSONValue.parseWithException(contents);
//			testsComb = (Map<String, Map<String, Map<String, ArrayList<Map<String, String>>>>>) JSONValue.parseWithException(contents);
//		} catch (FileNotFoundException | ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		return testsCombObj;
//		return testsComb;
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream(System.getProperty("basedir", ".") + File.separator + "TestComb.json");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            testsComb = (Map<String, Map<String, Map<String, ArrayList<Map<String, String>>>>>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Reads the Excel file, and creates a 2-dimensional array with the
     * information within.
     *
     * @return A 2-dimensional array containing the Excel sheet information
     */
    public Object[][] readFile(String TestsheetName) {

        ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();

        try {

            Workbook wb = WorkbookFactory.create(new FileInputStream(filename));
            // Sheet sheet = wb.getSheetAt(0);
            Sheet sheet = wb.getSheet(TestsheetName);
            boolean firstrow = true;
            int index = 0;

            for (Row row : sheet) {

                boolean allAreNull = true;
                ArrayList<Object> items = new ArrayList<Object>();

                if (firstrow) {
                    for (Cell cell : row) {
                        //System.out.println("CELL: " + cell);
                        switch (cell.getCellType()) {

                            case Cell.CELL_TYPE_NUMERIC:
                                allAreNull = false;
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    items.add(cell.getDateCellValue());
                                } else {
                                    double val = cell.getNumericCellValue();
                                    if (val == (int) val)
                                        items.add((int) val);
                                    else
                                        items.add(val);
                                }
                                index++;
                                break;

                            case Cell.CELL_TYPE_BOOLEAN:
                                allAreNull = false;
                                items.add(cell.getBooleanCellValue());
                                index++;
                                break;

                            case Cell.CELL_TYPE_FORMULA:

                            case Cell.CELL_TYPE_BLANK:
                                allAreNull = false;
                                items.add(cell.getCellFormula());
                                index++;
                                break;

                            case Cell.CELL_TYPE_STRING:

                            default:
                                allAreNull = false;
                                items.add(cell.getStringCellValue());
                                index++;
                                break;
                        }
                    }
                    firstrow = false;
                } else {
                    int total = 0;
                    for (int k = 0; k < index; k++) {
                        Cell cell = row.getCell(k, Row.RETURN_BLANK_AS_NULL);
                        //System.out.println("VALUE: " + k + cell);
                        if (cell == null) {
                            items.add(null);
                        } else {
                            allAreNull = false;
                            items.add(cell.getStringCellValue());
                        }
                    }
                }
                if (allAreNull) {
                    break;
                }

                list.add(items);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Reporter.log(Status.ERROR, e.getLocalizedMessage());
        }

        Object[][] data = new Object[list.size()][list.get(0).size()];
        for (int i = 0; i < list.size(); i++) {
            ArrayList<Object> subList = list.get(i);
            Object[] inner = new Object[subList.size()];
            for (int j = 0; j < subList.size(); j++) {
                inner[j] = subList.get(j);
            }
            data[i] = inner;
        }
        return data;

    }

    public static ArrayList<Map<String, String>> getTestDataIterator(String targetTest, String className) {
        ArrayList<Map<String, String>> dataIterParams = new ArrayList<Map<String, String>>();
        try {
            System.out.println(
                    System.getProperty("basedir", ".") + File.separator +
                            System.getProperty("ResFolder") + File.separator +
                            "TestData_Matrix" + ".xlsx"
            );
            ExcelReader reader = new ExcelReader(
                    System.getProperty("basedir", ".") + File.separator +
                            System.getProperty("ResFolder") + File.separator +
                            "TestData_Matrix" + ".xlsx"
            );
            Object[][] file = reader.readFile(className);
            Map<Integer, String> dataParams = new HashMap<Integer, String>();
            // INDEX
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < file[i].length; j++) {
                    String titlename = (String) file[i][j];
                    dataParams.put(j, titlename);
                }
            }
            System.out.println("Values" + dataParams.values());
            System.out.println("Target Test" + targetTest);

            Map<String, String> Datat = new HashMap<String, String>();
            // VALUE
            for (int i = 1; i < file.length; i++) {
                Datat = new HashMap<String, String>();
                for (int j = 0; j < file[i].length; j++) {
                    //System.out.println("TEST COMB" + dataParams.get(j) + (String) file[i][j]);
                    Datat.put(dataParams.get(j), (String) file[i][j]);
                }
                if (Datat.get("TEST_ID").equals(targetTest))
                    dataIterParams.add(Datat);
            }
        } catch (Exception E) {
            // TODO Auto-generated catch block
            E.printStackTrace();
            Reporter.log(Status.ERROR, E.getLocalizedMessage());
        }

        return dataIterParams;
    }
}
