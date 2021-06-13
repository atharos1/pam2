package com.tutv.android.repository;

import com.tutv.android.datasource.retrofit.endpoint.GenreAPI;
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI;
import com.tutv.android.db.dao.SeriesDao;
import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Series;

import io.reactivex.Single;

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

    public Single<Genre> getGenreById(int genreId) {
        return genreAPI.getById(genreId, 10, 1);
    }
}
