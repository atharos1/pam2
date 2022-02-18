package com.tutv.android.di

import android.content.Context
import android.content.SharedPreferences
import com.tutv.android.datasource.retrofit.RetrofitInstance
import com.tutv.android.datasource.retrofit.endpoint.GenreAPI
import com.tutv.android.datasource.retrofit.endpoint.NetworksAPI
import com.tutv.android.datasource.retrofit.endpoint.SeriesAPI
import com.tutv.android.datasource.retrofit.endpoint.UserAPI
import com.tutv.android.db.AppDatabase
import com.tutv.android.db.dao.SeriesDao
import com.tutv.android.db.dao.UserDao
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import com.tutv.android.utils.schedulers.SchedulerProvider
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class ContainerModule(context: Context?) {
    private val applicationContext: Context?
    fun getApplicationContext(): Context? {
        return applicationContext
    }

    fun provideUserRepository(userDao: UserDao?, userAPI: UserAPI?, authenticationSharedPreferences: SharedPreferences?): UserRepository? {
        return UserRepository(userDao, userAPI, authenticationSharedPreferences)
    }

    fun provideUserDao(): UserDao? {
        return AppDatabase.Companion.getInstance(applicationContext).userDao()
    }

    fun provideUserAPI(): UserAPI? {
        return RetrofitInstance.getRetrofitClient(applicationContext).create(UserAPI::class.java)
    }

    fun provideGenreAPI(): GenreAPI? {
        return RetrofitInstance.getRetrofitClient(applicationContext).create(GenreAPI::class.java)
    }

    fun provideNetworksAPI(): NetworksAPI? {
        return RetrofitInstance.getRetrofitClient(applicationContext).create(NetworksAPI::class.java)
    }

    fun provideSeriesRepository(seriesDao: SeriesDao?, seriesAPI: SeriesAPI?,
                                genreAPI: GenreAPI?, networksAPI: NetworksAPI?,
                                userRepository: UserRepository?, schedulerProvider: BaseSchedulerProvider?): SeriesRepository? {
        return SeriesRepository(seriesDao, seriesAPI, genreAPI, networksAPI, userRepository, schedulerProvider)
    }

    fun provideSeriesDao(): SeriesDao? {
        return AppDatabase.Companion.getInstance(applicationContext).seriesDao()
    }

    fun provideSeriesAPI(): SeriesAPI? {
        return RetrofitInstance.getRetrofitClient(applicationContext).create(SeriesAPI::class.java)
    }

    fun provideSchedulerProvider(): BaseSchedulerProvider? {
        return SchedulerProvider.Companion.getInstance()
    }

    fun provideSharedPreferences(name: String?, mode: Int): SharedPreferences? {
        return applicationContext.getSharedPreferences(name, mode)
    }

    init {
        applicationContext = context.getApplicationContext()
    }
}