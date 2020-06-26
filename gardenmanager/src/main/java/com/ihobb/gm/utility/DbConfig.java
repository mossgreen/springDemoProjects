package com.ihobb.gm.utility;

import org.springframework.beans.factory.annotation.Value;

import java.sql.*;

public class DbConfig {

    // todo read from properteis
    private final String dbDriverName = "org.postgresql.Driver";
    private final String dbAddress = "jdbc:postgresql://127.0.0.1:5432/";
    private final String userNameAndPasswd = "?user=postgres&password=ihobb";
    private final String userName = "postgres";
    private final String passwd = "ihobb";
    private PreparedStatement statement;
    private ResultSet result;
    private Connection con;

    //todo we should have a parameter passed in, to ask me to create a new db or not

    public void createDb(String dbName) {

        try {
            Class.forName(dbDriverName);
            con = DriverManager.getConnection(dbAddress + dbName, userName, passwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            createDatabase(dbName);
        }
    }

    private void createDatabase(String dbName) {
        try {
            Class.forName(dbDriverName);
            con = DriverManager.getConnection(dbAddress + userNameAndPasswd);
            Statement s = con.createStatement();
            int myResult = s.executeUpdate("CREATE DATABASE " + dbName);
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
