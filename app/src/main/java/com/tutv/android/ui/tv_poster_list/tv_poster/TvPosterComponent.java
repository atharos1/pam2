package com.tutv.android.ui.tv_poster_list.tv_poster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.ui.login.LoginPresenter;
import com.tutv.android.ui.series.SeriesActivity;

import org.jetbrains.annotations.NotNull;

public class TvPosterComponent extends RecyclerView.ViewHolder implements TvPosterView {
    TextView title;
    ImageView image;

    private TvPosterPresenter presenter;

    public TvPosterComponent(@NonNull @NotNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.tv_poster_title);
        image = itemView.findViewById(R.id.tv_poster_image);

        Container container = ContainerLocator.locateComponent(itemView.getContext());
        final SeriesRepository seriesRepository = container.getSeriesRepository();
        presenter = new TvPosterPresenter(this, seriesRepository);

        itemView.setOnClickListener(v -> presenter.onClick());
    }

    @Override
    public void setImageUrl(String imageUrl) {
        Picasso.get().load("https://image.tmdb.org/t/p/original" + imageUrl).fit()
                //.placeholder(R.drawable.user_placeholder)
                .into(image);
    }

    @Override
    public void setText(String text) {
        title.setText(text);
    }

    @Override
    public TvPosterPresenter getPresenter() {
        return null;
    }

    public void navigateToSeriesPage(int seriesId) {
        Intent intent = new Intent(itemView.getContext(), SeriesActivity.class);
        intent.putExtra("series_id", seriesId);
        itemView.getContext().startActivity(intent);
    }
}
