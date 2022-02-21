package com.tutv.android.ui.series

import com.tutv.android.domain.Episode
import com.tutv.android.domain.Season
import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

class SeriesPresenter(
        seriesView: SeriesView?,
        var seriesId: Int,
        var seriesRepository: SeriesRepository,
        var schedulerProvider: BaseSchedulerProvider) {

    private val seriesView: WeakReference<SeriesView?> = WeakReference(seriesView)
    private var series: Series? = null
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onViewAttached() {
        disposables.add(seriesRepository.getSeriesById(seriesId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ series: Series? -> onSeriesLoad(series) }) { e: Throwable? -> onSeriesLoadError(e) })
    }

    fun onViewDetached() {
        disposables.dispose()
    }

    private fun onSeriesLoad(series: Series?) {
        this.series = series
        val actualView = seriesView.get()
        if (actualView != null) {
            actualView.showSeriesName(series?.name)
            actualView.showSeriesDescription(series?.seriesDescription)
            actualView.showSeriesFollowed(series?.loggedInUserFollows ?: false)
            actualView.showFollowerCount(series?.followers)
            actualView.bindSeasons(series?.seasons)
            actualView.showSeriesBanner(series?.bannerUrl)
        }
    }

    private fun onSeriesLoadError(e: Throwable?) {
        val view = seriesView.get()
        view?.showError("Error al cargar la serie, asegurese de tener conexion")
    }

    fun onEpisodeClicked(s: Season, e: Episode) {
        if (series == null) return

        disposables.add(seriesRepository.setEpisodeViewed(series!!, s, e)
                .observeOn(schedulerProvider.computation())
                .flatMap { series: Series? ->
                    for (season in series?.seasons!!) {
                        if (season.number == s.number) {
                            return@flatMap Single.just(s)
                        }
                    }
                    Single.error(Throwable())
                }
                .observeOn(schedulerProvider.ui())
                .subscribe({ season: Season? -> onEpisodeViewed(season) }) { throwable: Throwable? -> onEpisodeViewedError(throwable) })
    }

    private fun onEpisodeViewedError(throwable: Throwable?) {
        System.err.println(throwable)
        val view = seriesView.get()
        view?.showError("Error al ver el episodio, asegurese de tener conexion y estar logueado")
    }

    private fun onEpisodeViewed(season: Season?) {
        val view = seriesView.get()
        view?.bindSeason(season)
    }

    fun onSeriesFollowClicked() {
        if (series == null) return

        if (series!!.loggedInUserFollows == null || !series?.loggedInUserFollows!!) {
            disposables.add(seriesRepository.setFollowSeries(series!!)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ series: Series? -> onSeriesFollowed(series) }) { throwable: Throwable? -> onSeriesFollowedError(throwable) })
        } else {
            disposables.add(seriesRepository.unfollowSeries(series!!)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ series: Series? -> onSeriesUnfollowed(series) }) { throwable: Throwable? -> onSeriesUnfollowedError(throwable) })
        }
    }

    private fun onSeriesUnfollowedError(throwable: Throwable?) {
        val view = seriesView.get()
        view?.showError("Error al dejar de seguir la serie, asegurese de tener conexion y estar logueado")
    }

    private fun onSeriesUnfollowed(series: Series?) {
        if (series == null) return
        val view = seriesView.get()
        if (view != null) {
            view.showSeriesFollowed(false)
            view.showFollowerCount(series.followers)
        }
    }

    private fun onSeriesFollowedError(throwable: Throwable?) {
        val view = seriesView.get()
        view?.showError("Error al seguir la serie, asegurese de tener conexion y estar logueado")
    }

    private fun onSeriesFollowed(series: Series?) {
        if (series == null) return
        val view = seriesView.get()
        if (view != null) {
            view.showFollowerCount(series.followers)
            view.showSeriesFollowed(series.loggedInUserFollows ?: false)
        }
    }

}