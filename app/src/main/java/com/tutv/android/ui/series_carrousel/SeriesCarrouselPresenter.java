package com.tutv.android.ui.series_carrousel;

import androidx.collection.ArraySet;

import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series_carrousel.tv_banner.TvBannerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SeriesCarrouselPresenter {
    private final WeakReference<SeriesCarrouselView> view;
    private final SeriesRepository seriesRepository;
    private List<Series> seriesList;
    private final CompositeDisposable disposables;

    public SeriesCarrouselPresenter(SeriesCarrouselView view, SeriesRepository seriesRepository) {
        this.view = new WeakReference<>(view);
        this.seriesRepository = seriesRepository;

        disposables = new CompositeDisposable();

        seriesList = new ArrayList<>();
    }

    private void seriesLoadSuccessful(List<Series> series) {
        this.seriesList = series;
        if(view.get() != null)
            view.get().setLoadingStatus(false);
    }

    private void seriesLoadError(Throwable e) {
    }

    public int getItemCount() {
        return seriesList.size();
    }

    public void onBindRepositoryRowViewAtPosition(int position, TvBannerView tvBannerView) {
        Series series = seriesList.get(position);

        tvBannerView.getPresenter().bind(series);
    }

    public void onViewAttached() {
        view.get().setLoadingStatus(true);

        Disposable disposable =
            seriesRepository.getFeatured()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::seriesLoadSuccessful, this::seriesLoadError);

        disposables.add(disposable);
    }

    public void onViewDetached() {
        disposables.dispose();
    }
}
