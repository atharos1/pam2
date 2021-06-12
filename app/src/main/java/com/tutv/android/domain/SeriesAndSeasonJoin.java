package com.tutv.android.domain;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/*
* Solution to Room-and-Retrofit-impedance-mismatch-so-to-speak extracted from: https://stackoverflow.com/questions/50799324/many-to-many-relations-with-room-livedata
* */
@Entity(primaryKeys = {"series_id", "season_id"},
        foreignKeys = {
                @ForeignKey(entity = Series.class,
                            parentColumns = "series_id",
                            childColumns = "season_id"),
                @ForeignKey(entity = Season.class,
                            parentColumns = "season_id",
                            childColumns = "season_id")
        },
        tableName = "series_and_season_join")
public class SeriesAndSeasonJoin {

    @ColumnInfo(name = "series_id") public int series_id;
    @ColumnInfo(name = "season_id") public int season_id;

}
