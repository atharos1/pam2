package com.tutv.android.repository;

import com.tutv.android.datasource.dto.SeriesFollowedDTO;
import com.tutv.android.datasource.dto.ResourceViewedDTO;
import com.tutv.android.datasource.retrofit.endpoint.GenreAPI;
import com.tutv.android.datasource.retrofit.endpoint.NetworksAPI;
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI;
import com.tutv.android.db.dao.SeriesDao;
import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Network;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.Series;
import com.tutv.android.domain.SeriesListAndSeriesMap;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Single;

public class SeriesRepository {

    private final SeriesDao seriesDao;
    private final SeriesAPI seriesAPI;
    private final GenreAPI genreAPI;
    private final NetworksAPI networksAPI;

    private final UserRepository userRepository;

    private final BaseSchedulerProvider schedulerProvider;

    public SeriesRepository(SeriesDao seriesDao, SeriesAPI seriesAPI, GenreAPI genreAPI, NetworksAPI networksAPI,
                            UserRepository userRepository, BaseSchedulerProvider schedulerProvider) {
        this.seriesDao = seriesDao;
        this.seriesAPI = seriesAPI;
        this.genreAPI = genreAPI;
        this.networksAPI = networksAPI;
        this.userRepository = userRepository;
        this.schedulerProvider = schedulerProvider;
    }

    public Single<Series> getSeriesById(int id) {
        return seriesDao.getFullSeriesById(id)
                .subscribeOn(schedulerProvider.io())
                .flatMap(series -> {
                    if(series.getSeasons() == null || series.getSeasons().size() == 0) {
                        return seriesAPI.getSeriesById(id)
                                .subscribeOn(schedulerProvider.io())
                                .doOnSuccess(s -> seriesDao.insertWholeSeries(s));
                    } else {
                        return Single.just(series);
                    }
                })
                .onErrorResumeNext(throwable -> seriesAPI.getSeriesById(id).subscribeOn(schedulerProvider.io()).doOnSuccess(s -> seriesDao.insertWholeSeries(s)));
    }

    public Single<Genre> getGenreById(int genreId, int page, int pageSize) {
        return genreAPI.getById(genreId, pageSize, page)
                .subscribeOn(schedulerProvider.io())
                .flatMap(genre -> {

                    return Single.just(genre);
                });
    }

    public Single<List<Series>> getSeriesByGenreId(int genreId, int page, int pageSize) {
        final String listId = "genre_" + genreId;

        return seriesDao.getSeriesListByListId(listId)
                .subscribeOn(schedulerProvider.io())
                .flatMap(seriesList -> {
                    if(seriesList == null || seriesList.size() == 0) {
                        return getSeriesSearch(null, page, genreId, null, pageSize)
                                .flatMap(sList -> {
                                    List<SeriesListAndSeriesMap> seriesListAndSeriesMaps = new LinkedList<>();
                                    for(Series s : sList)
                                        seriesListAndSeriesMaps.add(new SeriesListAndSeriesMap(listId, s.getId()));
                                    seriesDao.insertAllMaps(seriesListAndSeriesMaps);

                                    return Single.just(sList);
                                });
                    } else {
                        return Single.just(seriesList);
                    }
                })
                .onErrorResumeNext(throwable -> getSeriesSearch(null, page, genreId, null, pageSize));
    }

    public Single<List<Series>> getSeriesSearch(String name, int page, Integer genre, Integer network, int pageSize) {
        return seriesAPI.getSeriesSearch(name, pageSize, page, genre, network)
                .subscribeOn(schedulerProvider.io())
                .flatMap(seriesList -> {
                    seriesDao.insertAll(seriesList);
                    return Single.just(seriesList);
                });
    }

    public Single<List<Series>> getFeatured() {
        return seriesAPI.getFeatured();
    }

    public Single<List<Genre>> getGenres() {
        return genreAPI.getAll();
    }

    public Single<List<Network>> getNetworks() {
        return networksAPI.getAll();
    }

    public Single<Series> setEpisodeViewed(Series series, Season season, Episode episode) {
        return seriesAPI.setSeriesViewed(series.getId(), season.getNumber(), episode.getNumEpisode(), new ResourceViewedDTO(episode.getLoggedInUserViewed() == null ? true : !episode.getLoggedInUserViewed()))
                .subscribeOn(schedulerProvider.io())
                .flatMap(resourceViewedDTO -> {
                    episode.setLoggedInUserViewed(resourceViewedDTO.isViewedByUser());
                    seriesDao.insert(series);
                    seriesDao.insert(season);
                    seriesDao.insert(episode);
                    return Single.just(series);
                });
    }

    public Single<Series> setFollowSeries(Series series) {
         return userRepository.getCurrentUser()
                 .subscribeOn(schedulerProvider.io())
                .flatMap(user -> seriesAPI.setFollowSeries(user.getId(), new SeriesFollowedDTO(series.getId())))
                .flatMap(seriesFollowedResponseDTO -> {
                    series.setLoggedInUserFollows(seriesFollowedResponseDTO.getLoggedInUserFollows());
                    series.setFollowers(seriesFollowedResponseDTO.getFollowers());
                    seriesDao.update(series);
                    return Single.just(series);
                });
    }

    public Single<Series> unfollowSeries(Series series) {
        return userRepository.getCurrentUser()
                .subscribeOn(schedulerProvider.io())
                .flatMap(user -> seriesAPI.setUnfollowSeries(user.getId(), series.getId()))
                .flatMap(seriesFollowedResponseDTO -> {
                    series.setLoggedInUserFollows(seriesFollowedResponseDTO.getLoggedInUserFollows());
                    series.setFollowers(seriesFollowedResponseDTO.getFollowers());
                    seriesDao.update(series);
                    return Single.just(series);
                });
    }
}