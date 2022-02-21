package com.tutv.android.ui.tv_poster_list.tv_poster

import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import java.lang.ref.WeakReference

class TvPosterPresenter(
        view: TvPosterView,
        var seriesRepository: SeriesRepository) {

    private val view: WeakReference<TvPosterView?> = WeakReference(view)
    private var series: Series? = null

    fun bind(series: Series?) {
        this.series = series
        view.get()?.setImageUrl(series?.posterUrl)
        view.get()?.setText(series?.name)
    }

    fun onClick() {
        view.get()?.navigateToSeriesPage(series?.id)
    }

}