package com.tutv.android.datasource.retrofit.endpoint

import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import com.tutv.android.datasource.retrofit.annotation.AvoidForceShowLoginScreenOnAuthFail
import com.tutv.android.domain.User
import io.reactivex.Single
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import retrofit2.http.GET
import retrofit2.http.Header

interface UserAPI {
    @AvoidForceShowLoginScreenOnAuthFail
    @GET("user")
    open fun getCurrentUser(@Header("Authorization") authHeader: String?): Single<User?>?

    @AuthenticatedRequest
    @GET("user")
    open fun getCurrentUser(): Single<User?>?

    @AvoidForceShowLoginScreenOnAuthFail
    @AuthenticatedRequest
    @GET("user")
    open fun getCurrentUserNoForceLoginScreenOnFail(): Single<User?>?
}