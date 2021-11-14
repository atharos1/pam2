package com.tutv.android.datasource.retrofit

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import com.tutv.android.datasource.retrofit.interceptor.AuthorizationInterceptor
import com.tutv.android.datasource.retrofit.interceptor.LoginInterceptor
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {
    private const val BASE_URL = "https://tutv-pam.herokuapp.com/api/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getCustomClient(appContext!!))
            .build()
    }
    var appContext: Context? = null

    //Todo: este metodo no va mas al momento que el DI pase a ser completamente Kotlin
    @JvmStatic
    fun getRetrofitClient(appContext: Context): Retrofit? {
        return retrofit
    }

    private fun getCustomClient(appContext: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(appContext))
            .addInterceptor(LoginInterceptor(appContext))
            .addInterceptor(loggingInterceptor)
            .build()
    }
}