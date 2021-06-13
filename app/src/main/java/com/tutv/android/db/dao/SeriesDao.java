package com.tutv.android.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.Series;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface SeriesDao {

    @Query("SELECT * FROM series WHERE :id = series_id")
    Single<Series> getSeriesById(int id);

    @Insert
    void insert(Series s);

    @Insert
    void insert(Season s);

    @Insert
    void insert(Episode e);

    @Update
    void update(Episode e);

    @Update
    void update(Season s);

    @Update
    void update(Series s);

    @Delete
    void remove(Series s);

}
