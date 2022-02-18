package com.tutv.android.ui.tv_poster_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tutv.android.R
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterComponent
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class TvPosterListAdapter(private val presenter: TvPosterListPresenter?, private val mContext: Context?) : RecyclerView.Adapter<Any?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val tvPoster = inflater.inflate(R.layout.tv_poster, parent, false)
        return TvPosterComponent(tvPoster)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        presenter.onBindRepositoryRowViewAtPosition(position, holder as TvPosterView)
    }

    override fun getItemCount(): Int {
        return presenter.getItemCount()
    }

}