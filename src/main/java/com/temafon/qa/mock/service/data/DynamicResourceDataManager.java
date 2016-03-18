package com.temafon.qa.mock.service.data;

import com.temafon.qa.mock.service.data.dao.DispatchStrategyDao;
import com.temafon.qa.mock.service.data.dao.DynamicResourceDao;
import com.temafon.qa.mock.service.data.dao.MethodDao;
import com.temafon.qa.mock.service.data.dao.ScriptDao;
import com.temafon.qa.mock.service.data.dataSet.DispatchStrategy;
import com.temafon.qa.mock.service.data.dataSet.DynamicResource;
import com.temafon.qa.mock.service.data.dataSet.Method;
import com.temafon.qa.mock.service.data.dataSet.Script;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class DynamicResourceDataManager {

    SessionFactory sessionFactory;

    public DynamicResourceDataManager(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public List<DynamicResource> selectAllDynamicResources() throws DBException{
        try {
            Session session = sessionFactory.openSession();
            DynamicResourceDao dao = new DynamicResourceDao(session);
            List<DynamicResource> list = dao.selectAll();
            session.close();
            return list;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addMethod(String name) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            MethodDao dao = new MethodDao(session);
            long id = dao.insert(name);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addStrategy(String name) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            DispatchStrategyDao dao = new DispatchStrategyDao(session);
            long id = dao.insert(name);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addScript(String text, long resourceId) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ScriptDao dao = new ScriptDao(session);
            long id = dao.insert(text, resourceId);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public Method getMethod(String name) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            MethodDao dao = new MethodDao(session);
            Method dataSet = dao.getMethodByName(name);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public DispatchStrategy getStrategy(String name) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            DispatchStrategyDao dao = new DispatchStrategyDao(session);
            DispatchStrategy dataSet = dao.getStrategyByName(name);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public Script getScript(long id) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            ScriptDao dao = new ScriptDao(session);
            Script dataSet = dao.get(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addDynamicResource(String path, long methodId, long strategyId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            DynamicResourceDao dao = new DynamicResourceDao(session);
            long id = dao.insert(path, methodId, strategyId);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public DynamicResource getDynamicResource(String path) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            DynamicResourceDao dao = new DynamicResourceDao(session);
            DynamicResource dataSet = dao.getByPath(path);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }
}
