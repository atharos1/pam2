package com.tutv.android.datasource.retrofit.interceptor;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class LoginInterceptor implements Interceptor {

    private Context appContext;

    public LoginInterceptor(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        String token = response.header("Authorization");
        if(token != null) {
            appContext.getSharedPreferences("Token", Context.MODE_PRIVATE).edit().putString("Token", token).commit();
        }
        return response;
    }

}
