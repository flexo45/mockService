package com.temafon.qa.mock.service.data.dataSet;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dynamic_resources")
public class DynamicResource implements Serializable {
    private static final long serialVersionUID = -4537609702358236547L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return id;}
    public void setId(long id) {this.id = id;}

    @Column(name = "path", unique = true)
    private String path;
    public String getPath() {return path;}
    public void setPath(String path) {this.path = path;}

    @ManyToOne
    @JoinColumn(name = "method_id")
    private Method method;
    public Method getMethod() {return method;}
    public void setMethod(Method method) {this.method = method;}

    @ManyToOne
    @JoinColumn(name = "dispatch_strategy_id")
    private DispatchStrategy dispatch_strategy;
    public DispatchStrategy getDispatch_strategy() {return dispatch_strategy;}
    public void setDispatch_strategy(DispatchStrategy dispatch_strategy) {this.dispatch_strategy = dispatch_strategy;}

    @OneToOne
    @JoinColumn(name = "default_response_id")
    private DynamicResponse defaultDynamicResponse;
    public DynamicResponse getDefaultDynamicResponse() {return defaultDynamicResponse;}
    public void setDefaultDynamicResponse(DynamicResponse defaultDynamicResponse) {this.defaultDynamicResponse = defaultDynamicResponse;}

    public DynamicResource(){}

    public DynamicResource(long id, String path, Method method, DispatchStrategy dispatch_strategy
            , DynamicResponse defaultDynamicResponse){
        this.setId(id);
        this.setPath(path);
        this.setMethod(method);
        this.setDispatch_strategy(dispatch_strategy);
        this.setDefaultDynamicResponse(defaultDynamicResponse);
    }

    public DynamicResource(String path, Method method, DispatchStrategy dispatch_strategy_id
            , DynamicResponse defaultDynamicResponse){
        this.setId(-1);
        this.setPath(path);
        this.setMethod(method);
        this.setDispatch_strategy(dispatch_strategy_id);
        this.setDefaultDynamicResponse(defaultDynamicResponse);
    }

    @Override
    public String toString(){
        return "DynamicResource{id=" + id + ", path=" + path + "}";
    }
}
