package com.tutv.android.ui.home

import com.tutv.android.domain.Genre
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

class HomePresenter(
        view: HomeView,
        var seriesRepository: SeriesRepository,
        var schedulerProvider: BaseSchedulerProvider) {

    private val view: WeakReference<HomeView?> = WeakReference(view)
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onViewAttached() {
        disposables.add(seriesRepository.genres
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ genres: List<Genre> -> genresLoadSuccessful(genres) }) { e: Throwable? -> genresLoadError(e) }
        )
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    private fun genresLoadSuccessful(genres: List<Genre>) {
        for (genre in genres) view.get()?.createGenreList(genre.id, genre.name)
    }

    private fun genresLoadError(e: Throwable?) {
        val actualView = view.get()
        actualView?.showError()
    }

}