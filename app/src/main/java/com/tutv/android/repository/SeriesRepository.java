package com.tutv.android.repository;

import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI;
import com.tutv.android.db.dao.SeriesDao;
import com.tutv.android.domain.Series;

import io.reactivex.Single;

public class SeriesRepository {

    private final SeriesDao seriesDao;
    private final SeriesAPI seriesAPI;

    public SeriesRepository(SeriesDao seriesDao, SeriesAPI seriesAPI) {
        this.seriesDao = seriesDao;
        this.seriesAPI = seriesAPI;
    }

    public Single<Series> getSeriesById(int id) {
        return seriesAPI.getSeriesById(id);
    }

}
