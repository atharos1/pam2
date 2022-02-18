package com.tutv.android.ui.tv_poster_list.tv_poster

import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface TvPosterView {
    open fun setImageUrl(imageUrl: String?)
    open fun setText(text: String?)
    open fun getPresenter(): TvPosterPresenter?
    open fun navigateToSeriesPage(seriesId: Int)
}