package com.tutv.android.ui.series;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Season;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;

import java.util.List;

public class SeriesActivity extends AppCompatActivity implements SeriesView {

    private SeriesPresenter seriesPresenter;

    private TextView seriesNameTextView, seriesDescriptionTextView, seriesFollowerCountTextView;
    private ImageView seriesBannerImageView;
    private RecyclerView seasonRecyclerView;
    private CollapsingToolbarLayout seriesCollapsingToolbarLayout;

    private SeasonListAdapter seasonListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int seriesId = getIntent().getExtras().getInt("series_id");

        setContentView(R.layout.activity_series);

        this.seriesDescriptionTextView = (TextView) findViewById(R.id.series_description);
        this.seriesFollowerCountTextView = (TextView) findViewById(R.id.series_follower_count);
        this.seriesCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.series_collapsing_toolbar);
        this.seriesBannerImageView = (ImageView) findViewById(R.id.series_banner_imageview);
        this.seriesNameTextView = (TextView) findViewById(R.id.series_name_textview);

        this.seasonRecyclerView = (RecyclerView) findViewById(R.id.series_season_recyclerview);
        this.seasonListAdapter = new SeasonListAdapter((Season s, Episode e) -> seriesPresenter.onEpisodeClicked(s, e));
        seasonRecyclerView.setAdapter(seasonListAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Container container = ContainerLocator.locateComponent(this);
        SeriesRepository seriesRepository = container.getSeriesRepository();
        UserRepository userRepository = container.getUserRepository();
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
        seriesNameTextView.setText(seriesName);
    }

    @Override
    public void showSeriesDescription(String seriesDescription) {
        this.seriesDescriptionTextView.setText(seriesDescription);
    }

    @Override
    public void showUserFollows(boolean follows) {
        //throw new NotImplementedError();
    }

    @Override
    public void showFollowerCount(int followers) {
        this.seriesFollowerCountTextView.setText("Followers: " + followers);
    }

    @Override
    public void bindSeasons(final List<Season> seasonList) {
        seasonListAdapter.update(seasonList);
    }

    @Override
    public void showSeriesBanner(String bannerUrl) {
        Picasso.get().load("https://image.tmdb.org/t/p/original" + bannerUrl).fit()
                //.placeholder(R.drawable.user_placeholder)
                .into(seriesBannerImageView);
    }

}
