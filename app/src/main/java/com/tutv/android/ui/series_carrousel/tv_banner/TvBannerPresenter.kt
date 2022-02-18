package com.tutv.android.ui.series_carrousel.tv_banner

import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference

class TvBannerPresenter(view: TvBannerView?, seriesRepository: SeriesRepository?, schedulerProvider: BaseSchedulerProvider?) {
    private val view: WeakReference<TvBannerView?>?
    private val seriesRepository: SeriesRepository?
    private val schedulerProvider: BaseSchedulerProvider?
    private val disposables: CompositeDisposable?
    private var series: Series? = null
    fun bind(series: Series?) {
        this.series = series
        if (view.get() != null) {
            view.get().setImageUrl(series.getBannerUrl())
            view.get().setTitle(series.getName())
            view.get().setSubtitle(series.getFollowers().toString() + if (series.getFollowers() == 1) " follower" else " followers")
            view.get().setSeriesFollowed(series.getLoggedInUserFollows() != null && series.getLoggedInUserFollows())
        }
    }

    fun onClick() {
        if (view.get() != null) view.get().navigateToSeriesPage(series.getId())
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    fun onSeriesFollowClicked() {
        if (series.getLoggedInUserFollows() == null || !series.getLoggedInUserFollows()) {
            disposables.add(seriesRepository.setFollowSeries(series)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ series: Series? -> onSeriesFollowed(series) }) { throwable: Throwable? -> onSeriesFollowedError(throwable) })
        } else {
            disposables.add(seriesRepository.unfollowSeries(series)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ series: Series? -> onSeriesFollowed(series) }) { throwable: Throwable? -> onSeriesFollowedError(throwable) })
        }
    }

    private fun onSeriesFollowedError(throwable: Throwable?) {
        if (view.get() != null) {
            view.get().showError("Error al dejar seguir la serie :(")
        }
    }

    private fun onSeriesFollowed(series: Series?) {
        if (view.get() != null) {
            view.get().setSubtitle(series.getFollowers().toString() + if (series.getFollowers() == 1) " follower" else " followers")
            view.get().setSeriesFollowed(series.getLoggedInUserFollows())
        }
    }

    init {
        this.view = WeakReference(view)
        this.seriesRepository = seriesRepository
        this.schedulerProvider = schedulerProvider
        disposables = CompositeDisposable()
    }
}