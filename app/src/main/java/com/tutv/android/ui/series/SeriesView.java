package com.tutv.android.ui.series;

import com.tutv.android.domain.Season;

import java.util.List;

public interface SeriesView {

    void showSeriesName(String seriesName);
    void showSeriesDescription(String seriesDescription);
    void showUserFollows(boolean follows);
    void showFollowerCount(int followers);
    void bindSeasons(final List<Season> seasonList);

}
