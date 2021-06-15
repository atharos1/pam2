package com.tutv.android.ui.home;

import com.tutv.android.domain.Genre;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class HomePresenter {

    private final WeakReference<HomeView> view;
    private final SeriesRepository seriesRepository;
    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeDisposable disposables;

    public HomePresenter(HomeView view, SeriesRepository seriesRepository, BaseSchedulerProvider schedulerProvider) {
        this.view = new WeakReference<>(view);
        this.seriesRepository = seriesRepository;
        this.schedulerProvider = schedulerProvider;

        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
        disposables.add(seriesRepository.getGenres()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::genresLoadSuccessful, this::genresLoadError)
        );
    }

    public void onViewDetached() {
        disposables.dispose();
    }

    private void genresLoadSuccessful(List<Genre> genres) {
        for(Genre genre : genres)
            if(view.get() != null)
                view.get().createGenreList(genre.getId(), genre.getName());
    }

    private void genresLoadError(Throwable e) {
        HomeView actualView = view.get();
        if (actualView != null)
            actualView.showError();
    }
}
