package com.tutv.android.datasource.retrofit;

import com.tutv.android.datasource.retrofit.interceptor.AuthorizationInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static String BASE_URL = "https://tutv-pam.herokuapp.com/api/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getCustomClient())
                    .build();
        }

        return retrofit;
    }

    private static OkHttpClient getCustomClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthorizationInterceptor())
                .build();
    }
}
