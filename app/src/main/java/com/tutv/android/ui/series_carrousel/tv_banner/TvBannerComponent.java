package com.tutv.android.ui.series_carrousel.tv_banner;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series.SeriesActivity;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import org.jetbrains.annotations.NotNull;

public class TvBannerComponent extends RecyclerView.ViewHolder implements TvBannerView {
    private final TvBannerPresenter presenter;

    private final TextView title;
    private final TextView subtitle;
    private final ImageView image;
    private final FloatingActionButton followButton;

    public TvBannerComponent(@NonNull @NotNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.slide_title);
        subtitle = itemView.findViewById(R.id.slide_subtitle);
        image = itemView.findViewById(R.id.slide_image);
        followButton = itemView.findViewById(R.id.slide_follow_button);

        Container container = ContainerLocator.locateComponent(itemView.getContext());
        final SeriesRepository seriesRepository = container.getSeriesRepository();
        BaseSchedulerProvider schedulerProvider = container.getSchedulerProvider();
        presenter = new TvBannerPresenter(this, seriesRepository, schedulerProvider);

        itemView.setOnClickListener(v -> presenter.onClick());
        followButton.setOnClickListener(event -> presenter.onSeriesFollowClicked());
    }

    @Override
    public void setImageUrl(String imageUrl) {
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + imageUrl).fit().into(image);
    }

    @Override
    public void setTitle(String text) {
        title.setText(text);
    }

    @Override
    public void setSubtitle(String text) {
        subtitle.setText(text);
    }

    @Override
    public TvBannerPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void navigateToSeriesPage(int seriesId) {
        Intent intent = new Intent(itemView.getContext(), SeriesActivity.class);
        intent.putExtra("series_id", seriesId);
        itemView.getContext().startActivity(intent);
    }

    @Override
    public void onDetach() {
        presenter.onViewDetached();
    }

    @Override
    public void setSeriesFollowed(boolean followed) {
        if(followed) {
            followButton.setImageResource(R.drawable.ic_star_filled);
        } else {
            followButton.setImageResource(R.drawable.ic_star_empty);
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
