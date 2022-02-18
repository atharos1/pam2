package com.tutv.android.ui.series_carrousel.tv_banner

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator
import com.tutv.android.ui.series.SeriesActivity
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class TvBannerComponent(itemView: View) : RecyclerView.ViewHolder(itemView), TvBannerView {
    private val presenter: TvBannerPresenter?
    private val title: TextView?
    private val subtitle: TextView?
    private val image: ImageView?
    private val followButton: FloatingActionButton?
    override fun setImageUrl(imageUrl: String?) {
        Picasso.get().load("https://image.tmdb.org/t/p/w500$imageUrl").fit().into(image)
    }

    override fun setTitle(text: String?) {
        title.setText(text)
    }

    override fun setSubtitle(text: String?) {
        subtitle.setText(text)
    }

    override fun getPresenter(): TvBannerPresenter? {
        return presenter
    }

    override fun navigateToSeriesPage(seriesId: Int) {
        val intent = Intent(itemView.context, SeriesActivity::class.java)
        intent.putExtra("series_id", seriesId)
        itemView.context.startActivity(intent)
    }

    override fun onDetach() {
        presenter.onViewDetached()
    }

    override fun setSeriesFollowed(followed: Boolean) {
        if (followed) {
            followButton.setImageResource(R.drawable.ic_star_filled)
        } else {
            followButton.setImageResource(R.drawable.ic_star_empty)
        }
    }

    override fun showError(error: String?) {
        Toast.makeText(itemView.context, error, Toast.LENGTH_SHORT).show()
    }

    init {
        title = itemView.findViewById<TextView?>(R.id.slide_title)
        subtitle = itemView.findViewById<TextView?>(R.id.slide_subtitle)
        image = itemView.findViewById<ImageView?>(R.id.slide_image)
        followButton = itemView.findViewById(R.id.slide_follow_button)
        val container = ContainerLocator.locateComponent(itemView.context)
        val seriesRepository = container.seriesRepository
        val schedulerProvider = container.schedulerProvider
        presenter = TvBannerPresenter(this, seriesRepository, schedulerProvider)
        itemView.setOnClickListener { v: View? -> presenter.onClick() }
        followButton.setOnClickListener(View.OnClickListener { event: View? -> presenter.onSeriesFollowClicked() })
    }
}