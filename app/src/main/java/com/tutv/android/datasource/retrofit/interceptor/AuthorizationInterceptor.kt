package com.tutv.android.datasource.retrofit.interceptor

import android.content.Context
import android.content.Intent
import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest
import com.tutv.android.datasource.retrofit.annotation.AvoidForceShowLoginScreenOnAuthFail
import com.tutv.android.ui.login.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import retrofit2.Invocation
import java.io.IOException

class AuthorizationInterceptor(private val appContext: Context?) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
        val authenticatedRequestAnnotation = invocation.method().getAnnotation(AuthenticatedRequest::class.java)
                ?: return chain.proceed(chain.request())
        val requestBuilder = request.newBuilder()
        val sessionToken = appContext.getSharedPreferences("Token", Context.MODE_PRIVATE).getString("Token", "")
        if (sessionToken !== "") requestBuilder.addHeader("Authorization", sessionToken)
        val response = chain.proceed(requestBuilder.build())
        val avoidForceShowLoginScreenOnAuthFailAnnotation = invocation.method().getAnnotation(AvoidForceShowLoginScreenOnAuthFail::class.java)

        //User auth failed, redirect to login
        if (avoidForceShowLoginScreenOnAuthFailAnnotation == null && response.code() == 401) {
            val intent = Intent(appContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(intent)
        }
        return response
    }

}