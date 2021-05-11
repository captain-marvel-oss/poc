package e2e.framework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import com.aventstack.extentreports.Status;

import e2e.framework.support.ConfigProp;
import e2e.libraries.ObjectLookup;

public class DatabaseConnection {
    String dburl;
    String UsernameDB;
    String PasswordDB;
    String targetDB;
    Connection connection;
    Statement statement;

    public DatabaseConnection() {
        this.dburl = ConfigProp.getPropertyValue("dburl").replace("ipaddress", System.getProperty("serverid"))
                + "?serverTimezone=UTC";
        System.out.println("DBURL = " + this.dburl);
        this.UsernameDB = ConfigProp.getPropertyValue("dbuser");
        this.PasswordDB = ConfigProp.getPropertyValue("dbpass");
        this.targetDB = ObjectLookup.currentLocDriver().equals("andapp") ? ConfigProp.getPropertyValue("dbtargetdb_mob")
                : ConfigProp.getPropertyValue("dbtargetdb_web");
    }

    public void getConnected() {
        try {
            // Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.dburl, this.UsernameDB, this.PasswordDB);
            this.statement = this.connection.createStatement();
            System.out.println("Database connection successfully done");
        } catch (Exception e) {
            e.printStackTrace();
            Reporter.log(Status.FAIL, "DB Connection Failed: " + e.getLocalizedMessage());
        }
    }

    public void closeTheDb() {
        try {
            this.connection.close();
        } catch (Exception e) {
            Reporter.log(Status.FAIL, "DB Close Failed: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public int getMsisdnRowCount() {
        // int rowcount =0;
        try {
            ResultSet rs = this.statement.executeQuery("SELECT count(*) as RECORDCOUNT FROM " + this.targetDB + "."
                    + ConfigProp.getPropertyValue("dbmsisdntable"));
            rs.next();
            System.out.println("total row: " + rs.getInt("RECORDCOUNT"));
            return rs.getInt("RECORDCOUNT");
        } catch (Exception e) {
            Reporter.log(Status.FAIL, "DB Query Failed: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public HashMap<String, String> getmsisdn(String m) {
        // int rowcount =0;
        try {
            ResultSet rs = this.statement.executeQuery("SELECT * FROM " + this.targetDB + "."
                    + ConfigProp.getPropertyValue("dbmsisdntable") + " WHERE msisdn = '" + m + "'");
            // rowcount = rs.getRow();
            // System.out.println("Row count" +rowcount);
            if (rs == null) {
                Reporter.log(Status.FAIL, "No results obtained");
            } else {
                while (rs.next()) {

                    HashMap<String, String> msisdnvalues = new HashMap<String, String>();
                    msisdnvalues.put("reservation_id", rs.getString("reservation_id"));
                    msisdnvalues.put("serial_number", rs.getString("serial_number"));
                    msisdnvalues.put("imsi", rs.getString("imsi"));
                    msisdnvalues.put("iccid", rs.getString("iccid"));
                    msisdnvalues.put("msisdn_status", rs.getString("msisdn_status"));
                    msisdnvalues.put("owner", rs.getString("owner"));

                    return msisdnvalues;
                }
            }
        } catch (Exception e) {
            Reporter.log(Status.FAIL, "DB Query Failed: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }
}
