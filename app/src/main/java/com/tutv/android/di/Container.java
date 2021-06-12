package com.tutv.android.di;

import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;

public interface Container {
    UserRepository getUserRepository();
    SeriesRepository getSeriesRepository();
}
