package com.tutv.android.domain;

import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tutv.android.db.converter.SeriesListToStringConverter;

import java.util.List;

public class SeriesList {
    @PrimaryKey
    private String id;

    private String name;

    @TypeConverters(SeriesListToStringConverter.class)
    private List<Series> list;

    private int lastLoadedPage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Series> getList() {
        return list;
    }

    public void setList(List<Series> list) {
        this.list = list;
    }

    public int getLastLoadedPage() {
        return lastLoadedPage;
    }

    public void setLastLoadedPage(int lastLoadedPage) {
        this.lastLoadedPage = lastLoadedPage;
    }
}
