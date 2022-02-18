package com.tutv.android.datasource.retrofit.endpoint

import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import com.tutv.android.domain.Network
import io.reactivex.Single
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import retrofit2.http.GET

interface NetworksAPI {
    @AuthenticatedRequest
    @GET("networks")
    open fun getAll(): Single<MutableList<Network?>?>?
}