package com.temafon.qa.mock.service.data.dao;

import com.temafon.qa.mock.service.data.dataSet.DynamicResource;
import com.temafon.qa.mock.service.data.dataSet.Script;
import org.hibernate.Session;

public class ScriptDao {
    private Session session;

    public ScriptDao(Session session){this.session = session;}

    public long insert(String text, long resourceId){
        DynamicResource dynamicResource = (DynamicResource) session.load(DynamicResource.class, resourceId);

        Script script = new Script(text, dynamicResource);
        return (Long) session.save(script);
    }

    public Script get(long id){
        return (Script) session.get(Script.class, id);
    }
}
