package com.tutv.android.ui.tv_poster_list.tv_poster

import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.lang.ref.WeakReference

class TvPosterPresenter(view: TvPosterView?, seriesRepository: SeriesRepository?) {
    private val view: WeakReference<TvPosterView?>?
    private val seriesRepository: SeriesRepository?
    private var series: Series? = null
    fun bind(series: Series?) {
        this.series = series
        if (view.get() != null) {
            view.get().setImageUrl(series.getPosterUrl())
            view.get().setText(series.getName())
        }
    }

    fun onClick() {
        if (view.get() != null) view.get().navigateToSeriesPage(series.getId())
    }

    init {
        this.view = WeakReference(view)
        this.seriesRepository = seriesRepository
    }
}