package com.tutv.android.db.dao

import androidx.room.*
import com.tutv.android.domain.Series
import com.tutv.android.domain.Season
import com.tutv.android.domain.Episode
import com.tutv.android.domain.SeriesListAndSeriesMap
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

@Dao
interface SeriesDao {
    @Query("SELECT * FROM series WHERE :id = series_id")
    fun getSeriesById(id: Int): Single<Series>

    @Query("SELECT * FROM season WHERE :id = series_id")
    fun getSeasonsFromSeries(id: Int): Single<List<Season>>

    @Query("SELECT * FROM episode WHERE :id = season_id")
    fun getEpisodesFromSeasons(id: Int): Single<List<Episode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(s: Series)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(s: Season)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(e: Episode)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(seriesList: List<Series>)

    @Update
    fun update(e: Episode)

    @Update
    fun update(s: Season?)

    @Update
    fun update(s: Series?)

    @Delete
    fun remove(s: Series?)

    @Query("SELECT * FROM series WHERE series.series_id IN (SELECT series_id FROM serieslists_and_series_map WHERE series_list_id = :listId)")
    fun getSeriesListByListId(listId: String?): Single<List<Series>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMaps(map: List<SeriesListAndSeriesMap>)

}
