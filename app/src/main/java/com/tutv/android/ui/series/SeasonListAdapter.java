package com.tutv.android.ui.series;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutv.android.R;
import com.tutv.android.domain.Season;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SeasonListAdapter extends RecyclerView.Adapter<SeasonListAdapter.ViewHolder> {

    private final List<Season> seasonList;

    public SeasonListAdapter() {
        this.seasonList = new ArrayList<>();
    }

    public void update(final List<Season> seasonList) {
        this.seasonList.clear();
        if(seasonList != null)
            this.seasonList.addAll(seasonList);

        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_season, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.setSeasonNumber(seasonList.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return seasonList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView seasonNumberTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            seasonNumberTextView = (TextView) itemView.findViewById(R.id.season_number);
        }

        public void setSeasonNumber(int seasonNumber) {
            seasonNumberTextView.setText("Season " + seasonNumber);
        }

    }
}
