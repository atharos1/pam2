package com.tutv.android.ui.series

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tutv.android.R
import com.tutv.android.domain.Episode
import com.tutv.android.domain.Season
import java.util.*

class SeasonListAdapter(var seasonAndEpisodeClickedListener: SeasonAndEpisodeClickedListener?) :
        RecyclerView.Adapter<SeasonListAdapter.ViewHolder?>() {

    private val seasonList: MutableList<Season?> = ArrayList()

    fun update(seasonList: List<Season>?) {
        this.seasonList.clear()
        if (seasonList != null) this.seasonList.addAll(seasonList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_season, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setSeasonNumber(seasonList[position]?.number)
        holder.setEpisodes(seasonList[position]?.episodes)
        holder.setEpisodeListVisible(seasonList[position]?.isExpanded)
        if (seasonList[position] != null) {
            holder.setSeasonEpisodeClickListener {
                episode: Episode -> seasonAndEpisodeClickedListener?.onClick(seasonList[position]!!, episode)
            }
        }
    }

    override fun getItemCount(): Int {
        return seasonList.size
    }

    fun updateSeason(season: Season?) {
        for (i in seasonList.indices) {
            if (seasonList[i]?.number == season?.number) {
                seasonList[i] = season
                break
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val seasonNumberTextView: TextView? = itemView.findViewById<View?>(R.id.season_number) as TextView?
        private val seasonEpisodeRecyclerView: RecyclerView? = itemView.findViewById<View?>(R.id.season_episode_recyclerview) as RecyclerView?
        private val episodeListAdapter: EpisodeListAdapter = EpisodeListAdapter()

        fun setSeasonNumber(seasonNumber: Int?) {
            seasonNumberTextView?.text = "Season $seasonNumber"
        }

        fun setEpisodes(episodesList: List<Episode>?) {
            episodeListAdapter.update(episodesList)
        }

        fun setEpisodeListVisible(visible: Boolean?) {
            seasonEpisodeRecyclerView?.visibility = if (visible == true) View.VISIBLE else View.GONE
        }

        fun setSeasonEpisodeClickListener(episodeClickListener: EpisodeClickListener?) {
            episodeListAdapter.setEpisodeClickedListener(episodeClickListener)
        }

        init {
            seasonEpisodeRecyclerView?.adapter = episodeListAdapter
            seasonNumberTextView?.setOnClickListener {
                val season = seasonList[adapterPosition]
                if (season != null) {
                    season.isExpanded = !(season.isExpanded)
                }
                notifyItemChanged(adapterPosition)
            }
        }
    }

}