package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory factory = Util.getSessionFactoryHibernate();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String sql = ("CREATE TABLE IF NOT EXISTS users" +
                "(id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name text, lastName text, age int)");
        executeQuery(sql);

    }

    @Override
    public void dropUsersTable() {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            Query query = session.createSQLQuery("DROP TABLE IF EXISTS users");
            query.executeUpdate();
            session.getTransaction().commit();
            session.close();

        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.persist(user);
            transaction.commit();

        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            User entity = session.get(User.class, id);
            if (entity != null) {
                session.delete(entity);
            }
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        try (Session session = factory.openSession()) {
            users = loadAllData(session);
            return users;

        }
    }

    private List<User> loadAllData(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        criteriaQuery.from(User.class);
        List<User> data = session.createQuery(criteriaQuery).getResultList();
        return data;
    }

    @Override
    public void cleanUsersTable() {
        executeQuery("TRUNCATE users");

    }

    private void executeQuery(String sqlQuery) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createSQLQuery(sqlQuery);
            query.executeUpdate();
            transaction.commit();
        }
    }
}