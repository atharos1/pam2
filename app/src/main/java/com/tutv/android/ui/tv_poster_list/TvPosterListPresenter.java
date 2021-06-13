package com.tutv.android.ui.tv_poster_list;

import com.tutv.android.domain.Series;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView;

import java.lang.ref.WeakReference;
import java.util.List;

public class TvPosterListPresenter {
    private final String genreName;
    private final WeakReference<TvPosterListView> view;
    private List<Series> seriesList;

    public TvPosterListPresenter(TvPosterListView view, String genreName) {
        this.view = new WeakReference<>(view);
        this.genreName = genreName;

        //TODO inicializar seriesList con apiCall
    }

    public void onBindRepositoryRowViewAtPosition(int position, TvPosterView tvPosterView) {
        Series series = seriesList.get(position);
        tvPosterView.getPresenter().bind(series);
    }

    public int getItemCount() {
        return seriesList.size();
    }
}
