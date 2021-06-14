package com.tutv.android.datasource.retrofit.endpoint;

import com.tutv.android.domain.Series;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SeriesAPI {

    @GET("series/{id}")
    Single<Series> getSeriesById(@Path("id") int id);

    @GET("series")
    Single<List<Series>> getSeriesSearch(@Query("name") String name, @Query("pagesize") int pageSize, @Query("page") int page,
                                       @Query("genre") Integer genre, @Query("network") Integer network);

}
