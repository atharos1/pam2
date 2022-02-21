package com.tutv.android.ui.series_carrousel.tv_banner

interface TvBannerView {
    fun setImageUrl(imageUrl: String?)
    fun setTitle(text: String?)
    fun setSubtitle(text: String?)
    fun getPresenter(): TvBannerPresenter?
    fun navigateToSeriesPage(seriesId: Int?)
    fun onDetach()
    fun setSeriesFollowed(followed: Boolean?)
    fun showError(error: String?)
}