package com.tutv.android.repository

import com.tutv.android.db.dao.SeriesDao
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI
import com.tutv.android.datasource.retrofit.endpoint.GenreAPI
import com.tutv.android.datasource.retrofit.endpoint.NetworksAPI
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import com.tutv.android.datasource.dto.ResourceViewedDTO
import com.tutv.android.datasource.dto.SeriesFollowedDTO
import com.tutv.android.domain.*
import io.reactivex.Single
import io.reactivex.Single.just
import java.util.*
import java.util.Collections.EMPTY_LIST

class SeriesRepository(
    private val seriesDao: SeriesDao,
    private val seriesAPI: SeriesAPI,
    private val genreAPI: GenreAPI,
    private val networksAPI: NetworksAPI,
    private val userRepository: UserRepository,
    private val schedulerProvider: BaseSchedulerProvider
) {
    fun getSeriesById(id: Int): Single<Series> {
        return seriesDao.getFullSeriesById(id)
            .subscribeOn(schedulerProvider.io())
            .flatMap {
                if ((it.seasons ?: EMPTY_LIST).isEmpty()) {
                    return@flatMap seriesAPI.getSeriesById(id)
                        .subscribeOn(schedulerProvider.io())
                        .doOnSuccess { s -> seriesDao.insertWholeSeries(s) }
                } else {
                    return@flatMap just(it)
                }
            }
            .onErrorResumeNext {
                seriesAPI.getSeriesById(id).subscribeOn(
                    schedulerProvider.io()
                ).doOnSuccess { seriesDao.insertWholeSeries(it) }
            }
    }

    fun getGenreById(genreId: Int, page: Int, pageSize: Int): Single<Genre> {
        return genreAPI.getById(genreId, pageSize, page)
            .subscribeOn(schedulerProvider.io())
            .flatMap { just(it) }
    }

    fun getSeriesByGenreId(genreId: Int, page: Int, pageSize: Int): Single<List<Series>> {
        val listId = "genre_$genreId"
        return seriesDao.getSeriesListByListId(listId)
            .subscribeOn(schedulerProvider.io())
            .flatMap {
                if (it.isNullOrEmpty()) {
                    return@flatMap getSeriesSearch(null, page, genreId, null, pageSize)
                        .flatMap { sList: List<Series> ->
                            val seriesListAndSeriesMaps: MutableList<SeriesListAndSeriesMap> =
                                LinkedList()
                            for (s in sList) seriesListAndSeriesMaps.add(
                                SeriesListAndSeriesMap(
                                    listId,
                                    s.id
                                )
                            )
                            seriesDao.insertAllMaps(seriesListAndSeriesMaps)
                            just(sList)
                        }
                } else {
                    return@flatMap just(it)
                }
            }
            .onErrorResumeNext {
                getSeriesSearch(
                    null,
                    page,
                    genreId,
                    null,
                    pageSize
                )
            }
    }

    fun getSeriesSearch(
        name: String?,
        page: Int,
        genre: Int?,
        network: Int?,
        pageSize: Int
    ): Single<List<Series>> {
        return seriesAPI.getSeriesSearch(name, pageSize, page, genre, network)
            .subscribeOn(schedulerProvider.io())
            .flatMap {
                seriesDao.insertAll(it)
                just(it)
            }
    }

    val featured: Single<List<Series>>
        get() = seriesAPI.featured
    val genres: Single<List<Genre>>
        get() = genreAPI.all
    val networks: Single<List<Network>>
        get() = networksAPI.all

    fun setEpisodeViewed(series: Series, season: Season, episode: Episode): Single<Series> {
        return seriesAPI.setSeriesViewed(
            series.id,
            season.number,
            episode.numEpisode,
            ResourceViewedDTO(episode.loggedInUserViewed?.not() ?: true)
        )
            .subscribeOn(schedulerProvider.io())
            .flatMap {
                episode.loggedInUserViewed = it.viewedByUser
                seriesDao.insert(series)
                seriesDao.insert(season)
                seriesDao.insert(episode)
                just(series)
            }
    }

    fun setFollowSeries(series: Series): Single<Series> {
        return userRepository.currentUser
            .subscribeOn(schedulerProvider.io())
            .flatMap {
                seriesAPI.setFollowSeries(
                    it.id,
                    SeriesFollowedDTO(series.id)
                )
            }
            .flatMap {
                series.loggedInUserFollows = it.loggedInUserFollows
                series.followers = it.followers
                seriesDao.update(series)
                just(series)
            }
    }

    fun unfollowSeries(series: Series): Single<Series> {
        return userRepository.currentUser
            .subscribeOn(schedulerProvider.io())
            .flatMap { seriesAPI.setUnfollowSeries(it.id, series.id) }
            .flatMap {
                series.loggedInUserFollows = it.loggedInUserFollows
                series.followers = it.followers
                seriesDao.update(series)
                just(series)
            }
    }
}