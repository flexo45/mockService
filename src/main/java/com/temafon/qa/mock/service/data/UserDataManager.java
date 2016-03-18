package com.temafon.qa.mock.service.data;

import com.temafon.qa.mock.service.data.dao.UsersDao;
import com.temafon.qa.mock.service.data.dataSet.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UserDataManager {

    SessionFactory sessionFactory;

    public UserDataManager(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public long addUser(String name, String password, String email) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UsersDao dao = new UsersDao(session);
            long id = dao.insert(name, password, email);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public User getUser(String login) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            UsersDao dao = new UsersDao(session);
            User dataSet = dao.getUserByLogin(login);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

}
