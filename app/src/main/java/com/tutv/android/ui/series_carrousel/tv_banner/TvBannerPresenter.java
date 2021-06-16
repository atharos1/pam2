package com.tutv.android.ui.series_carrousel.tv_banner;

import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series.SeriesView;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

public class TvBannerPresenter {
    private final WeakReference<TvBannerView> view;
    private final SeriesRepository seriesRepository;
    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeDisposable disposables;

    private Series series;

    public TvBannerPresenter(TvBannerView view, SeriesRepository seriesRepository, BaseSchedulerProvider schedulerProvider) {
        this.view = new WeakReference<>(view);
        this.seriesRepository = seriesRepository;
        this.schedulerProvider = schedulerProvider;
        disposables = new CompositeDisposable();
    }

    public void bind(Series series) {
        this.series = series;

        if(view.get() != null) {
            view.get().setImageUrl(series.getBannerUrl());
            view.get().setTitle(series.getName());
            view.get().setSubtitle(series.getFollowers() + " followers");

            view.get().setSeriesFollowed(series.getLoggedInUserFollows() != null && series.getLoggedInUserFollows());
        }
    }

    public void onClick() {
        if(view.get() != null)
            view.get().navigateToSeriesPage(series.getId());
    }

    public void onViewDetached() {
        disposables.dispose();
    }

    public void onSeriesFollowClicked() {
        if(series.getLoggedInUserFollows() == null || !series.getLoggedInUserFollows()) {
            disposables.add(seriesRepository.setFollowSeries(series)
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onSeriesFollowed, this::onSeriesFollowedError));
        } else {
            disposables.add(seriesRepository.unfollowSeries(series)
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::onSeriesFollowed, this::onSeriesFollowedError));
        }
    }

    private void onSeriesFollowedError(Throwable throwable) {
        if(view.get() != null) {
            view.get().showError("Error al dejar seguir la serie :(");
        }
    }

    private void onSeriesFollowed(Series series) {
        if(view.get() != null) {
            view.get().setSubtitle(series.getFollowers() + " followers");
            view.get().setSeriesFollowed(series.getLoggedInUserFollows());
        }
    }
}
