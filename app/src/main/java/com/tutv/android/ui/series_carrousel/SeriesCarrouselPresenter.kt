package com.tutv.android.ui.series_carrousel

import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.ui.series_carrousel.tv_banner.TvBannerView
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference
import java.util.*

class SeriesCarrouselPresenter(
        view: SeriesCarrouselView,
        var seriesRepository: SeriesRepository,
        var schedulerProvider: BaseSchedulerProvider) {

    private val view: WeakReference<SeriesCarrouselView?> = WeakReference(view)
    private var seriesList: List<Series?> = ArrayList()
    private val disposables: CompositeDisposable = CompositeDisposable()

    private fun seriesLoadSuccessful(series: List<Series?>) {
        seriesList = series
        view.get()?.setLoadingStatus(false)
    }

    private fun seriesLoadError(e: Throwable?) {}

    fun getItemCount(): Int {
        return seriesList.size
    }

    fun onBindRepositoryRowViewAtPosition(position: Int, tvBannerView: TvBannerView?) {
        val series = seriesList[position]
        tvBannerView?.getPresenter()?.bind(series)
    }

    fun onViewAttached() {
        view.get()?.setLoadingStatus(true)

        val disposable = seriesRepository.featured
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ series: List<Series?> -> seriesLoadSuccessful(series) }) { e: Throwable? -> seriesLoadError(e) }

        disposables.add(disposable)
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    fun onViewHolderDetached(tvBannerView: TvBannerView?) {
        tvBannerView?.onDetach()
    }

}