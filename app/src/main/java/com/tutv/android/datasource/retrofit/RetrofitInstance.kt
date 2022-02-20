package com.tutv.android.datasource.retrofit

import android.content.Context
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import com.tutv.android.datasource.retrofit.interceptor.AuthorizationInterceptor
import com.tutv.android.datasource.retrofit.interceptor.LoginInterceptor
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Singleton lazy de la instancia de Retrofit
 * - Dado que para inicializar la instancia es necesario acceso al Context
 *   presentamos un unico metodo publico de acceso y hacemos de retrofit
 *   un miembro privado. Esta es la unica diferencia con respecto a un singleton
 *   comun y silvestre de Kotlin
 */
object RetrofitInstance {
    private const val TAG = "RETROFIT INSTANCE"

    private const val BASE_URL = "https://tutv-pam.herokuapp.com/api/"
    private lateinit var appContext: Context
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getCustomClient(appContext))
            .build()
    }

    //Todo: esta annotation no va mas al momento que DI sea completamente Kotlin!
    @JvmStatic
    fun getRetrofitClient(appContext: Context): Retrofit {
        if (!this::appContext.isInitialized) {
            this.appContext = appContext
        }
        return retrofit
    }

    private fun getCustomClient(appContext: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(appContext))
            .addInterceptor(LoginInterceptor(appContext))
            .addInterceptor(loggingInterceptor)
            .build()
    }
}