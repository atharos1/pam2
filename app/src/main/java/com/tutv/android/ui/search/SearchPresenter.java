package com.tutv.android.ui.search;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

public class SearchPresenter {
    private final WeakReference<SearchView> searchView;

    private final CompositeDisposable disposables;

    public SearchPresenter(SearchView searchView) {
        this.searchView = new WeakReference<>(searchView);

        this.disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
    }

    public void onViewDetached() {
        disposables.dispose();
    }

    public void performSearch(String searchQuery) {
        SearchView actualView = searchView.get();
        if (actualView != null) {
            actualView.setSearchQuery(searchQuery);
        }
    }
}
