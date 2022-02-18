package com.tutv.android.ui.series_carrousel.tv_banner

import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface TvBannerView {
    open fun setImageUrl(imageUrl: String?)
    open fun setTitle(text: String?)
    open fun setSubtitle(text: String?)
    open fun getPresenter(): TvBannerPresenter?
    open fun navigateToSeriesPage(seriesId: Int)
    open fun onDetach()
    open fun setSeriesFollowed(followed: Boolean)
    open fun showError(error: String?)
}