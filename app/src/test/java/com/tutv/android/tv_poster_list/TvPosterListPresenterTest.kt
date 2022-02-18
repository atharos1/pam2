package com.tutv.android.tv_poster_list

import com.tutv.android.domain.Genre
import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.ui.tv_poster_list.TvPosterListPresenter
import com.tutv.android.ui.tv_poster_list.TvPosterListView
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterPresenter
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import com.tutv.android.utils.schedulers.ImmediateSchedulerProvider
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class TvPosterListPresenterTest {
    private var view: TvPosterListView? = null
    private var seriesRepository: SeriesRepository? = null
    private val genreId = 1
    private val genreName: String? = "Genre"
    private val pageSize = 10
    private var genreFirstPage: Genre? = null
    private var genreSecondPage: Genre? = null
    private var genreEmptyPage: Genre? = null
    private var presenter: TvPosterListPresenter? = null

    @Before
    fun setup() {
        view = Mockito.mock(TvPosterListView::class.java)
        seriesRepository = Mockito.mock(SeriesRepository::class.java)
        val schedulerProvider: BaseSchedulerProvider = ImmediateSchedulerProvider()
        genreFirstPage = Genre(genreId, genreName)
        genreSecondPage = Genre(genreId, genreName)
        genreEmptyPage = Genre(genreId, genreName)
        val seriesFirstPage: MutableList<Series?> = ArrayList()
        val seriesSecondPage: MutableList<Series?> = ArrayList()
        val seriesEmptyPage: MutableList<Series?> = ArrayList()
        for (i in 0 until pageSize) {
            seriesFirstPage.add(Series())
            seriesSecondPage.add(Series())
        }
        genreFirstPage.setSeries(seriesFirstPage)
        genreSecondPage.setSeries(seriesSecondPage)
        genreEmptyPage.setSeries(seriesEmptyPage)
        presenter = TvPosterListPresenter(view, seriesRepository, schedulerProvider,
                genreId, genreName, pageSize)
    }

    @Test
    fun givenTheViewWasAttachedThenSetListName() {
        presenter.onViewAttached()
        Mockito.verify(view).setListName(genreName)
    }

    @Test
    fun givenTheViewWasAttachedThenSeriesAreFetched() {
        Mockito.`when`(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage))
        presenter.onViewAttached()
        Mockito.verify(seriesRepository, Mockito.timeout(1000).times(1)).getGenreById(genreId, 1, pageSize)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).setLoadingStatus(false)
    }

    @Test
    fun givenNextPageIsRequestedThenLoadNextPage() {
        Mockito.`when`(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage))
        Mockito.`when`(seriesRepository.getGenreById(genreId, 2, pageSize)).thenReturn(Single.just(genreSecondPage))
        presenter.onViewAttached()
        presenter.getNextPage()
        Mockito.verify(seriesRepository, Mockito.timeout(1000).times(1)).getGenreById(genreId, 1, pageSize)
        Mockito.verify(seriesRepository, Mockito.timeout(1000).times(1)).getGenreById(genreId, 2, pageSize)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).setLoadingStatus(true)
        Mockito.verify(view, Mockito.timeout(1000).times(2)).setLoadingStatus(false)
    }

    @Test
    fun givenLastPageReachedThenNotifyEndReached() {
        Mockito.`when`(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage))
        Mockito.`when`(seriesRepository.getGenreById(genreId, 2, pageSize)).thenReturn(Single.just(genreEmptyPage))
        presenter.onViewAttached()
        presenter.getNextPage()
        Mockito.verify(view, Mockito.timeout(1000).times(1)).notifyEndReached()
    }

    @Test
    fun givenTheViewWasAttachedWhenNetworkErrorThenShowError() {
        Mockito.`when`(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.error(Throwable()))
        presenter.onViewAttached()
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showLoadError()
    }

    @Test
    fun givenNextPageIsRequestedWhenNetworkErrorThenShowError() {
        Mockito.`when`(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage))
        Mockito.`when`(seriesRepository.getGenreById(genreId, 2, pageSize)).thenReturn(Single.error(Throwable()))
        presenter.onViewAttached()
        presenter.getNextPage()
        Mockito.verify(view, Mockito.timeout(1000).times(1)).setLoadingStatus(false)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showLoadError()
    }

    @Test
    fun givenTheViewWasAttachedThenLoadTheCorrectSeries() {
        Mockito.`when`(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage))
        val tvPosterView = Mockito.mock(TvPosterView::class.java)
        val tvPosterPresenter = Mockito.mock(TvPosterPresenter::class.java)
        Mockito.`when`(tvPosterView.getPresenter()).thenReturn(tvPosterPresenter)
        presenter.onViewAttached()
        val inOrder = Mockito.inOrder(tvPosterPresenter)
        for (i in genreFirstPage.getSeries().indices) {
            presenter.onBindRepositoryRowViewAtPosition(i, tvPosterView)
            inOrder.verify(tvPosterPresenter).bind(genreFirstPage.getSeries()[i])
        }
        Mockito.verifyNoMoreInteractions(tvPosterPresenter)
    }

    @Test
    fun givenNextPageIsRequestedThenPagesAreAppendedInCorrectOrder() {
        Mockito.`when`(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage))
        Mockito.`when`(seriesRepository.getGenreById(genreId, 2, pageSize)).thenReturn(Single.just(genreSecondPage))
        val tvPosterView = Mockito.mock(TvPosterView::class.java)
        val tvPosterPresenter = Mockito.mock(TvPosterPresenter::class.java)
        Mockito.`when`(tvPosterView.getPresenter()).thenReturn(tvPosterPresenter)
        presenter.onViewAttached()
        presenter.getNextPage()
        val sizeFirstPage = genreFirstPage.getSeries().size
        val sizeSecondPage = genreSecondPage.getSeries().size
        val inOrder = Mockito.inOrder(tvPosterPresenter)
        for (i in 0 until sizeFirstPage) {
            presenter.onBindRepositoryRowViewAtPosition(i, tvPosterView)
            inOrder.verify(tvPosterPresenter).bind(genreFirstPage.getSeries()[i])
        }
        for (j in 0 until sizeSecondPage) {
            presenter.onBindRepositoryRowViewAtPosition(sizeFirstPage + j, tvPosterView)
            inOrder.verify(tvPosterPresenter).bind(genreSecondPage.getSeries()[j])
        }
        Mockito.verifyNoMoreInteractions(tvPosterPresenter)
    }
}