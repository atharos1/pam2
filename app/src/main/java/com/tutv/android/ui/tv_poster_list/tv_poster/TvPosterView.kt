package com.tutv.android.ui.tv_poster_list.tv_poster

interface TvPosterView {
    fun setImageUrl(imageUrl: String?)
    fun setText(text: String?)
    fun getPresenter(): TvPosterPresenter?
    fun navigateToSeriesPage(seriesId: Int?)
}