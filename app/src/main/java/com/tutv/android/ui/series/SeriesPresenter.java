package com.tutv.android.ui.series;


import android.annotation.SuppressLint;

import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;

import java.lang.ref.WeakReference;

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

    }

    public void onEpisodeClicked(Season s, Episode e) {
        seriesRepository.setEpisodeViewed(series, s, e)
            .observeOn(Schedulers.io())
            .subscribe(series -> {
                SeriesView view = seriesView.get();
                if(view != null) {
                    for(Season season : series.getSeasons()) {
                        if(season.getNumber() == s.getNumber()) {
                            view.bindSeason(season);
                            break;
                        }
                    }
                }
            }, this::onSeriesUpdatedError);
    }

    private void onSeriesUpdatedError(Throwable throwable) {

    }

    public void onSeriesFollowClicked() {
        if(series.getLoggedInUserFollows() != null) {
            if(series.getLoggedInUserFollows()) {
                seriesRepository.setFollowSeries(series)
                        .observeOn(Schedulers.io())
                        .doOnSuccess(this::onSeriesFollowed)
                        .doOnError(this::onSeriesFollowedError);
            } else {
                seriesRepository.unfollowSeries(series)
                        .observeOn(Schedulers.io())
                        .doOnSuccess(this::onSeriesUnfollowed)
                        .doOnError(this::onSeriesUnfollowedError);
            }
        }
    }

    private void onSeriesUnfollowedError(Throwable throwable) {
    }

    private void onSeriesUnfollowed(Series series) {
        SeriesView view = seriesView.get();
        if(view != null) {
            view.showUserFollows(false);
            view.showFollowerCount(series.getFollowers());
        }
    }

    private void onSeriesFollowedError(Throwable throwable) {

    }

    private void onSeriesFollowed(Series series) {
        SeriesView view = seriesView.get();
        if(view != null) {
            view.showFollowerCount(series.getFollowers());
            view.showSeriesFollowed(series.getLoggedInUserFollows());
        }
    }
}
