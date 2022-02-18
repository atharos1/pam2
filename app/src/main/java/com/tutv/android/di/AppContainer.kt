package com.tutv.android.di

import android.content.Context
import android.content.SharedPreferences
import com.tutv.android.datasource.retrofit.endpoint.GenreAPI
import com.tutv.android.datasource.retrofit.endpoint.NetworksAPI
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI
import com.tutv.android.datasource.retrofit.endpoint.UserAPI
import com.tutv.android.db.dao.SeriesDao
import com.tutv.android.db.dao.UserDao
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class AppContainer(context: Context?) : Container {
    private val containerModule: ContainerModule?
    private var userRepository: UserRepository? = null
    private var userDao: UserDao? = null
    private var userAPI: UserAPI? = null
    private var seriesRepository: SeriesRepository? = null
    private var seriesDao: SeriesDao? = null
    private var seriesAPI: SeriesAPI? = null
    private var genreAPI: GenreAPI? = null
    private var networksAPI: NetworksAPI? = null
    private var schedulerProvider: BaseSchedulerProvider? = null
    override fun getUserRepository(): UserRepository? {
        if (userRepository == null) userRepository = containerModule.provideUserRepository(getUserDao(), getUserAPI(), getAuthenticationSharedPreferences())
        return userRepository
    }

    private fun getUserAPI(): UserAPI? {
        if (userAPI == null) userAPI = containerModule.provideUserAPI()
        return userAPI
    }

    private fun getUserDao(): UserDao? {
        if (userDao == null) userDao = containerModule.provideUserDao()
        return userDao
    }

    override fun getSeriesRepository(): SeriesRepository? {
        if (seriesRepository == null) seriesRepository = containerModule.provideSeriesRepository(
                getSeriesDao(), getSeriesAPI(), getGenreAPI(),
                getNetworksAPI(), getUserRepository(), getSchedulerProvider()
        )
        return seriesRepository
    }

    private fun getSeriesDao(): SeriesDao? {
        if (seriesDao == null) seriesDao = containerModule.provideSeriesDao()
        return seriesDao
    }

    private fun getSeriesAPI(): SeriesAPI? {
        if (seriesAPI == null) seriesAPI = containerModule.provideSeriesAPI()
        return seriesAPI
    }

    private fun getGenreAPI(): GenreAPI? {
        if (genreAPI == null) genreAPI = containerModule.provideGenreAPI()
        return genreAPI
    }

    private fun getNetworksAPI(): NetworksAPI? {
        if (networksAPI == null) networksAPI = containerModule.provideNetworksAPI()
        return networksAPI
    }

    override fun getSchedulerProvider(): BaseSchedulerProvider? {
        if (schedulerProvider == null) schedulerProvider = containerModule.provideSchedulerProvider()
        return schedulerProvider
    }

    private fun getAuthenticationSharedPreferences(): SharedPreferences? {
        return containerModule.provideSharedPreferences("Token", Context.MODE_PRIVATE)
    }

    init {
        containerModule = ContainerModule(context)
    }
}