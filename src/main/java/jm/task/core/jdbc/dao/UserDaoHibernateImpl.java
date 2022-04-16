package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS `pp_data_base`.`Users` ( " +
            "`id` BIGINT(255) NOT NULL AUTO_INCREMENT UNIQUE, " +
            "`name` VARCHAR(128) NULL, " +
            "`lastName` VARCHAR(128) NULL, " +
            "`age` TINYINT(255) NULL, " +
            "PRIMARY KEY (`id`)); " ;

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(CREATE_USERS_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Database creating error");
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS Users").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Database creating error");
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(session.get(User.class, id));
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("FROM User").getResultList();
        }


    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
