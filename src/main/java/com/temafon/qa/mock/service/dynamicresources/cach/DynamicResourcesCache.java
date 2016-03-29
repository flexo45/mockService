package com.temafon.qa.mock.service.dynamicresources.cach;

import com.temafon.qa.mock.service.data.DBException;
import com.temafon.qa.mock.service.data.DBManager;
import com.temafon.qa.mock.service.data.dataSet.DynamicResource;
import com.temafon.qa.mock.service.data.dataSet.DynamicResponse;
import com.temafon.qa.mock.service.data.dataSet.Header;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResourceItem;
import com.temafon.qa.mock.service.dynamicresources.constants.RestMethods;
import com.temafon.qa.mock.service.dynamicresources.constants.Strategy;
import com.temafon.qa.mock.service.dynamicresources.resource.DynamicResponseItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            List<DynamicResource> list = DBManager.getInstance()
                    .getDynamicResourceDataManager().selectAllDynamicResources();

            cacheList.clear();

            for(DynamicResource it : list){
                cacheList.add(createDynamicResourceItem(it));
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

    private List<DynamicResponseItem> getResponses(long resourceId) throws DBException{
        List<DynamicResponseItem> list = new ArrayList<>();

        List<DynamicResponse> dynamicResponseList = DBManager.getInstance()
                .getDynamicResourceDataManager().getDynamicResponsesOfResource(resourceId);

        for(DynamicResponse dynamicResponse : dynamicResponseList){

            DynamicResponseItem response =
                    new DynamicResponseItem(dynamicResponse.getId(), dynamicResponse.getName());

            response.setResponseCode(dynamicResponse.getCode());
            response.setContent(dynamicResponse.getContent());
            response.setScript(dynamicResponse.getScript());

            for(Map.Entry<String, String> header : getHeaders(dynamicResponse.getId()).entrySet()){
                response.addHeader(header.getKey(), header.getValue());
            }

            list.add(response);
        }

        return list;
    }

    private Map<String, String> getHeaders(long responseId) throws DBException{

        List<Header> headerList = DBManager.getInstance()
                .getDynamicResourceDataManager().getHeadersOfResponse(responseId);

        Map<String, String> list = new HashMap<>();

        for(Header header : headerList){
            list.put(header.getName(), header.getValue());
        }

        return list;
    }

    private DynamicResourceItem createDynamicResourceItem(DynamicResource dynamicResource) throws DBException{

        DynamicResourceItem dynamicResourceItem = new DynamicResourceItem(dynamicResource.getId()
                ,dynamicResource.getPath()
                , RestMethods.valueOf(dynamicResource.getMethod().getName())
                , Strategy.valueOf(dynamicResource.getDispatch_strategy().getName()));

        for(DynamicResponseItem response : getResponses(dynamicResource.getId())){

            if(dynamicResource.getDefaultDynamicResponse() != null){
                if(dynamicResource.getDefaultDynamicResponse().getId() == response.getId()){
                    dynamicResourceItem.setDefaultResponse(response);
                }
            }
            dynamicResourceItem.addResponse(response);
        }



        return dynamicResourceItem;
    }
}
