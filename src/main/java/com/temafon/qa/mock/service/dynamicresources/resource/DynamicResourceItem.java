package com.temafon.qa.mock.service.dynamicresources.resource;

import com.temafon.qa.mock.service.data.dataSet.Script;
import com.temafon.qa.mock.service.dynamicresources.constants.RestMethods;
import com.temafon.qa.mock.service.dynamicresources.constants.Strategy;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class DynamicResourceItem {

    private String path;
    private RestMethods method;
    private String script;
    private Strategy strategy;
    private List<DynamicResponse> responses;
    private DynamicResponse defaultResponse;
    private int sequenceCount;

    public DynamicResourceItem(String path, RestMethods method, Strategy strategy){
        this.sequenceCount = 0;
        this.responses = new ArrayList<>();
        setPath(path);
        setMethod(method);
        setStrategy(strategy);
    }

    public DynamicResponse getNextResponse(){
        DynamicResponse response = responses.get(sequenceCount);
        sequenceCount = sequenceCount + 1 < responses.size() ? sequenceCount + 1 : 0;
        return response;
    }

    public DynamicResponse getDefaultResponse() { return defaultResponse; }
    public String getPath(){return path;}
    public RestMethods getMethod() {return method;}
    public Strategy getStrategy(){ return strategy;}
    public String getScript(){return script;}
    public List<DynamicResponse> getResponses() {return responses;}

    public void setDefaultResponse(DynamicResponse response) {this.defaultResponse = response;}
    public void setPath(String path){ this.path = path;}
    public void setMethod(RestMethods method) {this.method = method;}
    public void setStrategy(Strategy strategy){ this.strategy = strategy;}
    public void setScript(String script){ this.script = script;}
    public void addResponse(DynamicResponse response) {this.responses.add(response);}
}
