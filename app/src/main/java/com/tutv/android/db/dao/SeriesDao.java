package com.tutv.android.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tutv.android.domain.Series;

@Dao
public interface SeriesDao {

    @Query("SELECT * FROM series WHERE :id = series_id")
    Series getSeriesById(int id);

    @Insert
    void insert(Series s);

    @Delete
    void remove(Series s);

}
