package com.tutv.android.datasource.retrofit.endpoint;

import com.tutv.android.datasource.dto.SeriesFollowedDTO;
import com.tutv.android.datasource.dto.ResourceViewedDTO;
import com.tutv.android.datasource.dto.SeriesFollowedResponseDTO;
import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Series;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SeriesAPI {

    @GET("series/{id}")
    Single<Series> getSeriesById(@Path("id") int id);

    @PUT("series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}")
    Single<ResourceViewedDTO> setSeriesViewed(@Path("seriesId") int seriesId, @Path("seasonId") int seasonId, @Path("episodeId") int episodeId, @Body ResourceViewedDTO resourceViewedDTO);

    @GET("series/{seriesId}/seasons/{seasonNumber}/episodes/{episodeNumber}")
    Single<Episode> getEpisode(@Path("seriesId") int seriesId, @Path("seasonNumber") int seasonNumber, @Path("episodeNumber") int numEpisode);

    @POST("/users/{userId}/following")
    Single<SeriesFollowedResponseDTO> setFollowSeries(@Path("userId") int userId, @Body SeriesFollowedDTO seriesFollowedDTO);

    @DELETE("/users/{userId}/following/{seriesId}")
    Single<SeriesFollowedResponseDTO> setUnfollowSeries(@Path("userId") int userId, @Path("seriesId") int seriesId);

}
