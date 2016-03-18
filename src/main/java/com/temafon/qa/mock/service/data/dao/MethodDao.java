package com.temafon.qa.mock.service.data.dao;

import com.temafon.qa.mock.service.data.dataSet.Method;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class MethodDao {
    private Session session;

    public MethodDao(Session session){this.session = session;}

    public long insert(String name){
        Method method = new Method(name);
        return (Long) session.save(method);
    }

    public Method getMethodByName(String name){
        Criteria criteria = session.createCriteria(Method.class);
        return ((Method) criteria.add(Restrictions.eq("name", name)).uniqueResult());
    }
}
