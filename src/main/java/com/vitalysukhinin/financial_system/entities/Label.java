package com.vitalysukhinin.financial_system.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Label {

    @Id
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "label", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> transactions;

    public Label(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Label() {
    }

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
}
