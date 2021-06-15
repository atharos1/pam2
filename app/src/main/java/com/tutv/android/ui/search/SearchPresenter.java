package com.tutv.android.ui.search;

import androidx.core.util.Pair;

import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Network;
import com.tutv.android.repository.SeriesRepository;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter {
    private final WeakReference<SearchView> searchView;

    private final SeriesRepository seriesRepository;
    private String searchQuery;
    private int genre;
    private int network;

    private final CompositeDisposable disposables;

    public SearchPresenter(SearchView searchView, SeriesRepository seriesRepository) {
        this.searchView = new WeakReference<>(searchView);
        this.genre = -1;
        this.network = -1;
        this.seriesRepository = seriesRepository;

        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
    }

    public void onViewDetached() {
        disposables.dispose();
    }

    public void performSearch(String searchQuery) {
        this.searchQuery = searchQuery;
        SearchView actualView = searchView.get();
        if (actualView != null) {
            actualView.setSearchQuery(searchQuery,
                    genre != -1 ? genre : null,
                    network != -1 ? network : null);
        }
    }

    public void applyFilters(int genre, int network) {
        if (this.genre != genre || this.network != network) {
            this.genre = genre;
            this.network = network;
        }

        performSearch(this.searchQuery);
    }
}
