package com.moss.demo.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;



@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @JsonIgnore
    public boolean isNew() {
        return this.id == null;
    }

}

