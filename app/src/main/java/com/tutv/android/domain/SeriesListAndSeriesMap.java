package com.tutv.android.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"series_list_id", "series_id"}, tableName = "serieslists_and_series_map")
public class SeriesListAndSeriesMap {
    @ColumnInfo(name = "series_list_id")
    private String seriesListId;

    @ColumnInfo(name = "series_id")
    private int seriesId;
}
