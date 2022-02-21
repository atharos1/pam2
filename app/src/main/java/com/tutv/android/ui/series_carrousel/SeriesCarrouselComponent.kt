package com.tutv.android.ui.series_carrousel

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator

class SeriesCarrouselComponent(context: Context?) : LinearLayout(context), SeriesCarrouselView {
    private val presenter: SeriesCarrouselPresenter?
    private var buildFinished = false
    private var sliderPager: ViewPager2? = null
    private var sliderIndicator: TabLayout? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        build()
    }

    fun build() {
        if (buildFinished) return
        buildFinished = true
        sliderPager = findViewById(R.id.slider_pager)
        sliderIndicator = findViewById(R.id.slider_indicator)
        sliderPager?.adapter = SeriesCarrouselAdapter(presenter, context)
        TabLayoutMediator(sliderIndicator!!, sliderPager!!
        ) { _: TabLayout.Tab?, _: Int -> }.attach()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onViewAttached()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onViewDetached()
    }

    override fun setLoadingStatus(status: Boolean) {
        if (!status) {
            sliderPager?.adapter?.notifyDataSetChanged()
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.series_carrousel, this, true)
        val container = ContainerLocator.locateComponent(context)
        val seriesRepository = container.seriesRepository
        val schedulerProvider = container.schedulerProvider
        presenter = SeriesCarrouselPresenter(this, seriesRepository, schedulerProvider)
    }
}