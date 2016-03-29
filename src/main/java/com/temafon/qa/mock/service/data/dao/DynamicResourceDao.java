package com.temafon.qa.mock.service.data.dao;

import com.temafon.qa.mock.service.data.dataSet.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class DynamicResourceDao {
    private Session session;

    public DynamicResourceDao(Session session){this.session = session;}

    public long insert(String name, long methodId, long strategyId, long defaultDynamicResponseId)
            throws HibernateException {
        Method method = (Method) session.load(Method.class, methodId);
        DispatchStrategy strategy = (DispatchStrategy) session.load(DispatchStrategy.class, strategyId);

        DynamicResponse dynamicResponse = null;
        if(defaultDynamicResponseId > 0){
            dynamicResponse =
                    (DynamicResponse) session.load(DynamicResponse.class, defaultDynamicResponseId);
        }

        DynamicResource dynamicResource = new DynamicResource(name, method, strategy, dynamicResponse);
        return (Long) session.save(dynamicResource);
    }

    public DynamicResource getByPath(String path) throws HibernateException{
        Criteria criteria = session.createCriteria(DynamicResource.class);
        return ((DynamicResource) criteria.add(Restrictions.eq("path", path)).uniqueResult());
    }

    public DynamicResource get(long id) throws HibernateException{
        return (DynamicResource) session.get(DynamicResource.class, id);
    }

    public List<DynamicResource> selectAll() throws HibernateException{
        Query query = session.createQuery("from DynamicResource");
        return query.list();
    }

    public void update(long id, String name, long methodId, long strategyId, long defaultDynamicResponseId)
            throws HibernateException{
        Method method = (Method) session.load(Method.class, methodId);
        DispatchStrategy strategy = (DispatchStrategy) session.load(DispatchStrategy.class, strategyId);

        DynamicResource dynamicResource = (DynamicResource) session.load(DynamicResource.class, id);

        dynamicResource.setPath(name);
        dynamicResource.setMethod(method);
        dynamicResource.setDispatch_strategy(strategy);

        if(defaultDynamicResponseId > 0){
            DynamicResponse dynamicResponse = (DynamicResponse) session.load(DynamicResponse.class, defaultDynamicResponseId);
            dynamicResource.setDefaultDynamicResponse(dynamicResponse);
        }

        session.merge(dynamicResource);
    }
}
