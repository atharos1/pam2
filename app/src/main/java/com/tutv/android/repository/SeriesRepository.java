package com.tutv.android.repository;

import com.tutv.android.datasource.dto.ResourceFollowedDTO;
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

    public SeriesRepository(SeriesDao seriesDao, SeriesAPI seriesAPI, GenreAPI genreAPI) {
        this.seriesDao = seriesDao;
        this.seriesAPI = seriesAPI;
        this.genreAPI = genreAPI;
    }

    public Single<Series> getSeriesById(int id) {
        return seriesDao.getSeriesById(id)
                .observeOn(Schedulers.io())
                .doOnError(throwable -> seriesAPI.getSeriesById(id)
                                        .doOnSuccess(seriesDao::insertWholeSeries));
    }

    public Single<Genre> getGenreById(int genreId) {
        return genreAPI.getById(genreId, 10, 1);
    }

    public Single<Series> setEpisodeViewed(Series series, Season s, Episode e) {
        return seriesAPI.setSeriesViewed(series.getId(), s.getNumber(), e.getNumEpisode(), new ResourceViewedDTO(e.getLoggedInUserViewed() == null ? true : e.getLoggedInUserViewed() == false ? true : false))
                .observeOn(Schedulers.io())
                .flatMap(resourceViewedDTO -> seriesAPI.getSeriesById(series.getId()))
                .doOnSuccess(seriesDao::insertWholeSeries);
    }

    public Single<Series> setFollowSeries(Series series) {
        return seriesAPI.setFollowSeries(series.getId(), new ResourceFollowedDTO(series.getLoggedInUserFollows() == null ? true : series.getLoggedInUserFollows() == false ? true : false))
                .observeOn(Schedulers.io())
                .flatMap(resourceFollowedDTO -> seriesAPI.getSeriesById(series.getId()))
                .doOnSuccess(seriesDao::insert);
    }

}