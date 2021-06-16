package com.tutv.android.datasource.retrofit.interceptor;

import android.content.Context;

import com.tutv.android.datasource.retrofit.annotation.AuthenticatedRequest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Invocation;

public class AuthorizationInterceptor implements Interceptor {

    private Context appContext;

    public AuthorizationInterceptor(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        /*if(!SessionManager.isAuthenticated())
            return chain.proceed(chain.request());*/

        Request request = chain.request();

        Invocation invocation = request.tag(Invocation.class);
        AuthenticatedRequest authenticatedRequestAnnotation = invocation.method().getAnnotation(AuthenticatedRequest.class);

        if(authenticatedRequestAnnotation == null)
            return chain.proceed(chain.request());

        Request.Builder requestBuilder = request.newBuilder();

        String sessionToken = appContext.getSharedPreferences("Token", Context.MODE_PRIVATE).getString("Token", "");
        if(sessionToken != "")
            requestBuilder.addHeader("Authorization", sessionToken);

        return chain.proceed(requestBuilder.build());
    }

}
