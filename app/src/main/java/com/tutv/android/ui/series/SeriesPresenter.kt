package com.tutv.android.ui.series

import android.util.Log
import com.tutv.android.domain.*
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
    private var reviews: MutableList<Review>? = null
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun onViewAttached() {
        disposables.add(seriesRepository.getSeriesById(seriesId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ series: Series? -> onSeriesLoad(series) }) { e: Throwable? -> onSeriesLoadError(e) })

        disposables.add(seriesRepository.getReviews(seriesId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                //TODO onReviewsLoadError
                .subscribe({ reviews: List<Review> -> onReviewsLoad(reviews) }) { e: Throwable? -> onSeriesLoadError(e) })
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

    private fun onReviewsLoad(reviews: List<Review>?) {
        this.reviews = reviews?.toMutableList()
        /*this.reviews = mutableListOf()
        this.reviews?.add(Review(0, 1, "La peor serie que vi en mi vida. La trama no tiene sentido, el personaje principal es superficial e irrelevante, y los efectos especiales me hicieron sangrar los ojos.", emptyList(), User(), false))
        this.reviews?.get(0)?.user?.userName = "Juan de los palotes"
        this.reviews?.add(Review(1, 10, "Una hermosa serie para compartir en familia.", emptyList(), User(), false))
        this.reviews?.get(1)?.user?.userName = "Lola Mento"*/
        seriesView.get()?.bindReviews(this.reviews)
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
        seriesView.get()?.showError("Error al ver el episodio, asegurese de tener conexion y estar logueado")
    }

    private fun onEpisodeViewed(season: Season?) {
        seriesView.get()?.bindSeason(season)
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
        seriesView.get()?.showError("Error al dejar de seguir la serie, asegurese de tener conexion y estar logueado")
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
        seriesView.get()?.showError("Error al seguir la serie, asegurese de tener conexion y estar logueado")
    }

    private fun onSeriesFollowed(series: Series?) {
        if (series == null) return
        val view = seriesView.get()
        if (view != null) {
            view.showFollowerCount(series.followers)
            view.showSeriesFollowed(series.loggedInUserFollows ?: false)
        }
    }

    fun onReviewLikeClicked(r: Review) {
        if (series == null) return

        disposables.add(seriesRepository.setReviewLiked(series!!, r, !(r.loggedInUserLikes ?: false))
                .observeOn(schedulerProvider.ui())
                .subscribe({ review: Review? -> onReviewLiked(review) }) { throwable: Throwable? -> onReviewLikedError(throwable) })
    }

    private fun onReviewLiked(r: Review?) {
        seriesView.get()?.bindReviews(this.reviews)
    }

    private fun onReviewLikedError(throwable: Throwable?) {
        seriesView.get()?.showError("Error al dar me gusta al comentario, asegurese de tener conexion y estar logueado")
    }

    fun onReviewSubmitClicked(body: String?) {
        if (series == null || body.isNullOrEmpty()) return

        disposables.add(seriesRepository.postReview(series!!, body)
                .observeOn(schedulerProvider.ui())
                .subscribe({ review: Review? -> onReviewSubmitted(review) }) { throwable: Throwable? -> onReviewSubmittedError(throwable) })
    }

    private fun onReviewSubmitted(r: Review?) {
        if (r != null) {
            this.reviews?.add(0, r)
            seriesView.get()?.bindReviews(this.reviews)
        }
    }

    private fun onReviewSubmittedError(throwable: Throwable?) {
        seriesView.get()?.showError("Error al publicar el comentario, asegurese de tener conexion y estar logueado")
    }

}