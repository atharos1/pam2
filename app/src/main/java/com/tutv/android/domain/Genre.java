package com.tutv.android.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Genre {
    private int id;
    private String name;
    private String seriesUri;
    private List<Series> series;

    public String getSeriesUri() {
        return seriesUri;
    }

    public void setSeriesUri(String seriesUri) {
        this.seriesUri = seriesUri;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

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

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
