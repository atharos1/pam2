package com.tutv.android.ui.tv_poster_list;

public interface TvPosterListView {
    void setLoadingStatus(boolean status);
    void setListName(String listName);
    void finishLoading();
    void showLoadError();
}
