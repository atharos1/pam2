package com.tutv.android.ui.series_carrousel.tv_banner

import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

class TvBannerPresenter(
        view: TvBannerView,
        var seriesRepository: SeriesRepository,
        var schedulerProvider: BaseSchedulerProvider) {

    private val view: WeakReference<TvBannerView?> = WeakReference(view)
    private val disposables: CompositeDisposable = CompositeDisposable()
    private var series: Series? = null

    fun bind(series: Series?) {
        this.series = series
        view.get()?.setImageUrl(series?.bannerUrl)
        view.get()?.setTitle(series?.name)
        view.get()?.setSubtitle(series?.followers.toString() + if (series?.followers == 1) " follower" else " followers")
        view.get()?.setSeriesFollowed(series?.loggedInUserFollows)
    }

    fun onClick() {
        view.get()?.navigateToSeriesPage(series?.id)
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    fun onSeriesFollowClicked() {
        if (series == null) return

        if (series?.loggedInUserFollows != true) {
            disposables.add(seriesRepository.setFollowSeries(series!!)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ series: Series? -> onSeriesFollowed(series) }) { throwable: Throwable? -> onSeriesFollowedError(throwable) })
        } else {
            disposables.add(seriesRepository.unfollowSeries(series!!)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ series: Series? -> onSeriesFollowed(series) }) { throwable: Throwable? -> onSeriesFollowedError(throwable) })
        }
    }

    private fun onSeriesFollowedError(throwable: Throwable?) {
        view.get()?.showError("Error al dejar seguir la serie :(")
    }

    private fun onSeriesFollowed(series: Series?) {
        view.get()?.setSubtitle(series?.followers.toString() + if (series?.followers == 1) " follower" else " followers")
        view.get()?.setSeriesFollowed(series?.loggedInUserFollows)
    }

}