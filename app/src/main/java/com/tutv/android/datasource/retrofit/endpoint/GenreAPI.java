package com.tutv.android.datasource.retrofit.endpoint;

import com.tutv.android.domain.Genre;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GenreAPI {
    @GET("genres")
    Single<List<Genre>> getAll();

    @GET("genres/{id}")
    Single<Genre> getById(@Path("id") int genreId, @Query("pagesize") int pagesize, @Query("page") int page);
}
