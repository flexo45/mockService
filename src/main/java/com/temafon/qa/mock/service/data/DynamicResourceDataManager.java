package com.temafon.qa.mock.service.data;

import com.temafon.qa.mock.service.data.dao.*;
import com.temafon.qa.mock.service.data.dataSet.*;
import com.temafon.qa.mock.service.dynamicresources.constants.Strategy;
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

    public Script getScriptByResourceId(long resourceId) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            ScriptDao dao = new ScriptDao(session);
            Script dataSet = dao.getByResourceId(resourceId);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addDynamicResource(String path, long methodId, long strategyId, long defaultDynamicResponseId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            DynamicResourceDao dao = new DynamicResourceDao(session);
            long id = dao.insert(path, methodId, strategyId, defaultDynamicResponseId);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public DynamicResource getDynamicResource(Long id) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            DynamicResourceDao dao = new DynamicResourceDao(session);
            DynamicResource dataSet = dao.get(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public long addDynamicResponse(String name, int code, byte[] content, String script, long resourceId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            DynamicResponseDao dao = new DynamicResponseDao(session);
            long id = dao.insert(name, code, content, script, resourceId);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public DynamicResponse getDynamicResponse(long id) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            DynamicResponseDao dao = new DynamicResponseDao(session);
            DynamicResponse dataSet = dao.get(id);
            session.close();
            return dataSet;
        }
        catch (HibernateException e){
            throw new DBException(e);
        }
    }

    public long addHeader(String name, String value, long responseId) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            HeaderDao dao = new HeaderDao(session);
            long id = dao.insert(name, value, responseId);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public Header getHeader(long id) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            HeaderDao dao = new HeaderDao(session);
            Header dataSet = dao.get(id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public List<Header> getHeadersOfResponse(long responseId) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            HeaderDao dao = new HeaderDao(session);
            List<Header> dataSetList = dao.getAllByResponse(responseId);
            session.close();
            return dataSetList;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public List<DynamicResponse> getDynamicResponsesOfResource(long resourceId) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            DynamicResponseDao dao = new DynamicResponseDao(session);
            List<DynamicResponse> dataSetList = dao.getAllByResource(resourceId);
            session.close();
            return dataSetList;
        } catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void updateDynamicResource(long id, String path, long methodId, long strategyId, long defaultDynamicResponseId)
            throws DBException{
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            DynamicResourceDao dao = new DynamicResourceDao(session);
            dao.update(id, path, methodId, strategyId, defaultDynamicResponseId);
            transaction.commit();
            session.close();
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }

    public void updateScript(long id, String text) throws DBException{
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            ScriptDao dao = new ScriptDao(session);
            dao.update(id, text);
            transaction.commit();
            session.close();
        }
        catch (HibernateException e) {
            throw new DBException(e);
        }
    }
}
