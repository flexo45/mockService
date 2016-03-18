package com.temafon.qa.mock.service.data.dao;

import com.temafon.qa.mock.service.data.dataSet.DispatchStrategy;
import com.temafon.qa.mock.service.data.dataSet.Method;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class DispatchStrategyDao {
    private Session session;

    public DispatchStrategyDao(Session session){this.session = session;}

    public long insert(String name){
        DispatchStrategy strategy = new DispatchStrategy(name);
        return (Long) session.save(strategy);
    }

    public DispatchStrategy getStrategyByName(String name){
        Criteria criteria = session.createCriteria(DispatchStrategy.class);
        return ((DispatchStrategy) criteria.add(Restrictions.eq("name", name)).uniqueResult());
    }
}
