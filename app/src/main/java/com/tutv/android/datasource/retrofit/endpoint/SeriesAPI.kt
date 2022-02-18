package com.tutv.android.datasource.retrofit.endpoint

import com.tutv.android.datasource.dto.ResourceViewedDTO
import com.tutv.android.datasource.dto.SeriesFollowedDTO
import com.tutv.android.datasource.dto.SeriesFollowedResponseDTO
import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import com.tutv.android.domain.Episode
import com.tutv.android.domain.Series
import io.reactivex.Single
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import retrofit2.http.*

interface SeriesAPI {
    @AuthenticatedRequest
    @GET("series/{id}")
    open fun getSeriesById(@Path("id") id: Int): Single<Series?>?

    @AuthenticatedRequest
    @GET("series")
    open fun getSeriesSearch(@Query("name") name: String?, @Query("pagesize") pageSize: Int, @Query("page") page: Int,
                             @Query("genre") genre: Int?, @Query("network") network: Int?): Single<MutableList<Series?>?>?

    @AuthenticatedRequest
    @PUT("series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}/viewed")
    open fun setSeriesViewed(@Path("seriesId") seriesId: Int, @Path("seasonId") seasonId: Int, @Path("episodeId") episodeId: Int, @Body resourceViewedDTO: ResourceViewedDTO?): Single<ResourceViewedDTO?>?

    @AuthenticatedRequest
    @GET("series/{seriesId}/seasons/{seasonNumber}/episodes/{episodeNumber}")
    open fun getEpisode(@Path("seriesId") seriesId: Int, @Path("seasonNumber") seasonNumber: Int, @Path("episodeNumber") numEpisode: Int): Single<Episode?>?

    @AuthenticatedRequest
    @POST("users/{userId}/following")
    open fun setFollowSeries(@Path("userId") userId: Int, @Body seriesFollowedDTO: SeriesFollowedDTO?): Single<SeriesFollowedResponseDTO?>?

    @AuthenticatedRequest
    @DELETE("users/{userId}/following/{seriesId}")
    open fun setUnfollowSeries(@Path("userId") userId: Int, @Path("seriesId") seriesId: Int): Single<SeriesFollowedResponseDTO?>?

    @AuthenticatedRequest
    @GET("series/featured")
    open fun getFeatured(): Single<MutableList<Series?>?>?
}