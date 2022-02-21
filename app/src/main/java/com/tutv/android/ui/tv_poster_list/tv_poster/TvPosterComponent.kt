package com.tutv.android.ui.tv_poster_list.tv_poster

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator
import com.tutv.android.ui.series.SeriesActivity

class TvPosterComponent(itemView: View) : RecyclerView.ViewHolder(itemView), TvPosterView {
    var title: TextView? = itemView.findViewById(R.id.tv_poster_title)
    var image: ImageView? = itemView.findViewById(R.id.tv_poster_image)
    private val presenter: TvPosterPresenter?

    override fun setImageUrl(imageUrl: String?) {
        Picasso.get().load("https://image.tmdb.org/t/p/original$imageUrl").fit() //.placeholder(R.drawable.user_placeholder)
                .into(image)
    }

    override fun setText(text: String?) {
        title?.text = text
    }

    override fun getPresenter(): TvPosterPresenter? {
        return presenter
    }

    override fun navigateToSeriesPage(seriesId: Int?) {
        val intent = Intent(itemView.context, SeriesActivity::class.java)
        intent.putExtra("series_id", seriesId)
        itemView.context.startActivity(intent)
    }

    init {
        val container = ContainerLocator.locateContainer(itemView.context)
        val seriesRepository = container.seriesRepository
        presenter = TvPosterPresenter(this, seriesRepository)
        itemView.setOnClickListener { presenter.onClick() }
    }
}