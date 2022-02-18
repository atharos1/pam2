package com.tutv.android.ui.series

import com.tutv.android.domain.Season
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface SeriesView {
    open fun showSeriesName(seriesName: String?)
    open fun showSeriesDescription(seriesDescription: String?)
    open fun showFollowerCount(followers: Int)
    open fun bindSeasons(seasonList: MutableList<Season?>?)
    open fun showSeriesBanner(bannerUrl: String?)
    open fun bindSeason(season: Season?)
    open fun showSeriesFollowed(followed: Boolean)
    open fun showError(error: String?)
}