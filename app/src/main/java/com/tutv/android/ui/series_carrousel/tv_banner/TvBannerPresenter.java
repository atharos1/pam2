package com.tutv.android.ui.series_carrousel.tv_banner;

import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView;

import java.lang.ref.WeakReference;

public class TvBannerPresenter {
    private final WeakReference<TvBannerView> view;
    private final SeriesRepository seriesRepository;

    private Series series;

    public TvBannerPresenter(TvBannerView view, SeriesRepository seriesRepository) {
        this.view = new WeakReference<>(view);
        this.seriesRepository = seriesRepository;
    }

    //TODO que tan terrible es usar strings asi nomas?
    public void bind(Series series) {
        this.series = series;

        if(view.get() != null) {
            view.get().setImageUrl(series.getBannerUrl());
            view.get().setTitle(series.getName());
            view.get().setSubtitle(series.getFollowers() + " followers");
        }
    }

    public void onClick() {
        if(view.get() != null)
            view.get().navigateToSeriesPage(series.getId());
    }
}
