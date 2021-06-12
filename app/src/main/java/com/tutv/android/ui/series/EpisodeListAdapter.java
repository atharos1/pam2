package com.tutv.android.ui.series;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutv.android.R;
import com.tutv.android.domain.Episode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EpisodeListAdapter extends RecyclerView.Adapter<EpisodeListAdapter.ViewHolder> {

    private final List<Episode> episodeList;

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
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView episodeNameTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            episodeNameTextView = itemView.findViewById(R.id.episode_name);
        }

        public void setEpisodeName(int number, String name) {
            episodeNameTextView.setText(number + ". " + name);
        }

    }
}
