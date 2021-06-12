package com.tutv.android.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tutv.android.domain.Season;
import com.tutv.android.domain.SeriesAndSeasonJoin;

import java.util.List;

@Dao
public interface SeriesAndSeasonDao {

    @Query("SELECT * FROM season INNER JOIN series_and_season_join ON season.season_id = series_and_season_join.season_id" +
            " WHERE series_and_season_join.series_id = :seriesId")
    List<Season> getSeasonsFromSeriesById(int seriesId);

    @Insert
    void insert(SeriesAndSeasonJoin join);

    @Delete
    void delete(SeriesAndSeasonJoin join);

}
