package com.jpcami.tads.xsearch.entity;

import java.io.Serializable;
import java.util.Objects;

public class Skill implements Serializable {

    private Integer id;

    private String name;

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

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Skill && Objects.equals(((Skill) obj).getId(), getId());
    }
}
