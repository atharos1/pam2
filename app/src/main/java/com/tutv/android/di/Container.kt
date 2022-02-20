package com.tutv.android.di

import com.tutv.android.repository.SeriesRepository
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider

interface Container {
    val userRepository: UserRepository
    val seriesRepository: SeriesRepository
    val schedulerProvider: BaseSchedulerProvider
}