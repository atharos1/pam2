package com.tutv.android.ui.series;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SeriesActivity extends AppCompatActivity implements SeriesView {

    private SeriesPresenter seriesPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        seriesPresenter = new SeriesPresenter(this);
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
}
