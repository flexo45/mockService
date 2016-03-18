package com.temafon.qa.mock.service.dynamicresources;

import com.temafon.qa.mock.service.data.DBException;
import com.temafon.qa.mock.service.data.DBManager;
import com.temafon.qa.mock.service.data.dataSet.DispatchStrategy;
import com.temafon.qa.mock.service.data.dataSet.Method;
import com.temafon.qa.mock.service.dynamicresources.cach.DynamicResourcesCache;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;

import java.util.List;

public class DynamicResourcesService {

    private static DynamicResourcesService instance;

    public static DynamicResourcesService getInstance(){
        if(instance == null){
            instance = new DynamicResourcesService();
        }
        return instance;
    }

    private DynamicResourcesCache dynamicResourcesCache;

    private DynamicResourcesService(){
        dynamicResourcesCache = new DynamicResourcesCache();
    }

    public List<DynamicResourceItem> getDynamicResourceItemList(){
        return dynamicResourcesCache.getDynamicResourcesItemList();
    }

    public void addDynamicResource(DynamicResourceItem item){
        try {
            Method method = DBManager.getInstance().getDynamicResourceDataManager().getMethod(item.getMethod().toString());
            DispatchStrategy strategy = DBManager.getInstance().getDynamicResourceDataManager().getStrategy(item.getStrategy().toString());

            long resourceId = DBManager.getInstance().getDynamicResourceDataManager()
                    .addDynamicResource(item.getPath(), method.getId(), strategy.getId());

            if(item.getScript() != null){
                DBManager.getInstance().getDynamicResourceDataManager().addScript(item.getScript(), resourceId);
            }

            dynamicResourcesCache.cacheNotActual();
        }
        catch (DBException ex){
            ex.printStackTrace();
        }
    }

    public DynamicResourceItem getDynamicResource(String path){
        for(DynamicResourceItem it : dynamicResourcesCache.getDynamicResourcesItemList()){
            if(it.getPath().equals(path)){
                return it;
            }
        }
        return null;
    }
}
