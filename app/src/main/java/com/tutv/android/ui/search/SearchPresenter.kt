package com.tutv.android.ui.search

import com.tutv.android.domain.Genre
import com.tutv.android.domain.Network
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

class SearchPresenter(
        view: SearchView,
        var seriesRepository: SeriesRepository,
        var schedulerProvider: BaseSchedulerProvider) {

    private val view: WeakReference<SearchView?> = WeakReference(view)
    private var searchQuery: String? = null
    private var genre: Int = -1
    private var network: Int = -1
    private var genres: MutableList<Genre?>? = null
    private var networks: MutableList<Network?>? = null
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onViewAttached() {
        disposables.add(seriesRepository.genres
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ genres: MutableList<Genre?> -> onGenresReceived(genres) }) { e: Throwable? -> onLoadError(e) })

        disposables.add(seriesRepository.networks
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ networks: MutableList<Network?> -> onNetworksReceived(networks) }) { e: Throwable? -> onLoadError(e) })
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    private fun onGenresReceived(genres: MutableList<Genre?>) {
        this.genres = genres
        if (networks != null && this.genres != null) {
            view.get()?.setFilters(this.genres!!, networks!!)
        }
    }

    private fun onNetworksReceived(networks: MutableList<Network?>) {
        this.networks = networks
        if (genres != null && this.networks != null) {
            view.get()?.setFilters(genres!!, this.networks!!)
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

}