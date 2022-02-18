package com.tutv.android.repository

import com.tutv.android.datasource.dto.ResourceViewedDTO
import com.tutv.android.datasource.dto.SeriesFollowedDTO
import com.tutv.android.datasource.dto.SeriesFollowedResponseDTO
import com.tutv.android.datasource.retrofit.endpoint.GenreAPI
import com.tutv.android.datasource.retrofit.endpoint.NetworksAPI
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI
import com.tutv.android.db.dao.SeriesDao
import com.tutv.android.domain.*
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.functions.Function
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.util.*

class SeriesRepository(private val seriesDao: SeriesDao?, private val seriesAPI: SeriesAPI?, private val genreAPI: GenreAPI?, private val networksAPI: NetworksAPI?,
                       private val userRepository: UserRepository?, private val schedulerProvider: BaseSchedulerProvider?) {
    fun getSeriesById(id: Int): Single<Series?>? {
        return seriesDao.getFullSeriesById(id)
                .subscribeOn(schedulerProvider.io())
                .flatMap { series: Series? ->
                    if (series.getSeasons() == null || series.getSeasons().size == 0) {
                        return@flatMap seriesAPI.getSeriesById(id)
                                .subscribeOn(schedulerProvider.io())
                                .doOnSuccess { s: Series? -> seriesDao.insertWholeSeries(s) }
                    } else {
                        return@flatMap Single.just(series)
                    }
                }
                .onErrorResumeNext { throwable: Throwable? -> seriesAPI.getSeriesById(id).subscribeOn(schedulerProvider.io()).doOnSuccess { s: Series? -> seriesDao.insertWholeSeries(s) } }
    }

    fun getGenreById(genreId: Int, page: Int, pageSize: Int): Single<Genre?>? {
        return genreAPI.getById(genreId, pageSize, page)
                .subscribeOn(schedulerProvider.io())
                .flatMap { item: Genre? -> Single.just(item) }
    }

    fun getSeriesByGenreId(genreId: Int, page: Int, pageSize: Int): Single<MutableList<Series?>?>? {
        val listId = "genre_$genreId"
        return seriesDao.getSeriesListByListId(listId)
                .subscribeOn(schedulerProvider.io())
                .flatMap { seriesList: MutableList<Series?>? ->
                    if (seriesList == null || seriesList.size == 0) {
                        return@flatMap getSeriesSearch(null, page, genreId, null, pageSize)
                                .flatMap(Function<MutableList<Series?>?, SingleSource<out MutableList<Series?>?>?> { sList: MutableList<Series?>? ->
                                    val seriesListAndSeriesMaps: MutableList<SeriesListAndSeriesMap?> = LinkedList()
                                    for (s in sList) seriesListAndSeriesMaps.add(SeriesListAndSeriesMap(listId, s.getId()))
                                    seriesDao.insertAllMaps(seriesListAndSeriesMaps)
                                    Single.just(sList)
                                })
                    } else {
                        return@flatMap Single.just(seriesList)
                    }
                }
                .onErrorResumeNext { throwable: Throwable? -> getSeriesSearch(null, page, genreId, null, pageSize) }
    }

    fun getSeriesSearch(name: String?, page: Int, genre: Int?, network: Int?, pageSize: Int): Single<MutableList<Series?>?>? {
        return seriesAPI.getSeriesSearch(name, pageSize, page, genre, network)
                .subscribeOn(schedulerProvider.io())
                .flatMap { seriesList: MutableList<Series?>? ->
                    seriesDao.insertAll(seriesList)
                    Single.just(seriesList)
                }
    }

    fun getFeatured(): Single<MutableList<Series?>?>? {
        return seriesAPI.getFeatured()
    }

    fun getGenres(): Single<MutableList<Genre?>?>? {
        return genreAPI.getAll()
    }

    fun getNetworks(): Single<MutableList<Network?>?>? {
        return networksAPI.getAll()
    }

    fun setEpisodeViewed(series: Series?, season: Season?, episode: Episode?): Single<Series?>? {
        return seriesAPI.setSeriesViewed(series.getId(), season.getNumber(), episode.getNumEpisode(), ResourceViewedDTO(if (episode.getLoggedInUserViewed() == null) true else !episode.getLoggedInUserViewed()))
                .subscribeOn(schedulerProvider.io())
                .flatMap { resourceViewedDTO: ResourceViewedDTO? ->
                    episode.setLoggedInUserViewed(resourceViewedDTO.isViewedByUser())
                    seriesDao.insert(series)
                    seriesDao.insert(season)
                    seriesDao.insert(episode)
                    Single.just(series)
                }
    }

    fun setFollowSeries(series: Series?): Single<Series?>? {
        return userRepository.getCurrentUser()
                .subscribeOn(schedulerProvider.io())
                .flatMap { user: User? -> seriesAPI.setFollowSeries(user.getId(), SeriesFollowedDTO(series.getId())) }
                .flatMap { seriesFollowedResponseDTO: SeriesFollowedResponseDTO? ->
                    series.setLoggedInUserFollows(seriesFollowedResponseDTO.getLoggedInUserFollows())
                    series.setFollowers(seriesFollowedResponseDTO.getFollowers())
                    seriesDao.update(series)
                    Single.just(series)
                }
    }

    fun unfollowSeries(series: Series?): Single<Series?>? {
        return userRepository.getCurrentUser()
                .subscribeOn(schedulerProvider.io())
                .flatMap { user: User? -> seriesAPI.setUnfollowSeries(user.getId(), series.getId()) }
                .flatMap { seriesFollowedResponseDTO: SeriesFollowedResponseDTO? ->
                    series.setLoggedInUserFollows(seriesFollowedResponseDTO.getLoggedInUserFollows())
                    series.setFollowers(seriesFollowedResponseDTO.getFollowers())
                    seriesDao.update(series)
                    Single.just(series)
                }
    }

}