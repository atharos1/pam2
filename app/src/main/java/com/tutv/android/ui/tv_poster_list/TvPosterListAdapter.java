package com.tutv.android.ui.tv_poster_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutv.android.R;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterComponent;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView;

import org.jetbrains.annotations.NotNull;

public class TvPosterListAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private TvPosterListPresenter presenter;

    public TvPosterListAdapter(TvPosterListPresenter presenter, Context mContext) {
        this.presenter = presenter;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View tvPoster = inflater.inflate(R.layout.tv_poster, parent, false);

        return new TvPosterComponent(tvPoster);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        presenter.onBindRepositoryRowViewAtPosition(position, (TvPosterView) holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }
}
