package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "mysql";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/pp_data_base";

    // реализуйте настройку соеденения с БД

    public static void registerDriver() {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            System.err.println("Unable to load driver class");
            e.printStackTrace();
        }
    }
    public static Connection createConnectionDB()  {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            con.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Connection creation error");
            e.printStackTrace();
        }
        return con;
    }
}