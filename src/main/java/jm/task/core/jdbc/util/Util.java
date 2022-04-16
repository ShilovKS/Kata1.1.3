package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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


    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://127.0.0.1:3306/pp_data_base?seLegacyDatetimeCode=false&serverTimezone=Europe/Moscow"); //useSSL=false&amp;&amp;serverTimezone=UTC ?????? useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "mysql");

                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "none"); //none create-drop

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

}