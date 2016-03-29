package com.temafon.qa.mock.service.data.dao;

import com.temafon.qa.mock.service.data.dataSet.DynamicResource;
import com.temafon.qa.mock.service.data.dataSet.Header;
import com.temafon.qa.mock.service.data.dataSet.Script;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ScriptDao {
    private Session session;

    public ScriptDao(Session session){this.session = session;}

    public long insert(String text, long resourceId){
        DynamicResource dynamicResource = (DynamicResource) session.load(DynamicResource.class, resourceId);

        Script script = new Script(text, dynamicResource);
        return (Long) session.save(script);
    }

    public void update(long id, String text){
        Script script = (Script)session.get(Script.class, id);
        script.setText(text);
        session.merge(text);
    }

    public Script getByResourceId(long resourceId){

        DynamicResource dynamicResource = (DynamicResource) session.load(DynamicResource.class, resourceId);

        Criteria criteria = session.createCriteria(Script.class);
        return ((Script) criteria.add(Restrictions.eq("dynamicResource", dynamicResource)).uniqueResult());
    }

    public Script get(long id){
        return (Script) session.get(Script.class, id);
    }
}
