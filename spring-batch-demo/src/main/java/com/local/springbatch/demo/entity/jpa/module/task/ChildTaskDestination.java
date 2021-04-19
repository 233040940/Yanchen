package com.local.springbatch.demo.entity.jpa.module.task;

import com.local.common.entity.MataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @Create-By: yanchen 2021/4/9 02:13
 * @Description: TODO
 */
@Entity
public class ChildTaskDestination extends MataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false,unique = true)
    private String name;
    @Column(nullable = false)
    private Integer parentID;

    @ManyToOne
    @JoinColumn(name = "parentID",insertable = false,updatable = false)
    private TaskDestination taskDestination;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public TaskDestination getExtensionTaskDestination() {
        return taskDestination;
    }

    public void setExtensionTaskDestination(TaskDestination taskDestination) {
        this.taskDestination = taskDestination;
    }
}
