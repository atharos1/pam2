package com.tutv.android.ui.search;

import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Network;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class SearchPresenter {
    private final WeakReference<SearchView> view;

    private final SeriesRepository seriesRepository;
    private final BaseSchedulerProvider schedulerProvider;
    private String searchQuery;
    private int genre;
    private int network;
    private List<Genre> genres;
    private List<Network> networks;

    private final CompositeDisposable disposables;

    public SearchPresenter(SearchView view, SeriesRepository seriesRepository, BaseSchedulerProvider schedulerProvider) {
        this.view = new WeakReference<>(view);
        this.genre = -1;
        this.network = -1;
        this.seriesRepository = seriesRepository;
        this.schedulerProvider = schedulerProvider;

        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
        disposables.add(seriesRepository.getGenres()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onGenresReceived, this::onLoadError));

        disposables.add(seriesRepository.getNetworks()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onNetworksReceived, this::onLoadError));
    }

    public void onViewDetached() {
        disposables.dispose();
    }

    private void onGenresReceived(List<Genre> genres) {
        this.genres = genres;
        SearchView actualView = view.get();
        if (this.networks != null && actualView != null) {
            actualView.setFilters(this.genres, this.networks);
        }
    }

    private void onNetworksReceived(List<Network> networks) {
        this.networks = networks;
        SearchView actualView = view.get();
        if (this.genres != null && actualView != null) {
            actualView.setFilters(this.genres, this.networks);
        }
    }

    private void onLoadError(final Throwable e) {
        SearchView actualView = view.get();
        if (actualView != null) {
            actualView.showFilterError();
        }
    }

    private void doSearch() {
        SearchView actualView = view.get();
        if (actualView != null) {
            actualView.setSearchQuery(searchQuery,
                    genre != -1 ? genre : null,
                    network != -1 ? network : null);
        }
    }

    public void performSearch(String searchQuery) {
        if (this.searchQuery != null && this.searchQuery.equals(searchQuery))
            return;

        this.searchQuery = searchQuery;
        doSearch();
    }

    public void applyFilters(int genre, int network) {
        if (this.genre != genre || this.network != network) {
            this.genre = genre;
            this.network = network;
            doSearch();
        }
    }
}
