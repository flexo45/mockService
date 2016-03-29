package com.temafon.qa.mock.service.data.dao;

import com.temafon.qa.mock.service.data.dataSet.DynamicResource;
import com.temafon.qa.mock.service.data.dataSet.DynamicResponse;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class DynamicResponseDao {
    private Session session;

    public DynamicResponseDao(Session session){this.session = session;}

    public long insert(String name, int code, byte[] content, String script, long resourceId){
        DynamicResource dynamicResource = (DynamicResource) session.load(DynamicResource.class, resourceId);

        DynamicResponse dynamicResponse = new DynamicResponse(name, code, content, script, dynamicResource);
        return (Long) session.save(dynamicResponse);
    }

    public DynamicResponse getByName(String name) throws HibernateException {
        Criteria criteria = session.createCriteria(DynamicResponse.class);
        return ((DynamicResponse) criteria.add(Restrictions.eq("name", name)).uniqueResult());
    }

    public DynamicResponse get(long id){
        return (DynamicResponse) session.get(DynamicResponse.class, id);
    }

    public List<DynamicResponse> getAllByResource(long resourceId) {
        DynamicResource dynamicResource = (DynamicResource) session.load(DynamicResource.class, resourceId);

        Criteria criteria = session.createCriteria(DynamicResponse.class);
        return criteria.add(Restrictions.eq("dynamicResource", dynamicResource)).list();
    }
}
