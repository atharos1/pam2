package com.tutv.android.datasource.retrofit.interceptor;

import android.content.Context;
import android.content.Intent;

import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest;
import com.tutv.android.ui.login.LoginActivity;
import com.tutv.android.ui.series.SeriesActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Invocation;

public class LoginInterceptor implements Interceptor {

    private Context appContext;

    public LoginInterceptor(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(chain.request());

        String token = response.header("Authorization");
        if(token != null) {
            appContext.getSharedPreferences("Token", Context.MODE_PRIVATE).edit().putString("Token", token).commit();
        }
        return response;
    }

}
