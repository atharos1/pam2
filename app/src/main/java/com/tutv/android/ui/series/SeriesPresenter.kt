package com.tutv.android.ui.series

import com.tutv.android.domain.Episode
import com.tutv.android.domain.Season
import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference

class SeriesPresenter(seriesView: SeriesView?, seriesId: Int, seriesRepository: SeriesRepository?, schedulerProvider: BaseSchedulerProvider?) {
    private val seriesView: WeakReference<SeriesView?>?
    private val seriesRepository: SeriesRepository?
    private val schedulerProvider: BaseSchedulerProvider?
    private val seriesId: Int

    //Todo: el tratamiento del objeto series deberia ser a traves de un Optional, pero no estamos en ese api level...
    private var series: Series?
    private val disposables: CompositeDisposable?
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
            actualView.showSeriesName(series.getName())
            actualView.showSeriesDescription(series.getSeriesDescription())
            actualView.showSeriesFollowed(if (series.getLoggedInUserFollows() == null) false else series.getLoggedInUserFollows())
            actualView.showFollowerCount(series.getFollowers())
            actualView.bindSeasons(series.getSeasons())
            actualView.showSeriesBanner(series.getBannerUrl())
        }
    }

    private fun onSeriesLoadError(e: Throwable?) {
        val view = seriesView.get()
        view?.showError("Error al cargar la serie, asegurese de tener conexion")
    }

    fun onEpisodeClicked(s: Season?, e: Episode?) {
        disposables.add(seriesRepository.setEpisodeViewed(series, s, e)
                .observeOn(schedulerProvider.computation())
                .flatMap { series: Series? ->
                    for (season in series.getSeasons()) {
                        if (season.number == s.getNumber()) {
                            return@flatMap Single.just(s)
                        }
                    }
                    Single.error<Season?>(Throwable())
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
        if (series.getLoggedInUserFollows() == null || !series.getLoggedInUserFollows()) {
            disposables.add(seriesRepository.setFollowSeries(series)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ series: Series? -> onSeriesFollowed(series) }) { throwable: Throwable? -> onSeriesFollowedError(throwable) })
        } else {
            disposables.add(seriesRepository.unfollowSeries(series)
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
            view.showSeriesFollowed(series.loggedInUserFollows)
        }
    }

    init {
        this.seriesView = WeakReference(seriesView)
        this.seriesId = seriesId
        this.seriesRepository = seriesRepository
        this.schedulerProvider = schedulerProvider
        series = null
        disposables = CompositeDisposable()
    }
}