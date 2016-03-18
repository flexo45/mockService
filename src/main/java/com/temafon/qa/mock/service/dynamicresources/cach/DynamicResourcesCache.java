package com.temafon.qa.mock.service.dynamicresources.cach;

import com.temafon.qa.mock.service.data.DBException;
import com.temafon.qa.mock.service.data.DBManager;
import com.temafon.qa.mock.service.data.dataSet.DynamicResource;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;
import com.temafon.qa.mock.service.dynamicresources.constants.RestMethods;
import com.temafon.qa.mock.service.dynamicresources.constants.Strategy;

import java.util.ArrayList;
import java.util.List;

public class DynamicResourcesCache {

    private List<DynamicResourceItem> cacheList;

    private boolean isActual;

    public DynamicResourcesCache(){
        cacheList = new ArrayList<>();
        loadFromDb();
        isActual = true;
    }

    private void loadFromDb(){
        try {
            List<DynamicResource> list = DBManager.getInstance().getDynamicResourceDataManager()
                    .selectAllDynamicResources();

            for(DynamicResource it : list){
                cacheList.add(new DynamicResourceItem(it.getPath()
                        , RestMethods.valueOf(it.getMethod().getName())
                        , Strategy.valueOf(it.getDispatch_strategy().getName())));
            }
        }
        catch (DBException e){
            e.printStackTrace();
        }
    }

    public void cacheNotActual(){
        isActual = false;
    }

    public List<DynamicResourceItem> getDynamicResourcesItemList(){
        if(!isActual){
            loadFromDb();
            isActual = true;
        }
        return cacheList;
    }
}
