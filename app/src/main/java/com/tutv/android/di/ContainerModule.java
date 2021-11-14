package com.tutv.android.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.tutv.android.datasource.retrofit.RetrofitInstance;
import com.tutv.android.datasource.retrofit.endpoint.GenreAPI;
import com.tutv.android.datasource.retrofit.endpoint.NetworksAPI;
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI;
import com.tutv.android.datasource.retrofit.endpoint.UserAPI;
import com.tutv.android.db.AppDatabase;
import com.tutv.android.db.dao.SeriesDao;
import com.tutv.android.db.dao.UserDao;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;
import com.tutv.android.utils.schedulers.SchedulerProvider;

public class ContainerModule {
    private final Context applicationContext;

    public ContainerModule(Context context) {
        this.applicationContext = context.getApplicationContext();
        RetrofitInstance.INSTANCE.setAppContext(context);
    }

    Context getApplicationContext() {
        return applicationContext;
    }

    public UserRepository provideUserRepository(final UserDao userDao, UserAPI userAPI, SharedPreferences authenticationSharedPreferences) {
        return new UserRepository(userDao, userAPI, authenticationSharedPreferences);
    }

    public UserDao provideUserDao() {
        return AppDatabase.getInstance(applicationContext).userDao();
    }

    public UserAPI provideUserAPI() {
        return RetrofitInstance.getRetrofitClient(applicationContext).create(UserAPI.class);
    }

    public GenreAPI provideGenreAPI() {
        return RetrofitInstance.getRetrofitClient(applicationContext).create(GenreAPI.class);
    }

    public NetworksAPI provideNetworksAPI() {
        return RetrofitInstance.getRetrofitClient(applicationContext).create(NetworksAPI.class);
    }

    public SeriesRepository provideSeriesRepository(final SeriesDao seriesDao, final SeriesAPI seriesAPI,
                                                    final GenreAPI genreAPI, final NetworksAPI networksAPI,
                                                    final UserRepository userRepository, final BaseSchedulerProvider schedulerProvider) {
        return new SeriesRepository(seriesDao, seriesAPI, genreAPI, networksAPI, userRepository, schedulerProvider);
    }

    public SeriesDao provideSeriesDao() {
        return AppDatabase.getInstance(applicationContext).seriesDao();
    }

    public SeriesAPI provideSeriesAPI() {
        return RetrofitInstance.getRetrofitClient(applicationContext).create(SeriesAPI.class);
    }

    public BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    public SharedPreferences provideSharedPreferences(String name, int mode) {
        return applicationContext.getSharedPreferences(name, mode);
    }
}
