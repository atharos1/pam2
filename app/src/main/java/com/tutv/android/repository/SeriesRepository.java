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
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

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
                .onErrorResumeNext(throwable -> seriesAPI.getSeriesById(id));
    }

    public Single<Genre> getGenreById(int genreId, int page) {
        return genreAPI.getById(genreId, 6, page).subscribeOn(schedulerProvider.io());
    }

    public Single<List<Series>> getSeriesSearch(String name, int page, Integer genre, Integer network) {
        return seriesAPI.getSeriesSearch(name, 18, page, genre, network).subscribeOn(schedulerProvider.io());
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

    //TODO porque carga en io? Eos bloquea la UI
    public Single<Series> setEpisodeViewed(Series series, Season season, Episode episode) {
        return seriesAPI.setSeriesViewed(series.getId(), season.getNumber(), episode.getNumEpisode(), new ResourceViewedDTO(episode.getLoggedInUserViewed() == null ? true : !episode.getLoggedInUserViewed()))
                .subscribeOn(schedulerProvider.io())
                .flatMap(resourceViewedDTO -> {
                    episode.setLoggedInUserViewed(resourceViewedDTO.isViewedByUser());
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