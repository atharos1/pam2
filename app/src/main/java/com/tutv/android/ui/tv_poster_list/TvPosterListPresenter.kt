package com.tutv.android.ui.tv_poster_list

import com.tutv.android.domain.Genre
import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.PublishProcessor
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference
import java.util.*

class TvPosterListPresenter private constructor(view: TvPosterListView?, seriesRepository: SeriesRepository?, schedulerProvider: BaseSchedulerProvider?) {
    private enum class Mode {
        GENRE, SEARCH
    }

    private var genreId = 0
    private var genreName: String? = null
    private var query: String? = null
    private var genre: Int? = null
    private var network: Int? = null
    private var mode: Mode? = null
    private var pageSize = 6
    private var loading = true
    private var reachedEnd = false
    private val view: WeakReference<TvPosterListView?>?
    private val seriesList: MutableList<Series?>?
    private val mPublishProcessor: PublishProcessor<Int?>?
    private val seriesRepository: SeriesRepository?
    private val schedulerProvider: BaseSchedulerProvider?
    private val disposables: CompositeDisposable?

    constructor(view: TvPosterListView?, seriesRepository: SeriesRepository?,
                schedulerProvider: BaseSchedulerProvider?, genreId: Int, genreName: String?, pageSize: Int) : this(view, seriesRepository, schedulerProvider) {
        this.genreId = genreId
        this.genreName = genreName
        this.pageSize = pageSize
        mode = Mode.GENRE
    }

    constructor(view: TvPosterListView?, seriesRepository: SeriesRepository?,
                schedulerProvider: BaseSchedulerProvider?, query: String?, genre: Int?, network: Int?, pageSize: Int) : this(view, seriesRepository, schedulerProvider) {
        this.query = query
        this.genre = genre
        this.network = network
        this.pageSize = pageSize
        mode = Mode.SEARCH
    }

    fun onViewAttached() {
        initObservable()
        val actualView = view.get()
        actualView?.setListName(genreName)
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    fun onBindRepositoryRowViewAtPosition(position: Int, tvPosterView: TvPosterView?) {
        val series = seriesList.get(position)
        tvPosterView.getPresenter().bind(series)
    }

    fun getItemCount(): Int {
        return seriesList.size
    }

    private fun initObservable() {
        val disposable: Disposable?
        disposable = when (mode) {
            Mode.GENRE -> mPublishProcessor
                    .doOnNext(Consumer { page: Int? -> onNext(page) })
                    .concatMapSingle { page: Int? -> seriesRepository.getGenreById(genreId, page, pageSize) }
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ genre: Genre? -> onLoadGenre(genre) }) { e: Throwable? -> onLoadError(e) }
            Mode.SEARCH -> mPublishProcessor
                    .doOnNext(Consumer { page: Int? -> onNext(page) })
                    .concatMapSingle { page: Int? -> seriesRepository.getSeriesSearch(query, page, genre, network, 18) }
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ series: MutableList<Series?>? -> onLoadSeries(series) }) { e: Throwable? -> onLoadError(e) }
            else -> return
        }
        disposables.add(disposable)
        mPublishProcessor.onNext(getPageNumber() + 1)
    }

    private fun onNext(page: Int?) {
        loading = true
        val actualView = view.get()
        if (actualView != null && seriesList.size > 0) {
            actualView.setLoadingStatus(true)
        }
    }

    private fun onLoadGenre(genre: Genre?) {
        onLoadSeries(genre.getSeries())
    }

    private fun onLoadSeries(series: MutableList<Series?>?) {
        if (series.size < pageSize) reachedEnd = true
        seriesList.addAll(series)
        val actualView = view.get()
        loading = false
        if (actualView != null) {
            actualView.setLoadingStatus(false)
            if (reachedEnd) actualView.notifyEndReached()
        }
    }

    private fun onLoadError(e: Throwable?) {
        val actualView = view.get()
        actualView?.showLoadError()
    }

    fun getNextPage() {
        if (!loading && !reachedEnd) {
            mPublishProcessor.onNext(getPageNumber() + 1)
        }
    }

    private fun getPageNumber(): Int {
        return seriesList.size / pageSize
    }

    init {
        this.view = WeakReference(view)
        seriesList = ArrayList()
        this.seriesRepository = seriesRepository
        this.schedulerProvider = schedulerProvider
        mPublishProcessor = PublishProcessor.create()
        disposables = CompositeDisposable()
    }
}