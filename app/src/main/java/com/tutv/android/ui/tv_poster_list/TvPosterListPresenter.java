package com.tutv.android.ui.tv_poster_list;

import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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
    private final BaseSchedulerProvider schedulerProvider;

    private final CompositeDisposable disposables;

    private int pageSize = 6;

    private boolean loading = true;

    private boolean reachedEnd = false;


    public TvPosterListPresenter(TvPosterListView view, SeriesRepository seriesRepository,
                                 BaseSchedulerProvider schedulerProvider, int genreId, String genreName, int pageSize) {
        this(view, seriesRepository, schedulerProvider);
        this.genreId = genreId;
        this.genreName = genreName;
        this.pageSize = pageSize;
        this.mode = Mode.GENRE;
    }

    public TvPosterListPresenter(TvPosterListView view, SeriesRepository seriesRepository,
                                 BaseSchedulerProvider schedulerProvider, String query, Integer genre, Integer network, int pageSize) {
        this(view, seriesRepository, schedulerProvider);
        this.query = query;
        this.genre = genre;
        this.network = network;
        this.pageSize = pageSize;
        this.mode = Mode.SEARCH;
    }

    private TvPosterListPresenter(TvPosterListView view, SeriesRepository seriesRepository, BaseSchedulerProvider schedulerProvider) {
        this.view = new WeakReference<>(view);
        this.seriesList = new ArrayList<>();
        this.seriesRepository = seriesRepository;
        this.schedulerProvider = schedulerProvider;
        this.mPublishProcessor = PublishProcessor.create();

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
        Disposable disposable;

        switch(mode) {
            case GENRE:
                disposable =
                        mPublishProcessor
                                .doOnNext(this::onNext)
                                .concatMapSingle(page -> seriesRepository.getGenreById(genreId, page, pageSize))
                                .observeOn(schedulerProvider.ui())
                                .subscribe(this::onLoadGenre, this::onLoadError);
                break;
            case SEARCH:
                disposable =
                        mPublishProcessor
                                .doOnNext(this::onNext)
                                .concatMapSingle(page -> seriesRepository.getSeriesSearch(query, page, genre, network, 18))
                                .observeOn(schedulerProvider.ui())
                                .subscribe(this::onLoadSeries, this::onLoadError);
                break;
            default:
                return;
        }

        disposables.add(disposable);
        mPublishProcessor.onNext(getPageNumber() + 1);
    }

    private void onNext(Integer page) {
        loading = true;
        TvPosterListView actualView = view.get();
        if(actualView != null && seriesList.size() > 0) {
            actualView.setLoadingStatus(true);
        }
    }

    private void onLoadGenre(Genre genre) {
        this.onLoadSeries(genre.getSeries());
    }

    private void onLoadSeries(List<Series> series) {
        if(series.size() < pageSize)
            reachedEnd = true;

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
        if (!loading && !reachedEnd) {
            mPublishProcessor.onNext(getPageNumber() + 1);
        }
    }

    private int getPageNumber() {
        return seriesList.size() / pageSize;
    }

}
