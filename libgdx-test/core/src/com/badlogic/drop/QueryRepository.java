package com.badlogic.drop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QueryRepository {
    public static void insertName(String username) {
        Database.query(String.format("insert into namen (gebruikersnaam) Select '%s' Where not exists(select * from namen where gebruikersnaam='%s')", username, username));
//        Connection myConn = DriverManager.getConnection(Database.host, Database.username, Database.password);
//        PreparedStatement myStmt = myConn.prepareStatement("insert into namen (gebruikersnaam) Select ? Where not exists(select * from namen where gebruikersnaam=?)");
//        myStmt.setString(1, username);
//        myStmt.setString(2, username);
//        Database.query(myStmt);
    }
}
