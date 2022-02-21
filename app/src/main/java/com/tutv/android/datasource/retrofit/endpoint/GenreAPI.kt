package com.tutv.android.datasource.retrofit.endpoint

import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import com.tutv.android.domain.Genre
import io.reactivex.Single
import retrofit2.http.*

interface GenreAPI {
    @get:GET("genres")
    @get:AuthenticatedRequest
    val all: Single<List<Genre>>

    @GET("genres/{id}")
    fun getById(
        @Path("id") genreId: Int,
        @Query("pagesize") pagesize: Int,
        @Query("page") page: Int
    ): Single<Genre>
}