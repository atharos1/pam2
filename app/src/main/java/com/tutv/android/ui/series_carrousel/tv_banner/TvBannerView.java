package com.tutv.android.ui.series_carrousel.tv_banner;

public interface TvBannerView {
    void setImageUrl(String imageUrl);

    void setTitle(String text);

    void setSubtitle(String text);

    TvBannerPresenter getPresenter();

    void navigateToSeriesPage(int seriesId);
}
