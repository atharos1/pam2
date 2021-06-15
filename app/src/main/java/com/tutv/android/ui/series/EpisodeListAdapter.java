package com.tutv.android.ui.series;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutv.android.R;
import com.tutv.android.domain.Episode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EpisodeListAdapter extends RecyclerView.Adapter<EpisodeListAdapter.ViewHolder> {

    private final List<Episode> episodeList;

    private EpisodeClickListener episodeClickListener;

    public EpisodeListAdapter() {
        this.episodeList = new ArrayList<>();
    }

    public void update(List<Episode> episodeList) {
        this.episodeList.clear();
        if(episodeList != null)
            this.episodeList.addAll(episodeList);

        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_episode, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Episode episode = episodeList.get(position);
        holder.setEpisodeName(episode.getNumEpisode(), episode.getName());
        holder.setViewed(episode.getLoggedInUserViewed() == null ? false : episode.getLoggedInUserViewed());
        holder.setListenerPropagation(() -> episodeClickListener.onClick(episode));
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public void setEpisodeClickedListener(EpisodeClickListener episodeClickListener) {
        this.episodeClickListener = episodeClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView episodeNameTextView;
        private ImageView viewedCheckmarkImageView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            episodeNameTextView = itemView.findViewById(R.id.episode_name);
            viewedCheckmarkImageView = itemView.findViewById(R.id.viewed_checkmark);
        }

        public void setEpisodeName(int number, String name) {
            episodeNameTextView.setText(number + ". " + name);
        }

        public void setViewed(boolean viewed) {
            if(viewed) {
                viewedCheckmarkImageView.setImageResource(R.drawable.ic_checkmark_green);
                viewedCheckmarkImageView.setVisibility(View.VISIBLE);
            } else {
                viewedCheckmarkImageView.setVisibility(View.INVISIBLE);
            }
        }

        public void setListenerPropagation(Callback c) {
            episodeNameTextView.setOnClickListener(event -> c.call());
        }
    }
}
