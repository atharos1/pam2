package com.tutv.android.di

import android.content.Context
import android.content.SharedPreferences

class AppContainer(context: Context) : Container {
    private val containerModule = ContainerModule(context)

    private var userDao = containerModule.provideUserDao()
    private var seriesDao = containerModule.provideSeriesDao()

    private var userAPI = containerModule.provideUserAPI()
    private var seriesAPI = containerModule.provideSeriesAPI()
    private var genreAPI = containerModule.provideGenreAPI()
    private var networksAPI = containerModule.provideNetworksAPI()

    private val authenticationSharedPreferences: SharedPreferences = containerModule.provideSharedPreferences("Token", Context.MODE_PRIVATE)

    override val schedulerProvider = containerModule.provideSchedulerProvider()
    override val userRepository = containerModule.provideUserRepository(userDao, userAPI, authenticationSharedPreferences)
    override val seriesRepository = containerModule.provideSeriesRepository(seriesDao, seriesAPI, genreAPI, networksAPI, userRepository, schedulerProvider)
}