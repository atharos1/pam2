package com.tutv.android.ui.series_carrousel;

import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series_carrousel.tv_banner.TvBannerView;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class SeriesCarrouselPresenter {
    private final WeakReference<SeriesCarrouselView> view;
    private final SeriesRepository seriesRepository;
    private final BaseSchedulerProvider schedulerProvider;
    private List<Series> seriesList;

    private final CompositeDisposable disposables;

    public SeriesCarrouselPresenter(SeriesCarrouselView view, SeriesRepository seriesRepository, BaseSchedulerProvider schedulerProvider) {
        this.view = new WeakReference<>(view);
        this.seriesRepository = seriesRepository;
        this.schedulerProvider = schedulerProvider;
        this.seriesList = new ArrayList<>();

        disposables = new CompositeDisposable();

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
                    .subscribeOn(schedulerProvider.computation())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(this::seriesLoadSuccessful, this::seriesLoadError);

        disposables.add(disposable);
    }

    public void onViewDetached() {
        disposables.dispose();
    }
}
