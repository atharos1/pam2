package com.tutv.android.search

import com.tutv.android.domain.Genre
import com.tutv.android.domain.Network
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.ui.search.SearchPresenter
import com.tutv.android.ui.search.SearchView
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import com.tutv.android.utils.schedulers.ImmediateSchedulerProvider
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.util.*

class SearchPresenterTest {
    private var view: SearchView? = null
    private var seriesRepository: SeriesRepository? = null
    private var presenter: SearchPresenter? = null

    @Before
    fun setup() {
        view = Mockito.mock(SearchView::class.java)
        seriesRepository = Mockito.mock(SeriesRepository::class.java)
        val schedulerProvider: BaseSchedulerProvider = ImmediateSchedulerProvider()
        presenter = SearchPresenter(view, seriesRepository, schedulerProvider)
    }

    @Test
    fun givenTheViewWasAttachedThenFiltersAreFetched() {
        val genres: MutableList<Genre?> = ArrayList()
        val networks: MutableList<Network?> = ArrayList()
        Mockito.`when`(seriesRepository.getGenres()).thenReturn(Single.just(genres))
        Mockito.`when`(seriesRepository.getNetworks()).thenReturn(Single.just(networks))
        presenter.onViewAttached()
        Mockito.verify(view, Mockito.timeout(1000).atLeastOnce()).setFilters(genres, networks)
    }

    @Test
    fun givenTheViewWasAttachedWhenGenreErrorThenShowError() {
        val networks: MutableList<Network?> = ArrayList()
        Mockito.`when`(seriesRepository.getGenres()).thenReturn(Single.error(Throwable()))
        Mockito.`when`(seriesRepository.getNetworks()).thenReturn(Single.just(networks))
        presenter.onViewAttached()
        Mockito.verify(view, Mockito.timeout(1000).atLeastOnce()).showFilterError()
    }

    @Test
    fun givenTheViewWasAttachedWhenNetworkErrorThenShowError() {
        val genres: MutableList<Genre?> = ArrayList()
        Mockito.`when`(seriesRepository.getGenres()).thenReturn(Single.just(genres))
        Mockito.`when`(seriesRepository.getNetworks()).thenReturn(Single.error(Throwable()))
        presenter.onViewAttached()
        Mockito.verify(view, Mockito.timeout(1000).atLeastOnce()).showFilterError()
    }

    @Test
    fun givenASearchWasPerformedThenSetSearchQuery() {
        val searchQuery = "a search query"
        presenter.performSearch(searchQuery)
        Mockito.verify(view, Mockito.times(1)).setSearchQuery(searchQuery, null, null)
    }

    @Test
    fun givenASearchWasPerformedWhenQueryStaysTheSameThenDontSetSearchQuery() {
        val searchQuery = "a search query"
        presenter.performSearch(searchQuery)
        Mockito.verify(view, Mockito.times(1)).setSearchQuery(searchQuery, null, null)

        /* should not call setSearchQuery again if the query stays the same */presenter.performSearch(searchQuery)
        Mockito.verify(view, Mockito.times(1)).setSearchQuery(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    @Test
    fun givenASearchWasPerformedWhenQueryChangesThenSetSearchQuery() {
        val searchQuery = "a search query"
        val anotherSearchQuery = "another search query"
        presenter.performSearch(searchQuery)
        Mockito.verify(view, Mockito.times(1)).setSearchQuery(searchQuery, null, null)

        /* should call setSearchQuery again since the query changed */presenter.performSearch(anotherSearchQuery)
        Mockito.verify(view, Mockito.times(2)).setSearchQuery(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    @Test
    fun givenFiltersAreAppliedThenSetSearchQuery() {
        val genre = 1
        val network = 1
        presenter.applyFilters(genre, network)
        Mockito.verify(view, Mockito.times(1)).setSearchQuery(null, genre, network)
    }

    @Test
    fun givenFiltersAreAppliedWhenFiltersStayTheSameThenDontSetSearchQuery() {
        val genre = 1
        val network = 1
        presenter.applyFilters(genre, network)
        Mockito.verify(view, Mockito.times(1)).setSearchQuery(null, genre, network)

        /* should not call setSearchQuery again if filters stay the same */presenter.applyFilters(genre, network)
        Mockito.verify(view, Mockito.times(1)).setSearchQuery(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    @Test
    fun givenFiltersAreAppliedWhenFiltersChangeTheSameThenSetSearchQuery() {
        var genre = 1
        var network = 1
        presenter.applyFilters(genre, network)
        Mockito.verify(view, Mockito.times(1)).setSearchQuery(null, genre, network)

        /* should call setSearchQuery again if at least one filter changed */genre = 2
        network = 1
        presenter.applyFilters(genre, network)
        Mockito.verify(view, Mockito.times(2)).setSearchQuery(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
        genre = 2
        network = 2
        presenter.applyFilters(genre, network)
        Mockito.verify(view, Mockito.times(3)).setSearchQuery(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    @Test
    fun givenFiltersAreAppliedWhenFiltersAreNullThenSetSearchQuery() {
        var genre = 1
        var network = 1
        presenter.applyFilters(genre, network)
        Mockito.verify(view, Mockito.times(1)).setSearchQuery(null, genre, network)

        /* should get null instead of -1 */genre = -1
        network = -1
        presenter.applyFilters(genre, network)
        Mockito.verify(view).setSearchQuery(null, null, null)
    }
}