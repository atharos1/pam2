package com.tutv.android.di;

import android.content.Context;

import com.tutv.android.datasource.retrofit.endpoint.GenreAPI;
import com.tutv.android.datasource.retrofit.endpoint.NetworksAPI;
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI;
import com.tutv.android.datasource.retrofit.endpoint.UserAPI;
import com.tutv.android.db.dao.SeriesDao;
import com.tutv.android.db.dao.UserDao;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.ui.login.LoginPresenter;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

public class AppContainer implements Container {
    private final ContainerModule containerModule;

    private UserRepository userRepository;
    private UserDao userDao;
    private UserAPI userAPI;

    private SeriesRepository seriesRepository;
    private SeriesDao seriesDao;
    private SeriesAPI seriesAPI;
    private GenreAPI genreAPI;
    private NetworksAPI networksAPI;

    private BaseSchedulerProvider schedulerProvider;

    public AppContainer(final Context context) {
        this.containerModule = new ContainerModule(context);
    }

    @Override
    public UserRepository getUserRepository() {
        if(userRepository == null)
            userRepository = containerModule.provideUserRepository(getUserDao(), getUserAPI());

        return userRepository;
    }

    private UserAPI getUserAPI() {
        if(userAPI == null)
            userAPI = containerModule.provideUserAPI();

        return userAPI;
    }

    private UserDao getUserDao() {
        if(userDao == null)
            userDao = containerModule.provideUserDao();

        return userDao;
    }

    @Override
    public SeriesRepository getSeriesRepository() {
        if(seriesRepository == null)
            seriesRepository = containerModule.provideSeriesRepository(
                    getSeriesDao(), getSeriesAPI(), getGenreAPI(),
                    getNetworksAPI(), getUserRepository(), getSchedulerProvider()
            );

        return seriesRepository;
    }

    private SeriesDao getSeriesDao() {
        if(seriesDao == null)
            seriesDao = containerModule.provideSeriesDao();

        return seriesDao;
    }

    private SeriesAPI getSeriesAPI() {
        if(seriesAPI == null)
            seriesAPI = containerModule.provideSeriesAPI();

        return seriesAPI;
    }

    private GenreAPI getGenreAPI() {
        if(genreAPI == null)
            genreAPI = containerModule.provideGenreAPI();

        return genreAPI;
    }

    private NetworksAPI getNetworksAPI() {
        if(networksAPI == null)
            networksAPI = containerModule.provideNetworksAPI();

        return networksAPI;
    }

    @Override
    public BaseSchedulerProvider getSchedulerProvider() {
        if (schedulerProvider == null)
            schedulerProvider = containerModule.provideSchedulerProvider();

        return schedulerProvider;
    }
}
