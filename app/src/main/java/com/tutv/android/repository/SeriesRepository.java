package com.tutv.android.repository;

import com.tutv.android.datasource.dto.SeriesFollowedDTO;
import com.tutv.android.datasource.dto.ResourceViewedDTO;
import com.tutv.android.datasource.retrofit.endpoint.GenreAPI;
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI;
import com.tutv.android.db.dao.SeriesDao;
import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.Series;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SeriesRepository {

    private final SeriesDao seriesDao;
    private final SeriesAPI seriesAPI;
    private final GenreAPI genreAPI;
    private final UserRepository userRepository;

    public SeriesRepository(SeriesDao seriesDao, SeriesAPI seriesAPI, GenreAPI genreAPI, UserRepository userRepository) {
        this.seriesDao = seriesDao;
        this.seriesAPI = seriesAPI;
        this.genreAPI = genreAPI;
        this.userRepository = userRepository;
    }

    public Single<Series> getSeriesById(int id) {
        return seriesDao.getFullSeriesById(id)
                .onErrorResumeNext(throwable -> seriesAPI.getSeriesById(id));
    }

    public Single<Genre> getGenreById(int genreId) {
        return genreAPI.getById(genreId, 10, 1);
    }

    public Single<Series> setEpisodeViewed(Series series, Season s, Episode e) {
        return seriesAPI.setSeriesViewed(series.getId(), s.getNumber(), e.getNumEpisode(), new ResourceViewedDTO(e.getLoggedInUserViewed() == null ? true : e.getLoggedInUserViewed() == false ? true : false))
                .flatMap(resourceViewedDTO -> {
                    e.setLoggedInUserViewed(resourceViewedDTO.isViewedByUser());
                    seriesDao.update(e);
                    return Single.just(series);
                });
    }

    public Single<Series> setFollowSeries(Series series) {
         return userRepository.getCurrentUser()
                .flatMap(user -> seriesAPI.setFollowSeries(user.getId(), new SeriesFollowedDTO(series.getId())))
                .flatMap(seriesFollowedResponseDTO -> {
                    series.setLoggedInUserFollows(seriesFollowedResponseDTO.getLoggedInUserFollows());
                    series.setFollowers(seriesFollowedResponseDTO.getFollowers());
                    seriesDao.insert(series);
                    return Single.just(series);
                });
    }

    public Single<Series> unfollowSeries(Series series) {
        return userRepository.getCurrentUser()
                .flatMap(user -> seriesAPI.setUnfollowSeries(user.getId(), series.getId()))
                .flatMap(seriesFollowedResponseDTO -> {
                    series.setLoggedInUserFollows(seriesFollowedResponseDTO.getLoggedInUserFollows());
                    series.setFollowers(seriesFollowedResponseDTO.getFollowers());
                    seriesDao.insert(series);
                    return Single.just(series);
                });
    }

}