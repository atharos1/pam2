package com.tutv.android.ui.tv_poster_list;

import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series.SeriesView;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;

public class TvPosterListPresenter {
    private enum Mode {
        GENRE,
        SEARCH;
    }

    private int genreId;
    private String genreName;
    private String query;
    private Integer genre;
    private Integer network;
    private Mode mode;

    private final WeakReference<TvPosterListView> view;
    private final List<Series> seriesList;
    private final PublishProcessor<Integer> mPublishProcessor;
    private final SeriesRepository seriesRepository;

    private final CompositeDisposable disposables;

    private int pageNumber = 1;
    private boolean loading = true;


    public TvPosterListPresenter(TvPosterListView view, SeriesRepository seriesRepository, int genreId, String genreName) {
        this(view, seriesRepository);
        this.genreId = genreId;
        this.genreName = genreName;
        this.mode = Mode.GENRE;
    }

    public TvPosterListPresenter(TvPosterListView view, SeriesRepository seriesRepository, String query, Integer genre, Integer network) {
        this(view, seriesRepository);
        this.query = query;
        this.genre = genre;
        this.network = network;
        this.mode = Mode.SEARCH;
    }

    private TvPosterListPresenter(TvPosterListView view, SeriesRepository seriesRepository) {
        this.view = new WeakReference<>(view);
        this.seriesRepository = seriesRepository;
        this.mPublishProcessor = PublishProcessor.create();
        this.seriesList = new ArrayList<>();

        disposables = new CompositeDisposable();
    }

    public void onViewAttached() {
        initObservable();

        TvPosterListView actualView = view.get();
        if(actualView != null) {
            actualView.setListName(this.genreName);
        }
    }

    public void onViewDetached() {
        disposables.dispose();
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
                            loading = true;
                            TvPosterListView actualView = view.get();
                            if(actualView != null && seriesList.size() > 0) {
                                actualView.setLoadingStatus(true);
                            }
                        })
                        .concatMapSingle(page -> {
                            switch (mode) {
                                case GENRE:
                                    return seriesRepository.getGenreById(genreId, page);
                                case SEARCH:
                                    return seriesRepository.getSeriesSearch(query, page, genre, network);
                                default:
                                    return null;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onLoad, this::onLoadError);

        disposables.add(disposable);
        mPublishProcessor.onNext(pageNumber);
    }

    private void onLoad(Object o) {
        List<Series> series = null;

        switch (mode) {
            case GENRE:
                Genre genre = (Genre) o;
                series = genre.getSeries();
                break;
            case SEARCH:
                series = (List<Series>) o;
                break;
        }

        seriesList.addAll(series);
        TvPosterListView actualView = view.get();
        loading = false;
        if(actualView != null) {
            actualView.setLoadingStatus(false);
            if (series.size() == 0)
                actualView.finishLoading();
        }
    }

    private void onLoadError(final Throwable e) {
        TvPosterListView actualView = view.get();
        if(actualView != null) {
            actualView.showLoadError();
        }
    }

    public void getNextPage() {
        if (!loading) {
            pageNumber++;
            mPublishProcessor.onNext(pageNumber);
        }
    }

}
