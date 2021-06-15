package com.tutv.android.datasource.retrofit.endpoint;

import com.tutv.android.domain.Network;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface NetworksAPI {
    @GET("networks")
    Single<List<Network>> getAll();
}
