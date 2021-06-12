package com.tutv.android.datasource.retrofit.endpoint;

import com.tutv.android.domain.User;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserAPI {
    @GET("user")
    Single<User> getCurrentUser(@Header("Authorization") String authHeader);

    @GET("user")
    Single<User> getCurrentUser();
}
