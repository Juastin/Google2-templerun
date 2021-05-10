package com.badlogic.drop;

import java.sql.*;
import java.util.ArrayList;

/* Based on: https://www.youtube.com/watch?v=2i4t-SL1VsU
EXAMPLE:
ArrayList<ArrayList<String>> result = Database.query("select * from Persoon");
System.out.println(result.toString());
Returns data in the following format:
[
  [value, value, value],   //row1
  [value, value, value],   //row2
  [value, value, value]    //row3
]
*/

public class Database {
    //Local database test
//    static String host = "jdbc:mysql://localhost:3306/templerun";
//    static String username = "root";
//    static String password = "";

    static String host = "jdbc:mysql://40.113.153.48/templerun";
    static String username = "newRemote";
    static String password = "P@ssw0rd";

    public static Connection maakVerbinding() {
        try {
            Connection myConn = DriverManager.getConnection(Database.host, Database.username, Database.password);
            return myConn;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
            return null;
        }
    }

    public static ArrayList<ArrayList<String>> query(PreparedStatement myStmt) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        Connection myConn = null;
        ResultSet myRs = null;

        try {
            myRs = myStmt.executeQuery();
            ResultSetMetaData rsmd = myRs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (myRs.next()) {
                ArrayList<String> result_part = new ArrayList<String>();
                for (int i = 1; i <= columnsNumber; i++) {
                    result_part.add(myRs.getString(i));
                }
                result.add(result_part);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }

        try {
            if (myRs != null) {myRs.close();}
            if (myStmt != null) {myStmt.close();}
            if (myConn != null) {myConn.close();}
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return result;
    }
}
