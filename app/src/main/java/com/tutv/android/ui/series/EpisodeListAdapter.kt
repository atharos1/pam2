package com.tutv.android.ui.series

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tutv.android.R
import com.tutv.android.domain.Episode
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.util.*

class EpisodeListAdapter : RecyclerView.Adapter<EpisodeListAdapter.ViewHolder?>() {
    private val episodeList: MutableList<Episode?>?
    private var episodeClickListener: EpisodeClickListener? = null
    fun update(episodeList: MutableList<Episode?>?) {
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
        val episode = episodeList.get(position)
        holder.setEpisodeName(episode.getNumEpisode(), episode.getName())
        holder.setViewed(if (episode.getLoggedInUserViewed() == null) false else episode.getLoggedInUserViewed())
        holder.setListenerPropagation(Callback { episodeClickListener.onClick(episode) })
    }

    override fun getItemCount(): Int {
        return episodeList.size
    }

    fun setEpisodeClickedListener(episodeClickListener: EpisodeClickListener?) {
        this.episodeClickListener = episodeClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val episodeNameTextView: TextView?
        private val viewedCheckmarkImageView: ImageView?
        fun setEpisodeName(number: Int, name: String?) {
            episodeNameTextView.setText("$number. $name")
        }

        fun setViewed(viewed: Boolean) {
            if (viewed) {
                viewedCheckmarkImageView.setImageResource(R.drawable.ic_checkmark_green)
                viewedCheckmarkImageView.setVisibility(View.VISIBLE)
            } else {
                viewedCheckmarkImageView.setVisibility(View.INVISIBLE)
            }
        }

        fun setListenerPropagation(c: Callback?) {
            episodeNameTextView.setOnClickListener(View.OnClickListener { event: View? -> c.call() })
        }

        init {
            episodeNameTextView = itemView.findViewById<TextView?>(R.id.episode_name)
            viewedCheckmarkImageView = itemView.findViewById<ImageView?>(R.id.viewed_checkmark)
        }
    }

    init {
        episodeList = ArrayList()
    }
}