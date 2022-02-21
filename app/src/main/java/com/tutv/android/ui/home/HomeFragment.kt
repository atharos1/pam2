package com.tutv.android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator
import com.tutv.android.ui.series_carrousel.SeriesCarrouselComponent
import com.tutv.android.ui.tv_poster_list.TvPosterListComponent

class HomeFragment : Fragment(), HomeView {
    private var presenter: HomePresenter? = null
    private var genresLayout: LinearLayout? = null
    private var featuredBanner: SeriesCarrouselComponent? = null
    private var progressBar: ProgressBar? = null
    private var textError: TextView? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val diContainer = ContainerLocator.locateComponent(context)
        val seriesRepository = diContainer.seriesRepository
        val schedulerProvider = diContainer.schedulerProvider
        presenter = HomePresenter(this, seriesRepository, schedulerProvider)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        genresLayout = root.findViewById<LinearLayout?>(R.id.genres_linear_layout)
        progressBar = root.findViewById<ProgressBar?>(R.id.home_progressbar)
        textError = root.findViewById<TextView?>(R.id.home_error)
        featuredBanner = SeriesCarrouselComponent(context)
        featuredBanner?.build()
        genresLayout?.addView(featuredBanner)
        return root
    }

    override fun onStart() {
        super.onStart()
        presenter?.onViewAttached()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onViewDetached()
    }

    override fun createGenreList(genreId: Int, genreName: String?) {
        progressBar?.visibility = View.GONE
        val tvl = TvPosterListComponent(context, null, genreId, genreName)
        tvl.build()
        genresLayout?.addView(tvl)
    }

    override fun showError() {
        progressBar?.visibility = View.GONE
        textError?.visibility = View.VISIBLE
    }
}