package com.temafon.qa.mock.service.dynamicresources;

import com.temafon.qa.mock.service.data.dataSet.Script;

public class DynamicResourceItem {

    private String path;
    private String method;
    private String script;
    private String strategy;

    public DynamicResourceItem(String path, String method, String strategy){
        this.path = path;
        this.method = method;
        this.strategy = strategy;
    }

    public String getPath(){return path;}
    public String getMethod() {return method;}
    public String getStrategy(){ return strategy;}
    public String getScript(){return script;}

    public void setPath(String path){ this.path = path;}
    public void setMethod(String method) {this.method = method;}
    public void setStrategy(String strategy){ this.strategy = strategy;}
    public void setScript(String script){ this.script = script;}

}
