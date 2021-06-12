package com.tutv.android.datasource.retrofit.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor {
    private String sessionToken = null;

    @Override
    public Response intercept(Chain chain) throws IOException {
        /*if(!SessionManager.isAuthenticated())
            return chain.proceed(chain.request());*/

        Request request = chain.request();

        Request.Builder requestBuilder = request.newBuilder();

        //TODO agregamos el auth header a todas las request o creamos un annotation para indicar si lo requiere?
        //requestBuilder.addHeader("Authorization", SessionManager.getUserToken());

        return chain.proceed(requestBuilder.build());
    }
}
