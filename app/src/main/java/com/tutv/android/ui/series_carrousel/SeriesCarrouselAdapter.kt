package com.tutv.android.ui.series_carrousel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tutv.android.R
import com.tutv.android.ui.series_carrousel.tv_banner.TvBannerComponent
import com.tutv.android.ui.series_carrousel.tv_banner.TvBannerView

class SeriesCarrouselAdapter(private val presenter: SeriesCarrouselPresenter?, private val context: Context?) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val slideLayout = inflater.inflate(R.layout.slider_item, parent, false)
        return TvBannerComponent(slideLayout)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        presenter?.onViewHolderDetached(holder as TvBannerView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        presenter?.onBindRepositoryRowViewAtPosition(position, holder as TvBannerView)
    }

    override fun getItemCount(): Int {
        return presenter?.getItemCount() ?: 0
    }

}