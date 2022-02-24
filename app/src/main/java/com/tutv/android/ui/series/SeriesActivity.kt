package com.tutv.android.ui.series

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator
import com.tutv.android.domain.Episode
import com.tutv.android.domain.Review
import com.tutv.android.domain.Season
import com.tutv.android.domain.User

class SeriesActivity : AppCompatActivity(), SeriesView {
    private var seriesPresenter: SeriesPresenter? = null
    private var seriesNameTextView: TextView? = null
    private var seriesDescriptionTextView: TextView? = null
    private var seriesFollowerCountTextView: TextView? = null
    private var seriesBannerImageView: ImageView? = null
    private var floatingActionButton: FloatingActionButton? = null
    private var seasonRecyclerView: RecyclerView? = null
    private var reviewRecyclerView: RecyclerView? = null
    private var seriesCollapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var seasonListAdapter: SeasonListAdapter? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val seriesId = intent.extras?.getInt("series_id")
        setContentView(R.layout.activity_series)
        seriesDescriptionTextView = findViewById<View?>(R.id.series_description) as TextView?
        seriesFollowerCountTextView = findViewById<View?>(R.id.series_follower_count) as TextView?
        seriesCollapsingToolbarLayout = findViewById<View?>(R.id.series_collapsing_toolbar) as CollapsingToolbarLayout?
        seriesBannerImageView = findViewById<View?>(R.id.series_banner_imageview) as ImageView?
        seriesNameTextView = findViewById<View?>(R.id.series_name_textview) as TextView?
        floatingActionButton = findViewById<View?>(R.id.follow_series_button) as FloatingActionButton?
        floatingActionButton?.setOnClickListener { seriesPresenter?.onSeriesFollowClicked() }
        seasonRecyclerView = findViewById<View?>(R.id.series_season_recyclerview) as RecyclerView?
        seasonListAdapter = SeasonListAdapter { s: Season, e: Episode -> seriesPresenter?.onEpisodeClicked(s, e) }
        seasonRecyclerView?.adapter = seasonListAdapter
        reviewRecyclerView = findViewById<View?>(R.id.series_reviews_recycleview) as RecyclerView?
        supportActionBar?.hide()
        val container = ContainerLocator.locateContainer(this)
        val seriesRepository = container.seriesRepository
        val schedulerProvider = container.schedulerProvider
        if (seriesId != null) seriesPresenter = SeriesPresenter(this, seriesId, seriesRepository, schedulerProvider)
    }

    override fun onStart() {
        super.onStart()
        seriesPresenter?.onViewAttached()
    }

    override fun onStop() {
        super.onStop()
        seriesPresenter?.onViewDetached()
    }

    override fun showSeriesName(seriesName: String?) {
        seriesNameTextView?.text = seriesName
    }

    override fun showSeriesDescription(seriesDescription: String?) {
        seriesDescriptionTextView?.text = seriesDescription
    }

    override fun showFollowerCount(followers: Int?) {
        seriesFollowerCountTextView?.text = "Followers: $followers"
    }

    override fun bindSeasons(seasonList: List<Season>?) {
        seasonListAdapter?.update(seasonList)
    }

    override fun showSeriesBanner(bannerUrl: String?) {
        Picasso.get().load("https://image.tmdb.org/t/p/original$bannerUrl").fit() //.placeholder(R.drawable.user_placeholder)
                .into(seriesBannerImageView)
    }

    override fun bindSeason(season: Season?) {
        seasonListAdapter?.updateSeason(season)
    }

    override fun bindReviews(reviewList: List<Review>?) {
        reviewRecyclerView?.adapter = ReviewListAdapter(reviewList!!)
    }

    override fun showSeriesFollowed(followed: Boolean) {
        if (followed) {
            floatingActionButton?.setImageResource(R.drawable.ic_star_filled)
        } else {
            floatingActionButton?.setImageResource(R.drawable.ic_star_empty)
        }
    }

    override fun showError(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}