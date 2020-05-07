package com.claudiordev.data;

import com.claudiordev.main.Main;

import java.io.File;
import java.sql.*;

public class SQLLiteData {

    static String url;
    static String tablename;


    public SQLLiteData() {

    }

    public SQLLiteData(String url) {
        this.url = url;
        createdir();
    }

    /**
     * Create Directory for the SQLLite Database;
     */
    public void createdir() {
        File file = new File(Main.getPlugin().getDataFolder().toString() + "\\db");

        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * First Connection;
     * Establishes Connection to the SQLLite Database and closes it;
     */
    public void connect() {
        Connection conn = null;
        try {
            //Set the Driver class for JDBC SQlLite
            Class.forName("org.sqlite.JDBC");


            // create a connection to the database
            conn = DriverManager.getConnection(url);

            Main.getPlugin().getLogger().info("Connection to SQLite database has been established;");

        } catch (SQLException | ClassNotFoundException e) {
            Main.getPlugin().getLogger().info(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Main.getPlugin().getLogger().info(ex.getMessage());
            }
        }
    }

    /**
     * Create a table within the Database;
     */
    public void createTable(String tableName) {

        this.tablename = tableName;

        //SQL Statement "\n" for New line
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	uniqueid TEXT,\n"
                + "	state INTEGER DEFAULT 1\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            //Execute statement
            stmt.execute(sql);
        } catch (SQLException e) {
            Main.getPlugin().getLogger().info(e.getMessage());
        }
    }

    /**
     * Used to Do Updates or Inserts on the SQLLite database;
     * @param query
     */
    public void manageData(String query) throws SQLException{
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        stmt.execute(query);
        conn.close();
    }

    public void insertData(String uniqueid, Integer state) {
        String query = "INSERT INTO visibility (uniqueid,state) \n"
                + "VALUES('" +uniqueid + "', "+state+");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
             stmt.execute(query);

             Main.getPlugin().getLogger().info("Data inserted with success;");

            //Closes conn automatically after Try/Catch;
        } catch (SQLException e) {
            Main.getPlugin().getLogger().info(e.getMessage());
        }
    }


    public Object retrieveData(String query, String type) throws Exception{
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        String value;

        try {

            //Error check;
            if (!conn.isClosed()) {
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    value = rs.getString(type);
                    return value;
                }

            } else {
                throw new Exception("Error on Data Writting, please contact the creator of the Plugin!");
            }


        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Main.getPlugin().getLogger().info(ex.getMessage());
            }
        }

        //Return Null if no value is taken on the while loop
        return null;
    }
}
