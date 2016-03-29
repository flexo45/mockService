package com.temafon.qa.mock.service.dynamicresources;

import com.temafon.qa.mock.service.data.DBException;
import com.temafon.qa.mock.service.data.DBManager;
import com.temafon.qa.mock.service.data.dataSet.*;
import com.temafon.qa.mock.service.dynamicresources.cach.DynamicResourcesCache;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResponseItem;

import java.util.List;
import java.util.Map;

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

    public void addResponseToResource(long resourceId, DynamicResponseItem response){
        try {
            DynamicResource dynamicResource =
                    DBManager.getInstance().getDynamicResourceDataManager().getDynamicResource(resourceId);

            long id = DBManager.getInstance().getDynamicResourceDataManager()
                    .addDynamicResponse(response.getName(), response.getResponseCode(), response.getContent()
                            , response.getScript(), dynamicResource.getId());

            for(Map.Entry<String, String> it : response.getHeaders().entrySet()){
                DBManager.getInstance().getDynamicResourceDataManager()
                        .addHeader(it.getKey(), it.getValue(), id);
            }

            dynamicResourcesCache.cacheNotActual();
        }
        catch (DBException e){
            e.printStackTrace();
        }
    }

    public void addDynamicResource(DynamicResourceItem item){
        try {
            Method method = DBManager.getInstance()
                    .getDynamicResourceDataManager().getMethod(item.getMethod().toString());

            DispatchStrategy strategy = DBManager.getInstance()
                    .getDynamicResourceDataManager().getStrategy(item.getStrategy().toString());

            DynamicResponse dynamicResponse = null;
            if(item.getDefaultResponse() != null){
                dynamicResponse = DBManager.getInstance()
                        .getDynamicResourceDataManager().getDynamicResponse(item.getDefaultResponse().getId());
            }

            long resourceId = DBManager.getInstance().getDynamicResourceDataManager()
                    .addDynamicResource(item.getPath()
                            , method.getId()
                            , strategy.getId()
                            , dynamicResponse == null ? 0 : dynamicResponse.getId());

            if(item.getScript() != null){
                DBManager.getInstance().getDynamicResourceDataManager().addScript(item.getScript(), resourceId);
            }

            dynamicResourcesCache.cacheNotActual();
        }
        catch (DBException ex){
            ex.printStackTrace();
        }
    }

    public void updateDynamicResource(DynamicResourceItem item){
        try {
            Method method = DBManager.getInstance()
                    .getDynamicResourceDataManager().getMethod(item.getMethod().toString());

            DispatchStrategy strategy = DBManager.getInstance()
                    .getDynamicResourceDataManager().getStrategy(item.getStrategy().toString());

            DynamicResponse defaultDynamicResponse = null;
            if(item.getDefaultResponse() != null){
                defaultDynamicResponse = DBManager.getInstance()
                        .getDynamicResourceDataManager().getDynamicResponse(item.getDefaultResponse().getId());
            }

            DBManager.getInstance().getDynamicResourceDataManager()
                    .updateDynamicResource(item.getId(), item.getPath(), method.getId(), strategy.getId()
                            , defaultDynamicResponse == null ? 0 : defaultDynamicResponse.getId());

            if(item.getScript() != null){
                Script script =
                        DBManager.getInstance().getDynamicResourceDataManager().getScriptByResourceId(item.getId());
                if(script == null && !item.getScript().equals("")){
                    DBManager.getInstance().getDynamicResourceDataManager().addScript(item.getScript(), item.getId());
                }
                else if(script != null){
                    DBManager.getInstance().getDynamicResourceDataManager().updateScript(script.getId(), item.getScript());
                }
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

    public DynamicResourceItem getDynamicResource(long id){
        for(DynamicResourceItem it : dynamicResourcesCache.getDynamicResourcesItemList()){
            if(it.getId() == id){
                return it;
            }
        }
        return null;
    }
}
