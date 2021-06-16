package com.tutv.android.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(primaryKeys = {"series_list_id", "series_id"}, tableName = "serieslists_and_series_map")
public class SeriesListAndSeriesMap {
    @NotNull
    @ColumnInfo(name = "series_list_id")
    private String seriesListId;

    @NotNull
    @ColumnInfo(name = "series_id")
    private int seriesId;

    public String getSeriesListId() {
        return seriesListId;
    }

    public void setSeriesListId(String seriesListId) {
        this.seriesListId = seriesListId;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }
}
