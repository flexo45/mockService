package com.temafon.qa.mock.service.data.dataSet;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "scripts")
public class Script implements Serializable {
    private static final long serialVersionUID = -7654652935755934654L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "dynamic_resource_id")
    private DynamicResource dynamicResource;

    public Script() {}

    public Script(long id, String text, DynamicResource resource){
        this.setId(id);
        this.setText(text);
        this.setDynamicResource(resource);
    }

    public Script(String text, DynamicResource resource){
        this.setId(-1);
        this.setText(text);
        this.setDynamicResource(resource);
    }

    public long getId(){return id;}
    public String getText() {return text;}
    public DynamicResource getDynamicResource() {return dynamicResource;}

    public void setId(long id) {this.id = id;}
    public void setText(String text) {this.text = text;}
    public void setDynamicResource(DynamicResource resource) {this.dynamicResource = resource;}

    @Override
    public String toString(){
        return "Script{id=" + id + ", text=" + text + "}";
    }
}