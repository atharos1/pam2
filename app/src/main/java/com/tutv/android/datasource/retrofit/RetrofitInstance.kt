package com.tutv.android.datasource.retrofit

import android.content.Context
import com.tutv.android.datasource.retrofit.interceptor.AuthorizationInterceptor
import com.tutv.android.datasource.retrofit.interceptor.LoginInterceptor
import okhttp3.OkHttpClient
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    var BASE_URL: String? = "https://tutv-pam.herokuapp.com/api/"
    private var retrofit: Retrofit? = null
    fun getRetrofitClient(appContext: Context?): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getCustomClient(appContext))
                    .build()
        }
        return retrofit
    }

    private fun getCustomClient(appContext: Context?): OkHttpClient? {
        return OkHttpClient.Builder()
                .addInterceptor(AuthorizationInterceptor(appContext))
                .addInterceptor(LoginInterceptor(appContext))
                .build()
    }
}