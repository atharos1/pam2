package com.tutv.android.datasource.retrofit.endpoint;

import com.tutv.android.datasource.dto.ResourceFollowedDTO;
import com.tutv.android.datasource.dto.ResourceViewedDTO;
import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Series;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SeriesAPI {

    @GET("series/{id}")
    Single<Series> getSeriesById(@Path("id") int id);

    @PUT("series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}")
    Single<ResourceViewedDTO> setSeriesViewed(@Path("seriesId") int seriesId, @Path("seasonId") int seasonId, @Path("episodeId") int episodeId, @Body ResourceViewedDTO resourceViewedDTO);

    @GET("series/{seriesId}/seasons/{seasonNumber}/episodes/{episodeNumber}")
    Single<Episode> getEpisode(@Path("seriesId") int seriesId, @Path("seasonNumber") int seasonNumber, @Path("episodeNumber") int numEpisode);

    @PUT("series/{seriesId}")
    Single<ResourceFollowedDTO> setFollowSeries(@Path("seriesId") int id, @Body ResourceFollowedDTO resourceFollowedDTO);
}
