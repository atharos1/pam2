package com.tutv.android.repository;

import com.tutv.android.datasource.retrofit.endpoint.GenreAPI;
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI;
import com.tutv.android.db.dao.SeriesDao;
import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Series;

import java.util.List;

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
        return seriesAPI.getSeriesById(id);
    }

    public Single<Genre> getGenreById(int genreId, int page) {
        return genreAPI.getById(genreId, 10, page).subscribeOn(Schedulers.io());
    }

    public Single<List<Series>> getSeriesSearch(String name, int page, Integer genre, Integer network) {
        return seriesAPI.getSeriesSearch(name, 10, page, genre, network).subscribeOn(Schedulers.io());
    }
}
