package com.tutv.android.ui.search

import com.tutv.android.domain.Genre
import com.tutv.android.domain.Network
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference

class SearchPresenter(view: SearchView?, seriesRepository: SeriesRepository?, schedulerProvider: BaseSchedulerProvider?) {
    private val view: WeakReference<SearchView?>?
    private val seriesRepository: SeriesRepository?
    private val schedulerProvider: BaseSchedulerProvider?
    private var searchQuery: String? = null
    private var genre: Int
    private var network: Int
    private var genres: MutableList<Genre?>? = null
    private var networks: MutableList<Network?>? = null
    private val disposables: CompositeDisposable?
    fun onViewAttached() {
        disposables.add(seriesRepository.getGenres()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ genres: MutableList<Genre?>? -> onGenresReceived(genres) }) { e: Throwable? -> onLoadError(e) })
        disposables.add(seriesRepository.getNetworks()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ networks: MutableList<Network?>? -> onNetworksReceived(networks) }) { e: Throwable? -> onLoadError(e) })
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    private fun onGenresReceived(genres: MutableList<Genre?>?) {
        this.genres = genres
        val actualView = view.get()
        if (networks != null && actualView != null) {
            actualView.setFilters(this.genres, networks)
        }
    }

    private fun onNetworksReceived(networks: MutableList<Network?>?) {
        this.networks = networks
        val actualView = view.get()
        if (genres != null && actualView != null) {
            actualView.setFilters(genres, this.networks)
        }
    }

    private fun onLoadError(e: Throwable?) {
        val actualView = view.get()
        actualView?.showFilterError()
    }

    private fun doSearch() {
        val actualView = view.get()
        actualView?.setSearchQuery(searchQuery,
                if (genre != -1) genre else null,
                if (network != -1) network else null)
    }

    fun performSearch(searchQuery: String?) {
        if (this.searchQuery != null && this.searchQuery == searchQuery) return
        this.searchQuery = searchQuery
        doSearch()
    }

    fun applyFilters(genre: Int, network: Int) {
        if (this.genre != genre || this.network != network) {
            this.genre = genre
            this.network = network
            doSearch()
        }
    }

    init {
        this.view = WeakReference(view)
        genre = -1
        network = -1
        this.seriesRepository = seriesRepository
        this.schedulerProvider = schedulerProvider
        disposables = CompositeDisposable()
    }
}