package com.tutv.android.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "series")
public class Series {

    @PrimaryKey
    private int id;
    private int followers;

    private String name;
    private String seriesDescription;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeriesDescription() {
        return seriesDescription;
    }

    public void setSeriesDescription(String seriesDescription) {
        this.seriesDescription = seriesDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
