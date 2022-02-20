package com.tutv.android.datasource.retrofit.interceptor

import android.content.Context
import okhttp3.Interceptor
import kotlin.Throws
import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import com.tutv.android.datasource.retrofit.annotation.AvoidForceShowLoginScreenOnAuthFail
import android.content.Intent
import com.tutv.android.ui.login.LoginActivity
import okhttp3.Response
import java.io.IOException

class LoginInterceptor(private val appContext: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(chain.request())
        val token = response.header("Authorization")
        if (token != null) {
            appContext.getSharedPreferences("Token", Context.MODE_PRIVATE).edit()
                .putString("Token", token).commit()
        }
        return response
    }
}