package com.temafon.qa.mock.service.data.dao;

import com.temafon.qa.mock.service.data.dataSet.DynamicResource;
import com.temafon.qa.mock.service.data.dataSet.DynamicResponse;
import com.temafon.qa.mock.service.data.dataSet.Header;
import com.temafon.qa.mock.service.data.dataSet.Script;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class HeaderDao {
    private Session session;

    public HeaderDao(Session session){this.session = session;}

    public long insert(String name, String value, long responseId){
        DynamicResponse dynamicResponse = (DynamicResponse) session.load(DynamicResponse.class, responseId);

        Header header = new Header(name, value, dynamicResponse);
        return (Long) session.save(header);
    }

    public List<Header> getAllByResponse(long responseId) throws HibernateException {
        DynamicResponse dynamicResponse = (DynamicResponse) session.load(DynamicResponse.class, responseId);

        Criteria criteria = session.createCriteria(Header.class);
        return ((List<Header>) criteria.add(Restrictions.eq("dynamicResponse", dynamicResponse)).list());
    }

    public Header get(long id){
        return (Header) session.get(Header.class, id);
    }
}
