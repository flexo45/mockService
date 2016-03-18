package com.temafon.qa.mock.service.data.dataSet;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "methods")
public class Method implements Serializable {
    private static final long serialVersionUID = -8253572378357676245L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    public Method() {}

    public Method(long id, String name){
        this.setId(id);
        this.setName(name);
    }

    public Method(String name){
        this.setId(-1);
        this.setName(name);
    }

    public long getId(){return id;}
    public String getName() {return name;}

    public void setId(long id) {this.id = id;}
    public void setName(String name) {this.name = name;}

    @Override
    public String toString(){
        return "Method{id=" + id + ", name=" + name + "}";
    }
}
