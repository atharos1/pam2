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

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SeriesRepository {

    private final SeriesDao seriesDao;
    private final SeriesAPI seriesAPI;
    private final GenreAPI genreAPI;
    private final NetworksAPI networksAPI;

    private final UserRepository userRepository;

    public SeriesRepository(SeriesDao seriesDao, SeriesAPI seriesAPI, GenreAPI genreAPI, NetworksAPI networksAPI, UserRepository userRepository) {
        this.seriesDao = seriesDao;
        this.seriesAPI = seriesAPI;
        this.genreAPI = genreAPI;
        this.networksAPI = networksAPI;
        this.userRepository = userRepository;
    }

    public Single<Series> getSeriesById(int id) {
        return seriesDao.getFullSeriesById(id)
                .subscribeOn(Schedulers.io())
                .flatMap(series -> {
                    if(series.getSeasons() == null || series.getSeasons().size() == 0) {
                        return seriesAPI.getSeriesById(id)
                                .subscribeOn(Schedulers.io())
                                .doOnSuccess(s -> seriesDao.insertWholeSeries(s));
                    } else {
                        return Single.just(series);
                    }
                })
                .onErrorResumeNext(throwable -> seriesAPI.getSeriesById(id));
    }

    public Single<Genre> getGenreById(int genreId, int page) {
        return genreAPI.getById(genreId, 6, page).subscribeOn(Schedulers.io());
    }

    public Single<List<Series>> getSeriesSearch(String name, int page, Integer genre, Integer network) {
        return seriesAPI.getSeriesSearch(name, 18, page, genre, network).subscribeOn(Schedulers.io());
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
                .subscribeOn(Schedulers.io())
                .flatMap(resourceViewedDTO -> {
                    episode.setLoggedInUserViewed(resourceViewedDTO.isViewedByUser());
                    seriesDao.insert(episode);
                    return Single.just(series);
                });
    }

    public Single<Series> setFollowSeries(Series series) {
         return userRepository.getCurrentUser()
                 .subscribeOn(Schedulers.io())
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
                .subscribeOn(Schedulers.io())
                .flatMap(user -> seriesAPI.setUnfollowSeries(user.getId(), series.getId()))
                .flatMap(seriesFollowedResponseDTO -> {
                    series.setLoggedInUserFollows(seriesFollowedResponseDTO.getLoggedInUserFollows());
                    series.setFollowers(seriesFollowedResponseDTO.getFollowers());
                    seriesDao.update(series);
                    return Single.just(series);
                });
    }



}