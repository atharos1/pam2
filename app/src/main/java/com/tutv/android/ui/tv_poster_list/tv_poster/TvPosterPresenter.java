package com.tutv.android.ui.tv_poster_list.tv_poster;

import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;

import java.lang.ref.WeakReference;

public class TvPosterPresenter {
    private final WeakReference<TvPosterView> view;
    private final SeriesRepository seriesRepository;

    private Series series;

    public TvPosterPresenter(TvPosterView view, SeriesRepository seriesRepository) {
        this.view = new WeakReference<>(view);
        this.seriesRepository = seriesRepository;
    }

    public void bind(Series series) {
        this.series = series;

        if(view.get() != null) {
            view.get().setImageUrl(series.getPosterUrl());
            view.get().setText(series.getName());
        }
    }

    public void onClick() {
        if(view.get() != null)
            view.get().navigateToSeriesPage(series.getId());
    }
}
