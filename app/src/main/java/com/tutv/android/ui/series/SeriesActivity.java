package com.tutv.android.ui.series;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.SeriesRepository;

public class SeriesActivity extends AppCompatActivity implements SeriesView {

    private SeriesPresenter seriesPresenter;

    private TextView seriesDescriptionTextView, seriesFollowerCountTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int seriesId = getIntent().getExtras().getInt("series_id");

        setContentView(R.layout.activity_series);

        this.seriesDescriptionTextView = (TextView) findViewById(R.id.series_description);
        this.seriesFollowerCountTextView = (TextView) findViewById(R.id.series_follower_count);

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
        //throw new NotImplementedError();
    }

    @Override
    public void showSeriesDescription(String seriesDescription) {
        //throw new NotImplementedError();
        this.seriesDescriptionTextView.setText(seriesDescription);
    }

    @Override
    public void showUserFollows(boolean follows) {
        //throw new NotImplementedError();
    }

    @Override
    public void showFollowerCount(int followers) {
        //throw new NotImplementedError();
        this.seriesFollowerCountTextView.setText("Followers: " + followers);
    }

}
