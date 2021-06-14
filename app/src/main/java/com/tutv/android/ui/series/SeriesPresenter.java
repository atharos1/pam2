package com.tutv.android.ui.series;

import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SeriesPresenter {

    private final WeakReference<SeriesView> seriesView;

    private final SeriesRepository seriesRepository;
    private final int seriesId;

    private final CompositeDisposable disposables;

    public SeriesPresenter(SeriesView seriesView, int seriesId, SeriesRepository seriesRepository) {
        this.seriesView = new WeakReference<>(seriesView);
        this.seriesId = seriesId;
        this.seriesRepository = seriesRepository;

        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
        disposables.add(seriesRepository.getSeriesById(seriesId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSeriesLoad, this::onSeriesLoadError));
    }

    public void onViewDetached() {
        disposables.dispose();
    }

    private void onSeriesLoad(Series series) {
        SeriesView actualView = seriesView.get();
        if(actualView != null) {
            actualView.showSeriesName(series.getName());
            actualView.showSeriesDescription(series.getSeriesDescription());
            actualView.showUserFollows(false);
            actualView.showFollowerCount(series.getFollowers());
            actualView.bindSeasons(series.getSeasons());
        }
    }

    private void onSeriesLoadError(final Throwable e) {

    }

}
