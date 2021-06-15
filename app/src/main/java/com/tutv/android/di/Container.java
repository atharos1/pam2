package com.tutv.android.di;

import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

public interface Container {
    UserRepository getUserRepository();
    SeriesRepository getSeriesRepository();
    BaseSchedulerProvider getSchedulerProvider();
}
