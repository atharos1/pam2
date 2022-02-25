package com.tutv.android.datasource.retrofit.endpoint

import com.tutv.android.datasource.dto.*
import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import com.tutv.android.domain.Series
import com.tutv.android.domain.Episode
import com.tutv.android.domain.Review
import io.reactivex.Single
import retrofit2.http.*

interface SeriesAPI {
    @AuthenticatedRequest
    @GET("series/{id}")
    fun getSeriesById(@Path("id") id: Int): Single<Series>

    @AuthenticatedRequest
    @GET("series")
    fun getSeriesSearch(
        @Query("name") name: String?, @Query("pagesize") pageSize: Int, @Query("page") page: Int,
        @Query("genre") genre: Int?, @Query("network") network: Int?
    ): Single<List<Series>>

    @AuthenticatedRequest
    @PUT("series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}/viewed")
    fun setSeriesViewed(
        @Path("seriesId") seriesId: Int,
        @Path("seasonId") seasonId: Int,
        @Path("episodeId") episodeId: Int,
        @Body resourceViewedDTO: ResourceViewedDTO?
    ): Single<ResourceViewedDTO>

    @AuthenticatedRequest
    @GET("series/{seriesId}/seasons/{seasonNumber}/episodes/{episodeNumber}")
    fun getEpisode(
        @Path("seriesId") seriesId: Int,
        @Path("seasonNumber") seasonNumber: Int,
        @Path("episodeNumber") numEpisode: Int
    ): Single<Episode>

    @AuthenticatedRequest
    @POST("users/{userId}/following")
    fun setFollowSeries(
        @Path("userId") userId: Int,
        @Body seriesFollowedDTO: SeriesFollowedDTO?
    ): Single<SeriesFollowedResponseDTO>

    @AuthenticatedRequest
    @DELETE("users/{userId}/following/{seriesId}")
    fun setUnfollowSeries(
        @Path("userId") userId: Int,
        @Path("seriesId") seriesId: Int
    ): Single<SeriesFollowedResponseDTO>

    @AuthenticatedRequest
    @GET("series/{seriesId}/reviews")
    fun getSeriesReviewsList(
        @Path("seriesId") seriesId: Int
    ): Single<List<Review>>

    @AuthenticatedRequest
    @PUT("series/{seriesId}/reviews/{reviewId}/like")
    fun setReviewLiked(
        @Path("seriesId") seriesId: Int,
        @Path("reviewId") reviewId: Int,
        @Body reviewLikedDTO: ReviewLikedDTO
    ): Single<ReviewLikedDTOResponse>

    @AuthenticatedRequest
    @POST("series/{seriesId}/reviews/")
    fun postReview(
            @Path("seriesId") seriesId: Int,
            @Body reviewDTO: ReviewDTO
    ): Single<Review>

    @get:GET("series/featured")
    @get:AuthenticatedRequest
    val featured: Single<List<Series>>
}