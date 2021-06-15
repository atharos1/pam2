package com.tutv.android.ui.series_carrousel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutv.android.R;
import com.tutv.android.ui.series_carrousel.tv_banner.TvBannerComponent;
import com.tutv.android.ui.series_carrousel.tv_banner.TvBannerView;

import org.jetbrains.annotations.NotNull;

public class SeriesCarrouselAdapter extends RecyclerView.Adapter {
    private final SeriesCarrouselPresenter presenter;
    private final Context context;

    public SeriesCarrouselAdapter(SeriesCarrouselPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View slideLayout = inflater.inflate(R.layout.slider_item, parent, false);

        return new TvBannerComponent(slideLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        presenter.onBindRepositoryRowViewAtPosition(position, (TvBannerView) holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }
}
