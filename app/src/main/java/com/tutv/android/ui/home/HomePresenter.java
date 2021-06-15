package com.tutv.android.ui.home;

import com.tutv.android.domain.Genre;
import com.tutv.android.repository.SeriesRepository;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter {

    private final WeakReference<HomeView> view;
    private final SeriesRepository seriesRepository;

    private final CompositeDisposable disposables;

    public HomePresenter(HomeView view, SeriesRepository seriesRepository) {
        this.view = new WeakReference<>(view);
        this.seriesRepository = seriesRepository;

        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
        disposables.add(seriesRepository.getGenres()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
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
        //ToDO: Mostrar error
    }
}
