package com.tutv.android.datasource.retrofit.endpoint

import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import com.tutv.android.domain.Genre
import com.tutv.android.domain.Series
import com.tutv.android.datasource.dto.ResourceViewedDTO
import com.tutv.android.domain.Episode
import com.tutv.android.datasource.dto.SeriesFollowedDTO
import com.tutv.android.datasource.dto.SeriesFollowedResponseDTO
import com.tutv.android.datasource.retrofit.annotation.AvoidForceShowLoginScreenOnAuthFail
import com.tutv.android.domain.User
import io.reactivex.Single
import retrofit2.http.*

interface UserAPI {
    @AvoidForceShowLoginScreenOnAuthFail
    @GET("user")
    fun getCurrentUser(@Header("Authorization") authHeader: String?): Single<User>

    @get:GET("user")
    @get:AuthenticatedRequest
    val currentUser: Single<User>

    @get:GET("user")
    @get:AuthenticatedRequest
    @get:AvoidForceShowLoginScreenOnAuthFail
    val currentUserNoForceLoginScreenOnFail: Single<User>
}