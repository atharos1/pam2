package com.tutv.android.home

import com.tutv.android.domain.Genre
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.ui.home.HomePresenter
import com.tutv.android.ui.home.HomeView
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import com.tutv.android.utils.schedulers.ImmediateSchedulerProvider
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class HomePresenterTest {
    private var view: HomeView? = null
    private var seriesRepository: SeriesRepository? = null
    private var genres: MutableList<Genre?>? = null
    private val genreId1 = 1
    private val genreName1: String? = "Genre 1"
    private val genreId2 = 2
    private val genreName2: String? = "Genre 2"
    private var presenter: HomePresenter? = null

    @Before
    fun setup() {
        view = Mockito.mock(HomeView::class.java)
        seriesRepository = Mockito.mock(SeriesRepository::class.java)
        val schedulerProvider: BaseSchedulerProvider = ImmediateSchedulerProvider()
        genres = ArrayList()
        genres.add(Genre(genreId1, genreName1))
        genres.add(Genre(genreId2, genreName2))
        presenter = HomePresenter(view, seriesRepository, schedulerProvider)
    }

    @Test
    fun givenTheViewWasAttachedThenCreateGenreList() {
        Mockito.`when`(seriesRepository.getGenres()).thenReturn(Single.just(genres))
        presenter.onViewAttached()
        Mockito.verify(view, Mockito.timeout(1000).times(1)).createGenreList(genreId1, genreName1)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).createGenreList(genreId2, genreName2)
    }

    @Test
    fun givenTheViewWasAttachedWhenNetworkErrorThenShowError() {
        Mockito.`when`(seriesRepository.getGenres()).thenReturn(Single.error(Throwable()))
        presenter.onViewAttached()
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showError()
    }
}