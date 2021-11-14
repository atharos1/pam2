package com.tutv.android.datasource.retrofit.endpoint

import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import retrofit2.http.GET
import com.tutv.android.domain.Genre
import com.tutv.android.domain.Series
import retrofit2.http.PUT
import com.tutv.android.datasource.dto.ResourceViewedDTO
import com.tutv.android.domain.Episode
import retrofit2.http.POST
import com.tutv.android.datasource.dto.SeriesFollowedDTO
import com.tutv.android.datasource.dto.SeriesFollowedResponseDTO
import retrofit2.http.DELETE
import com.tutv.android.datasource.retrofit.annotation.AvoidForceShowLoginScreenOnAuthFail
import com.tutv.android.domain.Network
import io.reactivex.Single

interface NetworksAPI {
    @get:GET("networks")
    @get:AuthenticatedRequest
    val all: Single<List<Network?>?>?
}