package com.tutv.android.di

import com.tutv.android.repository.SeriesRepository
import com.tutv.android.repository.UserRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface Container {
    open fun getUserRepository(): UserRepository?
    open fun getSeriesRepository(): SeriesRepository?
    open fun getSchedulerProvider(): BaseSchedulerProvider?
}