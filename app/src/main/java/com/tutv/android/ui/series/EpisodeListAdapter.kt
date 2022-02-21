package com.tutv.android.ui.series

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tutv.android.R
import com.tutv.android.domain.Episode
import java.util.*

class EpisodeListAdapter : RecyclerView.Adapter<EpisodeListAdapter.ViewHolder?>() {
    private val episodeList: MutableList<Episode> = ArrayList()
    private var episodeClickListener: EpisodeClickListener? = null
    fun update(episodeList: List<Episode>?) {
        this.episodeList.clear()
        if (episodeList != null) this.episodeList.addAll(episodeList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_episode, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = episodeList[position]
        holder.setEpisodeName(episode.numEpisode, episode.name)
        holder.setViewed(episode.loggedInUserViewed ?: false)
        holder.setListenerPropagation { episodeClickListener?.onClick(episode) }
    }

    override fun getItemCount(): Int {
        return episodeList.size
    }

    fun setEpisodeClickedListener(episodeClickListener: EpisodeClickListener?) {
        this.episodeClickListener = episodeClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val episodeNameTextView: TextView? = itemView.findViewById(R.id.episode_name)
        private val viewedCheckmarkImageView: ImageView? = itemView.findViewById(R.id.viewed_checkmark)

        fun setEpisodeName(number: Int?, name: String?) {
            episodeNameTextView?.text = "$number. $name"
        }

        fun setViewed(viewed: Boolean) {
            if (viewed) {
                viewedCheckmarkImageView?.setImageResource(R.drawable.ic_checkmark_green)
                viewedCheckmarkImageView?.visibility = View.VISIBLE
            } else {
                viewedCheckmarkImageView?.visibility = View.INVISIBLE
            }
        }

        fun setListenerPropagation(c: Callback?) {
            episodeNameTextView?.setOnClickListener{ c?.call() }
        }
    }

}