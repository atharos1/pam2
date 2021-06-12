package com.tutv.android.ui.series;

public interface SeriesView {

    void showSeriesName(String seriesName);
    void showSeriesDescription(String seriesDescription);
    void showUserFollows(boolean follows);
    void showFollowerCount(int followers);

}
