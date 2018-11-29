package com.jpcami.tads.xsearch.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Mutant implements Serializable {

    private Integer id;

    private String name;

    private List<Skill> skills;

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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Mutant && Objects.equals(((Mutant) obj).getId(), getId());
    }
}
