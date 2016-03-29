package com.temafon.qa.mock.service.dynamicresources.resource;

import com.temafon.qa.mock.service.dynamicresources.constants.RestMethods;
import com.temafon.qa.mock.service.dynamicresources.constants.Strategy;

import java.util.ArrayList;
import java.util.List;

public class DynamicResourceItem {

    private long id;
    private String path;
    private RestMethods method;
    private String script;
    private Strategy strategy;
    private List<DynamicResponseItem> responses;
    private DynamicResponseItem defaultResponse;
    private int sequenceCount;

    public DynamicResourceItem(String path, RestMethods method, Strategy strategy){
        this.id = -1;
        this.sequenceCount = 0;
        this.responses = new ArrayList<>();
        setPath(path);
        setMethod(method);
        setStrategy(strategy);
    }

    public DynamicResourceItem(long id, String path, RestMethods method, Strategy strategy){
        this.id = id;
        this.sequenceCount = 0;
        this.responses = new ArrayList<>();
        setPath(path);
        setMethod(method);
        setStrategy(strategy);
    }

    public DynamicResponseItem getNextResponse(){
        DynamicResponseItem response = responses.get(sequenceCount);
        sequenceCount = sequenceCount + 1 < responses.size() ? sequenceCount + 1 : 0;
        return response;
    }

    public long getId() {return id;}
    public DynamicResponseItem getDefaultResponse() { return defaultResponse; }
    public String getPath(){return path;}
    public RestMethods getMethod() {return method;}
    public Strategy getStrategy(){ return strategy;}
    public String getScript(){return script;}
    public List<DynamicResponseItem> getResponses() {return responses;}

    public void setDefaultResponse(DynamicResponseItem response) {this.defaultResponse = response;}
    public void setDefaultResponse(long responseId) {
        for(DynamicResponseItem it : responses){
            if(it.getId() == responseId){
                defaultResponse = it;
            }
        }
    }
    public void setPath(String path){ this.path = path;}
    public void setMethod(RestMethods method) {this.method = method;}
    public void setStrategy(Strategy strategy){ this.strategy = strategy;}
    public void setScript(String script){ this.script = script;}
    public void addResponse(DynamicResponseItem response) {this.responses.add(response);}
}
