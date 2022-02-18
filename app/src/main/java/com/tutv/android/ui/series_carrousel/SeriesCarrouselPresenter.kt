package com.tutv.android.ui.series_carrousel

import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.ui.series_carrousel.tv_banner.TvBannerView
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference
import java.util.*

class SeriesCarrouselPresenter(view: SeriesCarrouselView?, seriesRepository: SeriesRepository?, schedulerProvider: BaseSchedulerProvider?) {
    private val view: WeakReference<SeriesCarrouselView?>?
    private val seriesRepository: SeriesRepository?
    private val schedulerProvider: BaseSchedulerProvider?
    private var seriesList: MutableList<Series?>?
    private val disposables: CompositeDisposable?
    private fun seriesLoadSuccessful(series: MutableList<Series?>?) {
        seriesList = series
        if (view.get() != null) view.get().setLoadingStatus(false)
    }

    private fun seriesLoadError(e: Throwable?) {}
    fun getItemCount(): Int {
        return seriesList.size
    }

    fun onBindRepositoryRowViewAtPosition(position: Int, tvBannerView: TvBannerView?) {
        val series = seriesList.get(position)
        tvBannerView.getPresenter().bind(series)
    }

    fun onViewAttached() {
        view.get().setLoadingStatus(true)
        val disposable = seriesRepository.getFeatured()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe({ series: MutableList<Series?>? -> seriesLoadSuccessful(series) }) { e: Throwable? -> seriesLoadError(e) }
        disposables.add(disposable)
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    fun onViewHolderDetached(tvBannerView: TvBannerView?) {
        tvBannerView.onDetach()
    }

    init {
        this.view = WeakReference(view)
        this.seriesRepository = seriesRepository
        this.schedulerProvider = schedulerProvider
        seriesList = ArrayList()
        disposables = CompositeDisposable()
    }
}