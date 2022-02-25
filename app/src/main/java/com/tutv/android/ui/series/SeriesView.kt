package com.tutv.android.ui.series

import com.tutv.android.domain.Review
import com.tutv.android.domain.Season

interface SeriesView {
    fun showSeriesName(seriesName: String?)
    fun showSeriesDescription(seriesDescription: String?)
    fun showFollowerCount(followers: Int?)
    fun bindSeasons(seasonList: List<Season>?)
    fun bindReviews(reviewList: List<Review>?)
    fun showSeriesBanner(bannerUrl: String?)
    fun bindSeason(season: Season?)
    fun showSeriesFollowed(followed: Boolean)
    fun showError(error: String?)
}