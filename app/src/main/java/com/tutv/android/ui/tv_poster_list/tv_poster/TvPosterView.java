package com.tutv.android.ui.tv_poster_list.tv_poster;

public interface TvPosterView {
    void setImageUrl(String imageUrl);

    void setText(String text);

    TvPosterPresenter getPresenter();

    void navigateToSeriesPage(int seriesId);
}
