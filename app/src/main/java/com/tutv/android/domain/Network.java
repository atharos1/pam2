package com.tutv.android.domain;

import org.jetbrains.annotations.NotNull;

public class Network {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String toString() {
        return getName();
    }

    public Network(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
