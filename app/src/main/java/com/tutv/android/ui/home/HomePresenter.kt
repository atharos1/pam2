package com.tutv.android.ui.home

import com.tutv.android.domain.Genre
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference

class HomePresenter(view: HomeView?, seriesRepository: SeriesRepository?, schedulerProvider: BaseSchedulerProvider?) {
    private val view: WeakReference<HomeView?>?
    private val seriesRepository: SeriesRepository?
    private val schedulerProvider: BaseSchedulerProvider?
    private val disposables: CompositeDisposable?
    fun onViewAttached() {
        disposables.add(seriesRepository.getGenres()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ genres: MutableList<Genre?>? -> genresLoadSuccessful(genres) }) { e: Throwable? -> genresLoadError(e) }
        )
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    private fun genresLoadSuccessful(genres: MutableList<Genre?>?) {
        for (genre in genres) if (view.get() != null) view.get().createGenreList(genre.getId(), genre.getName())
    }

    private fun genresLoadError(e: Throwable?) {
        val actualView = view.get()
        actualView?.showError()
    }

    init {
        this.view = WeakReference(view)
        this.seriesRepository = seriesRepository
        this.schedulerProvider = schedulerProvider
        disposables = CompositeDisposable()
    }
}