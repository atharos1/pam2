package com.tutv.android.datasource.retrofit.endpoint;

import com.tutv.android.domain.Series;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SeriesAPI {

    @GET("series/{id}")
    Single<Series> getSeriesById(@Path("id") int id);

}
