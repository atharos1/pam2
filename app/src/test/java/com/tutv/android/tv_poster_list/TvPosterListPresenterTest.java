package com.tutv.android.tv_poster_list;

import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.tv_poster_list.TvPosterListPresenter;
import com.tutv.android.ui.tv_poster_list.TvPosterListView;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterPresenter;
import com.tutv.android.ui.tv_poster_list.tv_poster.TvPosterView;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;
import com.tutv.android.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class TvPosterListPresenterTest {
    TvPosterListView view;
    private SeriesRepository seriesRepository;

    private final int genreId = 1;
    private final String genreName = "Genre";
    private final int pageSize = 10;
    private Genre genreFirstPage;
    private Genre genreSecondPage;
    private Genre genreEmptyPage;

    private TvPosterListPresenter presenter;

    @Before
    public void setup() {
        view = mock(TvPosterListView.class);
        seriesRepository = mock(SeriesRepository.class);
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();

        genreFirstPage = new Genre(genreId, genreName);
        genreSecondPage = new Genre(genreId, genreName);
        genreEmptyPage = new Genre(genreId, genreName);
        List<Series> seriesFirstPage = new ArrayList<>();
        List<Series> seriesSecondPage = new ArrayList<>();
        List<Series> seriesEmptyPage = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            seriesFirstPage.add(new Series());
            seriesSecondPage.add(new Series());
        }
        genreFirstPage.setSeries(seriesFirstPage);
        genreSecondPage.setSeries(seriesSecondPage);
        genreEmptyPage.setSeries(seriesEmptyPage);

        Genre genrePage2 = new Genre(genreId, genreName);
        List<Series> seriesPage2 = new ArrayList<>();
        for (int i = 0; i < pageSize; i++)
            seriesPage2.add(new Series());
        genrePage2.setSeries(seriesPage2);



        presenter = new TvPosterListPresenter(view, seriesRepository, schedulerProvider,
                genreId, genreName, pageSize);
    }

    @Test
    public void givenTheViewWasAttachedThenSetListName() {
        presenter.onViewAttached();

        verify(view).setListName(genreName);
    }

    @Test
    public void givenTheViewWasAttachedThenSeriesAreFetched() {
        when(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage));

        presenter.onViewAttached();

        verify(seriesRepository, timeout(1000).times(1)).getGenreById(genreId, 1, pageSize);
        verify(view, timeout(1000).times(1)).setLoadingStatus(false);
    }

    @Test
    public void givenNextPageIsRequestedThenLoadNextPage() {
        when(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage));
        when(seriesRepository.getGenreById(genreId, 2, pageSize)).thenReturn(Single.just(genreSecondPage));

        presenter.onViewAttached();
        presenter.getNextPage();

        verify(seriesRepository, timeout(1000).times(1)).getGenreById(genreId, 1, pageSize);
        verify(seriesRepository, timeout(1000).times(1)).getGenreById(genreId, 2, pageSize);
        verify(view, timeout(1000).times(1)).setLoadingStatus(true);
        verify(view, timeout(1000).times(2)).setLoadingStatus(false);
    }

    @Test
    public void givenLastPageReachedThenNotifyEndReached() {
        when(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage));
        when(seriesRepository.getGenreById(genreId, 2, pageSize)).thenReturn(Single.just(genreEmptyPage));

        presenter.onViewAttached();
        presenter.getNextPage();

        verify(view, timeout(1000).times(1)).notifyEndReached();
    }

    @Test
    public void givenViewWasAttachedWhenNetworkErrorThenShowError() {
        when(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();

        verify(view, timeout(1000).times(1)).showLoadError();
    }

    @Test
    public void givenNextPageIsRequestedWhenNetworkErrorThenShowError() {
        when(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage));
        when(seriesRepository.getGenreById(genreId, 2, pageSize)).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();
        presenter.getNextPage();

        verify(view, timeout(1000).times(1)).setLoadingStatus(false);
        verify(view, timeout(1000).times(1)).showLoadError();
    }

    @Test
    public void givenViewIsAttachedThenLoadTheCorrectSeries() {
        when(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage));

        TvPosterView tvPosterView = mock(TvPosterView.class);
        TvPosterPresenter tvPosterPresenter = mock(TvPosterPresenter.class);

        when(tvPosterView.getPresenter()).thenReturn(tvPosterPresenter);

        presenter.onViewAttached();

        InOrder inOrder = inOrder(tvPosterPresenter);

        for (int i = 0; i < genreFirstPage.getSeries().size(); i++) {
            presenter.onBindRepositoryRowViewAtPosition(i, tvPosterView);
            inOrder.verify(tvPosterPresenter).bind(genreFirstPage.getSeries().get(i));
        }
        verifyNoMoreInteractions(tvPosterPresenter);
    }

    @Test
    public void givenNextPageIsRequestedThenPagesAreAppendedInCorrectOrder() {
        when(seriesRepository.getGenreById(genreId, 1, pageSize)).thenReturn(Single.just(genreFirstPage));
        when(seriesRepository.getGenreById(genreId, 2, pageSize)).thenReturn(Single.just(genreSecondPage));

        TvPosterView tvPosterView = mock(TvPosterView.class);
        TvPosterPresenter tvPosterPresenter = mock(TvPosterPresenter.class);

        when(tvPosterView.getPresenter()).thenReturn(tvPosterPresenter);

        presenter.onViewAttached();
        presenter.getNextPage();

        int sizeFirstPage = genreFirstPage.getSeries().size();
        int sizeSecondPage = genreSecondPage.getSeries().size();

        InOrder inOrder = inOrder(tvPosterPresenter);

        for (int i = 0; i < sizeFirstPage; i++) {
            presenter.onBindRepositoryRowViewAtPosition(i, tvPosterView);
            inOrder.verify(tvPosterPresenter).bind(genreFirstPage.getSeries().get(i));
        }
        for (int j = 0; j < sizeSecondPage; j++) {
            presenter.onBindRepositoryRowViewAtPosition(sizeFirstPage + j, tvPosterView);
            inOrder.verify(tvPosterPresenter).bind(genreSecondPage.getSeries().get(j));
        }
        verifyNoMoreInteractions(tvPosterPresenter);
    }
}
