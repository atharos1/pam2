package com.tutv.android.ui.series;

import android.widget.Toast;

import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;

import java.lang.ref.WeakReference;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SeriesPresenter {

    private WeakReference<SeriesView> seriesView;

    private SeriesRepository seriesRepository;
    private Series series;
    private int seriesId;

    public SeriesPresenter(SeriesView seriesView, int seriesId, SeriesRepository seriesRepository) {
        this.seriesView = new WeakReference<>(seriesView);
        this.seriesId = seriesId;
        this.seriesRepository = seriesRepository;
    }

    public void onViewAttached() {
        seriesRepository.getSeriesById(seriesId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSeriesLoad, this::onSeriesLoadError);
    }

    public void onViewDetached() {}

    private void onSeriesLoad(Series series) {
        this.series = series;
        SeriesView actualView = seriesView.get();
        if(actualView != null) {
            actualView.showSeriesName(series.getName());
            actualView.showSeriesDescription(series.getSeriesDescription());
            actualView.showUserFollows(false);
            actualView.showFollowerCount(series.getFollowers());
            actualView.bindSeasons(series.getSeasons());
            actualView.showSeriesBanner(series.getBannerUrl());
        }
    }

    private void onSeriesLoadError(final Throwable e) {
        //Todo: properly handle an error
        System.out.println("AAAaaaaah");
    }

    public void onEpisodeClicked(Season s, Episode e) {
        seriesRepository.setEpisodeViewed(series, s, e)
            .observeOn(Schedulers.io())
            .subscribe(series -> {
                SeriesView view = seriesView.get();
                if(view != null) {
                    view.bindSeasons(series.getSeasons());
                }
            }, this::onSeriesUpdatedError);
    }

    private void onSeriesUpdated(Series series) {

    }

    private void onSeriesUpdatedError(Throwable throwable) {

    }
}
