package com.tutv.android.di

import android.content.Context
import com.tutv.android.datasource.retrofit.RetrofitInstance.getRetrofitClient
import com.tutv.android.db.dao.UserDao
import com.tutv.android.datasource.retrofit.endpoint.UserAPI
import android.content.SharedPreferences
import com.tutv.android.repository.UserRepository
import com.tutv.android.db.AppDatabase
import com.tutv.android.datasource.retrofit.endpoint.GenreAPI
import com.tutv.android.datasource.retrofit.endpoint.NetworksAPI
import com.tutv.android.db.dao.SeriesDao
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.utils.schedulers.SchedulerProvider

class ContainerModule(context: Context) {
    private val applicationContext: Context = context.applicationContext

    fun provideUserRepository(
        userDao: UserDao,
        userAPI: UserAPI,
        authenticationSharedPreferences: SharedPreferences
    ) = UserRepository(userDao, userAPI, authenticationSharedPreferences)

    fun provideUserDao(): UserDao =  AppDatabase.getInstance(applicationContext).userDao()

    fun provideUserAPI(): UserAPI = getRetrofitClient(applicationContext).create(UserAPI::class.java)

    fun provideGenreAPI(): GenreAPI = getRetrofitClient(applicationContext).create(GenreAPI::class.java)

    fun provideNetworksAPI(): NetworksAPI = getRetrofitClient(applicationContext).create(NetworksAPI::class.java)

    fun provideSeriesRepository(
        seriesDao: SeriesDao, seriesAPI: SeriesAPI,
        genreAPI: GenreAPI, networksAPI: NetworksAPI,
        userRepository: UserRepository, schedulerProvider: BaseSchedulerProvider
    ): SeriesRepository =
         SeriesRepository(
            seriesDao,
            seriesAPI,
            genreAPI,
            networksAPI,
            userRepository,
            schedulerProvider)

    fun provideSeriesDao(): SeriesDao {
        return AppDatabase.getInstance(applicationContext).seriesDao()
    }

    fun provideSeriesAPI(): SeriesAPI = getRetrofitClient(applicationContext).create(SeriesAPI::class.java)

    fun provideSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider.getInstance()

    fun provideSharedPreferences(name: String, mode: Int): SharedPreferences = applicationContext.getSharedPreferences(name, mode)
}