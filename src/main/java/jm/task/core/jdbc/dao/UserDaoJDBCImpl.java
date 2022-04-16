package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.Main;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS `pp_data_base`.`Users` ( " +
            "`id` BIGINT(255) NOT NULL AUTO_INCREMENT UNIQUE, " +
            "`name` VARCHAR(128) NULL, " +
            "`lastName` VARCHAR(128) NULL, " +
            "`age` TINYINT(255) NULL, " +
            "PRIMARY KEY (`id`)); " ;

    private static final Connection con = Util.createConnectionDB();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        try (Statement statement = con.createStatement()) {
            statement.execute(CREATE_USERS_TABLE);
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Database creating error");
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = con.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Users");
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Database deleting error");
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement =
                     con.prepareStatement("INSERT  INTO Users (name, lastname, age) values (?, ?, ?);")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Database save user error");
            e.printStackTrace();
        }


    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement =
                     con.prepareStatement("DELETE FROM Users WHERE id = ?;")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database remove user error");
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try (ResultSet resultSet = con.createStatement().executeQuery("SELECT * FROM Users")){
            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));

                userList.add(user);
                con.commit();
            }
        } catch (SQLException e) {
            try {
                con.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Statement statement = con.createStatement()) {
            statement.execute("TRUNCATE TABLE Users");
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Database cleanup error");
            e.printStackTrace();
        }
    }
}
