package com.temafon.qa.mock.service.data.dao;

import com.temafon.qa.mock.service.data.dataSet.User;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UsersDao {
    private Session session;

    public UsersDao(Session session) {
        this.session = session;
    }

    public User get(long id) throws HibernateException {
        return (User) session.get(User.class, id);
    }

    public User getUserByLogin(String login) throws HibernateException {
        Criteria criteria = session.createCriteria(User.class);
        return ((User) criteria.add(Restrictions.eq("login", login)).uniqueResult());
    }

    public long insert(String login, String password, String email) throws HibernateException {
        return (Long) session.save(new User(login, password, email));
    }
}
