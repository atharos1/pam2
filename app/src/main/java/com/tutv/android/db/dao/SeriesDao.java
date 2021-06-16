package com.tutv.android.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.Series;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Dao
public interface SeriesDao {

    @Query("SELECT * FROM series WHERE :id = series_id")
    Single<Series> getSeriesById(int id);

    @Query("SELECT * FROM season WHERE :id = series_id")
    Single<List<Season>> getSeasonsFromSeries(int id);

    @Query("SELECT * FROM episode WHERE :id = season_id")
    Single<List<Episode>> getEpisodesFromSeasons(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Series s);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Season s);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Episode e);

    @Update
    void update(Episode e);

    @Update
    void update(Season s);

    @Update
    void update(Series s);

    @Delete
    void remove(Series s);

    //@Query("SELECT * FROM series WHERE series.series_id IN (SELECT series_id FROM serieslists_and_series_map WHERE series_list_id = :listId)")
    //Single<List<Series>> getSeriesListByListId(String listId);

    default void insertWholeSeries(Series series) {
        insert(series);
        for(Season season : series.getSeasons()) {
            season.setSeriesId(series.getId());
            insert(season);
            for(Episode episode : season.getEpisodes()) {
                episode.setSeasonId(season.getId());
                insert(episode);
            }
        }
    }

    default Single<Series> getFullSeriesById(int id) {
        return getSeriesById(id)
        .observeOn(Schedulers.io())
        .doOnSuccess(series -> {
            List<Season> seasons = getSeasonsFromSeries(id).observeOn(Schedulers.io()).blockingGet();
            series.setSeasons(seasons);
            for(Season season : seasons) {
                List<Episode> episodes = getEpisodesFromSeasons(season.getId()).blockingGet();
                season.setEpisodes(episodes);
            }
        });
    }
}
