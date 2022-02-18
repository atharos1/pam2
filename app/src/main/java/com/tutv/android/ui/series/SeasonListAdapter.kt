package com.tutv.android.ui.series

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tutv.android.R
import com.tutv.android.domain.Episode
import com.tutv.android.domain.Season
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito
import java.util.*

class SeasonListAdapter(seasonAndEpisodeClickedListener: SeasonAndEpisodeClickedListener?) : RecyclerView.Adapter<SeasonListAdapter.ViewHolder?>() {
    private val seasonList: MutableList<Season?>?
    private val seasonAndEpisodeClickedListener: SeasonAndEpisodeClickedListener?
    fun update(seasonList: MutableList<Season?>?) {
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
        holder.setSeasonNumber(seasonList.get(position).getNumber())
        holder.setEpisodes(seasonList.get(position).getEpisodes())
        holder.setEpisodeListVisible(seasonList.get(position).isExpanded())
        holder.setSeasonEpisodeClickListener(EpisodeClickListener { episode: Episode? -> seasonAndEpisodeClickedListener.onClick(seasonList.get(position), episode) })
    }

    override fun getItemCount(): Int {
        return seasonList.size
    }

    fun updateSeason(season: Season?) {
        for (i in seasonList.indices) {
            if (seasonList.get(i).getNumber() == season.getNumber()) {
                seasonList.set(i, season)
                break
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val seasonNumberTextView: TextView?
        private val seasonEpisodeRecyclerView: RecyclerView?
        private val episodeListAdapter: EpisodeListAdapter?
        fun setSeasonNumber(seasonNumber: Int) {
            seasonNumberTextView.setText("Season $seasonNumber")
        }

        fun setEpisodes(episodesList: MutableList<Episode?>?) {
            episodeListAdapter.update(episodesList)
        }

        fun setEpisodeListVisible(visible: Boolean) {
            seasonEpisodeRecyclerView.setVisibility(if (visible) View.VISIBLE else View.GONE)
        }

        fun setSeasonEpisodeClickListener(episodeClickListener: EpisodeClickListener?) {
            episodeListAdapter.setEpisodeClickedListener(episodeClickListener)
        }

        init {
            seasonNumberTextView = itemView.findViewById<View?>(R.id.season_number) as TextView?
            seasonEpisodeRecyclerView = itemView.findViewById<View?>(R.id.season_episode_recyclerview) as RecyclerView?
            episodeListAdapter = EpisodeListAdapter()
            seasonEpisodeRecyclerView.setAdapter(episodeListAdapter)
            seasonNumberTextView.setOnClickListener(View.OnClickListener { event: View? ->
                val season = seasonList.get(adapterPosition)
                season.setExpanded(!season.isExpanded())
                notifyItemChanged(adapterPosition)
            })
        }
    }

    init {
        seasonList = ArrayList()
        this.seasonAndEpisodeClickedListener = seasonAndEpisodeClickedListener
    }
}