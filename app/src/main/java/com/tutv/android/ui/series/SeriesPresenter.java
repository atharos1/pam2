package com.tutv.android.ui.series;

import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class SeriesPresenter {

    private final WeakReference<SeriesView> seriesView;

    private final SeriesRepository seriesRepository;
    private final BaseSchedulerProvider schedulerProvider;
    private final int seriesId;
    //Todo: el tratamiento del objeto series deberia ser a traves de un Optional, pero no estamos en ese api level...
    private Series series;


    private final CompositeDisposable disposables;

    public SeriesPresenter(SeriesView seriesView, int seriesId, SeriesRepository seriesRepository, BaseSchedulerProvider schedulerProvider) {
        this.seriesView = new WeakReference<>(seriesView);
        this.seriesId = seriesId;
        this.seriesRepository = seriesRepository;
        this.schedulerProvider = schedulerProvider;

        this.series = null;

        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
        disposables.add(seriesRepository.getSeriesById(seriesId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSeriesLoad, this::onSeriesLoadError));
    }

    public void onViewDetached() {
        disposables.dispose();
    }

    private void onSeriesLoad(Series series) {
        this.series = series;
        SeriesView actualView = seriesView.get();
        if(actualView != null) {
            actualView.showSeriesName(series.getName());
            actualView.showSeriesDescription(series.getSeriesDescription());
            actualView.showSeriesFollowed(series.getLoggedInUserFollows() == null ? false : series.getLoggedInUserFollows());
            actualView.showFollowerCount(series.getFollowers());
            actualView.bindSeasons(series.getSeasons());
            actualView.showSeriesBanner(series.getBannerUrl());
        }
    }

    private void onSeriesLoadError(final Throwable e) {
        SeriesView view = seriesView.get();
        if(view != null) {
            view.showError("Error al cargar la serie, asegurese de tener conexion");
        }
    }

    public void onEpisodeClicked(Season s, Episode e) {
        disposables.add(seriesRepository.setEpisodeViewed(series, s, e)
                .observeOn(schedulerProvider.computation())
                .flatMap(series -> {
                    for(Season season : series.getSeasons()) {
                        if(season.getNumber() == s.getNumber()) {
                            return Single.just(s);
                        }
                    }
                    return Single.error(new Throwable());
                })
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onEpisodeViewed, this::onEpisodeViewedError));
    }

    private void onEpisodeViewedError(Throwable throwable) {
        System.err.println(throwable);
        SeriesView view = seriesView.get();
        if (view != null) {
            view.showError("Error al ver el episodio, asegurese de tener conexion y estar logueado");
        }
    }

    private void onEpisodeViewed(Season season) {
        SeriesView view = seriesView.get();
        if(view != null) {
            view.bindSeason(season);
        }
    }

    public void onSeriesFollowClicked() {
        if(series == null) return;
        if(series.getLoggedInUserFollows() == null || !series.getLoggedInUserFollows()) {
            disposables.add(seriesRepository.setFollowSeries(series)
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onSeriesFollowed, this::onSeriesFollowedError));
        } else {
            disposables.add(seriesRepository.unfollowSeries(series)
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onSeriesUnfollowed, this::onSeriesUnfollowedError));
        }
    }

    private void onSeriesUnfollowedError(Throwable throwable) {
        SeriesView view = seriesView.get();
        if(view != null) {
            view.showError("Error al dejar de seguir la serie, asegurese de tener conexion y estar logueado");
        }
    }

    private void onSeriesUnfollowed(Series series) {
        if(series == null) return;
        SeriesView view = seriesView.get();
        if(view != null) {
            view.showSeriesFollowed(false);
            view.showFollowerCount(series.getFollowers());
        }
    }

    private void onSeriesFollowedError(Throwable throwable) {
        SeriesView view = seriesView.get();
        if(view != null) {
            view.showError("Error al seguir la serie, asegurese de tener conexion y estar logueado");
        }
    }

    private void onSeriesFollowed(Series series) {
        if(series == null) return;
        SeriesView view = seriesView.get();
        if(view != null) {
            view.showFollowerCount(series.getFollowers());
            view.showSeriesFollowed(series.getLoggedInUserFollows());
        }
    }
}
