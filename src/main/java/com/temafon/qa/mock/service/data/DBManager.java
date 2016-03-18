package com.temafon.qa.mock.service.data;

import com.temafon.qa.mock.service.data.dataSet.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DBManager {

    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "create"; //update

    private static DBManager instance;

    private UserDataManager userDataManager;
    private DynamicResourceDataManager dynamicResourceDataManager;

    public static DBManager getInstance(){
        if(instance == null){
            instance = new DBManager();
        }
        return instance;
    }

    private final SessionFactory sessionFactory;

    private DBManager(){
        Configuration configuration = getH2Configuration();
        sessionFactory = createSessionFactory(configuration);

        userDataManager = new UserDataManager(sessionFactory);
        dynamicResourceDataManager = new DynamicResourceDataManager(sessionFactory);
    }

    public UserDataManager getUserDataManager(){return userDataManager;}

    public DynamicResourceDataManager getDynamicResourceDataManager() {return dynamicResourceDataManager;}

    private Configuration getH2Configuration() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Method.class);
        configuration.addAnnotatedClass(Script.class);
        configuration.addAnnotatedClass(DispatchStrategy.class);
        configuration.addAnnotatedClass(DynamicResource.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./mockservice/h2db/mock");
        configuration.setProperty("hibernate.connection.username", "mock");
        configuration.setProperty("hibernate.connection.password", "vm6&*iwamL");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
