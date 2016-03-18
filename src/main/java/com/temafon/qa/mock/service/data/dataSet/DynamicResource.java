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

    @Column(name = "path", unique = true)
    private String path;

    @ManyToOne
    @JoinColumn(name = "method_id")
    private Method method;

    @ManyToOne
    @JoinColumn(name = "dispatch_strategy_id")
    private DispatchStrategy dispatch_strategy;

    public long getId(){return id;}
    public String getPath() {return path;}
    public Method getMethod() {return method;}
    public DispatchStrategy getDispatch_strategy() {return dispatch_strategy;}

    public void setId(long id) {this.id = id;}
    public void setPath(String path) {this.path = path;}
    public void setMethod(Method method) {this.method = method;}
    public void setDispatch_strategy(DispatchStrategy dispatch_strategy) {this.dispatch_strategy = dispatch_strategy;}

    public DynamicResource(){}

    public DynamicResource(long id, String path, Method method, DispatchStrategy dispatch_strategy){
        this.setId(id);
        this.setPath(path);
        this.setMethod(method);
        this.setDispatch_strategy(dispatch_strategy);
    }

    public DynamicResource(String path, Method method, DispatchStrategy dispatch_strategy_id){
        this.setId(-1);
        this.setPath(path);
        this.setMethod(method);
        this.setDispatch_strategy(dispatch_strategy_id);
    }

    @Override
    public String toString(){
        return "DynamicResource{id=" + id + ", path=" + path + "}";
    }
}
