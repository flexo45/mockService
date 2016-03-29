package com.temafon.qa.mock.service.dynamicresources.resource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicResponseItem {

    public DynamicResponseItem(String name){
        this.id = -1;
        setName(name);
        setResponseCode(HttpServletResponse.SC_OK);
        this.headers = new HashMap<>();
    }

    public DynamicResponseItem(long id, String name){
        this.id = id;
        setName(name);
        setResponseCode(HttpServletResponse.SC_OK);
        this.headers = new HashMap<>();
    }

    private long id;
    private String name;
    private int responseCode;
    private Map<String, String> headers;
    private byte[] content;
    private String script;

    public String getName() {return name;}
    public int getResponseCode() {return responseCode;}
    public Map<String, String> getHeaders() {return headers;}
    public byte[] getContent() {return content;}
    public String getScript() {return script;}
    public long getId() {return id;}

    public void setName(String name) { this.name = name;}
    public void setResponseCode(int code) { this.responseCode = code;}
    public void setContent(byte[] content) { this.content = content;}
    public void setScript(String script) { this.script = script;}

    public void addHeader(String key, String value){
        headers.put(key, value);
    }

    public void processResponse(HttpServletResponse response) throws IOException{
        if(script == null){
            response.setStatus(responseCode);

            for(Map.Entry<String, String> it : headers.entrySet()){
                response.setHeader(it.getKey(), it.getValue());
            }

            if(content != null){
                response.getWriter().println(new String(content, "UTF-8"));
            }
        }
        else {
            //response send to script handler
        }
    }
}
