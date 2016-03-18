package com.temafon.qa.mock.service.data.dataSet;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dispatch_strategies")
public class DispatchStrategy implements Serializable {
    private static final long serialVersionUID = -6745345756568564853L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    public DispatchStrategy() {}

    public DispatchStrategy(long id, String name){
        this.setId(id);
        this.setName(name);
    }

    public DispatchStrategy(String name){
        this.setId(-1);
        this.setName(name);
    }

    public long getId(){return id;}
    public String getName() {return name;}

    public void setId(long id) {this.id = id;}
    public void setName(String name) {this.name = name;}

    @Override
    public String toString(){
        return "DispatchStrategy{id=" + id + ", name=" + name + "}";
    }
}