package com.temafon.qa.mock.service.data.dataSet;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dynamic_responses")
public class DynamicResponse implements Serializable {
    /** private String name;
     private int responseCode;
     private Map<String, String> headers;
     private byte[] content;
     private String script;*/

    private static final long serialVersionUID = -2645728657104785774L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id){this.id = id;}

    @Column(name = "name", nullable = false)
    private String name;
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    @Column(name = "code", nullable = false)
    private int code;
    public int getCode(){return this.code;}
    public void setCode(int code){this.code = code;}

    @Column(name = "content")
    private byte[] content;
    public byte[] getContent(){return this.content;}
    public void setContent(byte[] content){this.content = content;}

    @Column(name = "script")
    private String script;
    public String getScript(){return this.script;}
    public void setScript(String script){this.script = script;}

    @ManyToOne
    @JoinColumn(name = "dynamic_resource_id")
    private DynamicResource dynamicResource;
    public DynamicResource getDynamicResource(){return this.dynamicResource;}
    public void setDynamicResource(DynamicResource dynamicResource) {this.dynamicResource = dynamicResource;}

    public DynamicResponse(){}

    public DynamicResponse(long id, String name, int code, byte[] content, String script, DynamicResource dynamicResource){
        setId(id);
        setName(name);
        setCode(code);
        setContent(content);
        setScript(script);
        setDynamicResource(dynamicResource);
    }

    public DynamicResponse(String name, int code, byte[] content, String script, DynamicResource dynamicResource){
        setId(-1);
        setName(name);
        setCode(code);
        setContent(content);
        setScript(script);
        setDynamicResource(dynamicResource);
    }

    @Override
    public String toString(){
        return "DynamicResponse{id=" + id + ", name=" + name + "}";
    }

}
