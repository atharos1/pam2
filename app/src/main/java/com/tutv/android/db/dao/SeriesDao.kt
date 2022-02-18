package com.tutv.android.db.dao

import androidx.room.*
import com.tutv.android.domain.Episode
import com.tutv.android.domain.Season
import com.tutv.android.domain.Series
import com.tutv.android.domain.SeriesListAndSeriesMap
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

@Dao
interface SeriesDao {
    @Query("SELECT * FROM series WHERE :id = series_id")
    open fun getSeriesById(id: Int): Single<Series?>?

    @Query("SELECT * FROM season WHERE :id = series_id")
    open fun getSeasonsFromSeries(id: Int): Single<MutableList<Season?>?>?

    @Query("SELECT * FROM episode WHERE :id = season_id")
    open fun getEpisodesFromSeasons(id: Int): Single<MutableList<Episode?>?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    open fun insert(s: Series?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    open fun insert(s: Season?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    open fun insert(e: Episode?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    open fun insertAll(seriesList: MutableList<Series?>?)

    @Update
    open fun update(e: Episode?)

    @Update
    open fun update(s: Season?)

    @Update
    open fun update(s: Series?)

    @Delete
    open fun remove(s: Series?)

    @Query("SELECT * FROM series WHERE series.series_id IN (SELECT series_id FROM serieslists_and_series_map WHERE series_list_id = :listId)")
    open fun getSeriesListByListId(listId: String?): Single<MutableList<Series?>?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    open fun insertAllMaps(map: MutableList<SeriesListAndSeriesMap?>?)
    fun insertWholeSeries(series: Series?) {
        insert(series)
        for (season in series.getSeasons()) {
            season.seriesId = series.getId()
            insert(season)
            for (episode in season.episodes) {
                episode.seasonId = season.id
                insert(episode)
            }
        }
    }

    fun getFullSeriesById(id: Int): Single<Series?>? {
        return getSeriesById(id)
                .observeOn(Schedulers.io())
                .doOnSuccess { series: Series? ->
                    val seasons = getSeasonsFromSeries(id).observeOn(Schedulers.io()).blockingGet()
                    series.setSeasons(seasons)
                    for (season in seasons) {
                        val episodes = getEpisodesFromSeasons(season.getId()).blockingGet()
                        season.setEpisodes(episodes)
                    }
                }
    }
}