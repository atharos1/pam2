package com.tutv.android.ui.series;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.SeriesRepository;

import kotlin.NotImplementedError;

public class SeriesActivity extends AppCompatActivity implements SeriesView {

    private SeriesPresenter seriesPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int seriesId = savedInstanceState.getInt("series_id");

        Container container = ContainerLocator.locateComponent(this);
        SeriesRepository seriesRepository = container.getSeriesRepository();
        seriesPresenter = new SeriesPresenter(this, seriesId, seriesRepository);
    }

    @Override
    protected void onStart() {
        super.onStart();

        seriesPresenter.onViewAttached();
    }

    @Override
    protected void onStop() {
        super.onStop();

        seriesPresenter.onViewDetached();
    }

    @Override
    public void showSeriesName(String seriesName) {
        throw new NotImplementedError();
    }

    @Override
    public void showSeriesDescription(String seriesDescription) {
        throw new NotImplementedError();
    }

    @Override
    public void showUserFollows(boolean follows) {
        throw new NotImplementedError();
    }

    @Override
    public void showFollowerCount(int followers) {
        throw new NotImplementedError();
    }

}
