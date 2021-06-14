package com.tutv.android.ui.tv_poster_list;

import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series.SeriesView;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;

public class TvPosterListPresenter {
    private final int genreId;
    private final WeakReference<TvPosterListView> view;
    private List<Series> seriesList;
    private PublishProcessor<Integer> mPublishProcessor;
    private int pageNumber = 1;
    private SeriesRepository seriesRepository;
    private boolean loading = true;

    public TvPosterListPresenter(TvPosterListView view, int genreId, SeriesRepository seriesRepository) {
        this.view = new WeakReference<>(view);
        this.genreId = genreId;
        this.seriesRepository = seriesRepository;
        this.mPublishProcessor = PublishProcessor.create();
        this.seriesList = new ArrayList<>();

        initObservable();
    }

    public void onBindRepositoryRowViewAtPosition(int position, TvPosterView tvPosterView) {
        Series series = seriesList.get(position);
        tvPosterView.getPresenter().bind(series);
    }

    public int getItemCount() {
        return seriesList.size();
    }

    private void initObservable() {
        Disposable disposable =
                mPublishProcessor
                        .doOnNext(page -> {
                            TvPosterListView actualView = view.get();
                            loading = true;
                            if(actualView != null) {
                                actualView.setLoadingStatus(true);
                            }
                        })
                        .concatMapSingle(page -> seriesRepository.getGenreById(genreId, page))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(genre -> {
                            seriesList.addAll(genre.getSeries());
                            System.out.println("asds");
                            TvPosterListView actualView = view.get();
                            loading = false;
                            if(actualView != null) {
                                actualView.setLoadingStatus(false);
                            }
                        });

        // disposables.add(disposable);
        mPublishProcessor.onNext(pageNumber);
    }

    public void getNextPage() {
        if (!loading) {
            pageNumber++;
            mPublishProcessor.onNext(pageNumber);
        }
    }
}
