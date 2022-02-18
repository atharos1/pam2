package com.tutv.android.datasource.retrofit.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.io.IOException

class LoginInterceptor(private val appContext: Context?) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain.request()
        val response = chain.proceed(chain.request())
        val token = response.header("Authorization")
        if (token != null) {
            appContext.getSharedPreferences("Token", Context.MODE_PRIVATE).edit().putString("Token", token).commit()
        }
        return response
    }

}