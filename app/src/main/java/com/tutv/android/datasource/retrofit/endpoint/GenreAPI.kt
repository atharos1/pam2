package com.tutv.android.datasource.retrofit.endpoint

import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import com.tutv.android.domain.Genre
import io.reactivex.Single
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GenreAPI {
    @AuthenticatedRequest
    @GET("genres")
    open fun getAll(): Single<MutableList<Genre?>?>?

    @GET("genres/{id}")
    open fun getById(@Path("id") genreId: Int, @Query("pagesize") pagesize: Int, @Query("page") page: Int): Single<Genre?>?
}