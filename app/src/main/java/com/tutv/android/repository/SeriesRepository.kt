package com.tutv.android.repository

import com.tutv.android.db.dao.SeriesDao
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI
import com.tutv.android.datasource.retrofit.endpoint.GenreAPI
import com.tutv.android.datasource.retrofit.endpoint.NetworksAPI
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import com.tutv.android.datasource.dto.ResourceViewedDTO
import com.tutv.android.datasource.dto.SeriesFollowedResponseDTO
import com.tutv.android.datasource.dto.SeriesFollowedDTO
import com.tutv.android.domain.*
import io.reactivex.Single
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
            .flatMap { series: Series ->
                if ((series.seasons ?: EMPTY_LIST).isEmpty()) {
                    return@flatMap seriesAPI.getSeriesById(id)
                        .subscribeOn(schedulerProvider.io())
                        .doOnSuccess { s: Series? -> seriesDao.insertWholeSeries(s) }
                } else {
                    return@flatMap Single.just(series)
                }
            }
            .onErrorResumeNext {
                seriesAPI.getSeriesById(id).subscribeOn(
                    schedulerProvider.io()
                ).doOnSuccess { s: Series? -> seriesDao.insertWholeSeries(s) }
            }
    }

    fun getGenreById(genreId: Int, page: Int, pageSize: Int): Single<Genre> {
        return genreAPI.getById(genreId, pageSize, page)
            .subscribeOn(schedulerProvider.io())
            .flatMap { item: Genre? -> Single.just(item) }
    }

    fun getSeriesByGenreId(genreId: Int, page: Int, pageSize: Int): Single<List<Series>> {
        val listId = "genre_$genreId"
        return seriesDao.getSeriesListByListId(listId)
            .subscribeOn(schedulerProvider.io())
            .flatMap { seriesList: List<Series>? ->
                if (seriesList == null || seriesList.size == 0) {
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
                            Single.just(sList)
                        }
                } else {
                    return@flatMap Single.just(seriesList)
                }
            }
            .onErrorResumeNext { throwable: Throwable? ->
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
            .flatMap { seriesList: List<Series> ->
                seriesDao.insertAll(seriesList)
                Single.just(seriesList)
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
            ResourceViewedDTO(if (episode.loggedInUserViewed == null) true else !episode.loggedInUserViewed!!)
        )
            .subscribeOn(schedulerProvider.io())
            .flatMap { resourceViewedDTO: ResourceViewedDTO ->
                episode.loggedInUserViewed = resourceViewedDTO.isViewedByUser
                seriesDao.insert(series)
                seriesDao.insert(season)
                seriesDao.insert(episode)
                Single.just(series)
            }
    }

    fun setFollowSeries(series: Series): Single<Series> {
        return userRepository.currentUser
            .subscribeOn(schedulerProvider.io())
            .flatMap { user: User ->
                seriesAPI.setFollowSeries(
                    user.id,
                    SeriesFollowedDTO(series.id)
                )
            }
            .flatMap { seriesFollowedResponseDTO: SeriesFollowedResponseDTO ->
                series.loggedInUserFollows = seriesFollowedResponseDTO.loggedInUserFollows
                series.followers = seriesFollowedResponseDTO.followers
                seriesDao.update(series)
                Single.just(series)
            }
    }

    fun unfollowSeries(series: Series): Single<Series> {
        return userRepository.currentUser
            .subscribeOn(schedulerProvider.io())
            .flatMap { user: User -> seriesAPI.setUnfollowSeries(user.id, series.id) }
            .flatMap { seriesFollowedResponseDTO: SeriesFollowedResponseDTO ->
                series.loggedInUserFollows = seriesFollowedResponseDTO.loggedInUserFollows
                series.followers = seriesFollowedResponseDTO.followers
                seriesDao.update(series)
                Single.just(series)
            }
    }
}