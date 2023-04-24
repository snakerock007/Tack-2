package jm.task.core.jdbc.util;


import jm.task.core.jdbc.model.User;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static final String userName = "postgres";
    private static final String password = "admin";
    private static final String connectionUrl = "jdbc:postgresql://localhost:5432/postgres";
//    private static final String driver = "org.postgresql.Driver";


    public static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(connectionUrl, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactoryHibernate (){
        if (sessionFactory == null){
            try {
                Properties properties = new Properties();
//                properties.setProperty("hibernate.connection.driver_class","org.postgresql.Driver");
                properties.setProperty("hibernate.connection.username","postgres");
                properties.setProperty("hibernate.connection.password", "admin");
                properties.setProperty("hibernate.connection.url","jdbc:postgresql://localhost:5432/postgres");
                Configuration configuration = new Configuration();
                configuration.setProperties(properties);
                configuration.addAnnotatedClass(User.class);

                StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(ssrb.build());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}

