package com.tutv.android.search;

import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Network;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.search.SearchPresenter;
import com.tutv.android.ui.search.SearchView;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;
import com.tutv.android.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchPresenterTest {
    private SearchView view;
    private SeriesRepository seriesRepository;

    private SearchPresenter presenter;

    @Before
    public void setup() {
        view = mock(SearchView.class);
        seriesRepository = mock(SeriesRepository.class);
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();

        presenter = new SearchPresenter(view, seriesRepository, schedulerProvider);
    }

    @Test
    public void givenTheViewWasAttachedThenFiltersAreFetched() {
        List<Genre> genres = new ArrayList<>();
        List<Network> networks = new ArrayList<>();

        when(seriesRepository.getGenres()).thenReturn(Single.just(genres));
        when(seriesRepository.getNetworks()).thenReturn(Single.just(networks));

        presenter.onViewAttached();

        verify(view, timeout(1000).atLeastOnce()).setFilters(genres, networks);
    }

    @Test
    public void givenTheViewWasAttachedWhenErrorThenShowError() {
        List<Genre> genres = new ArrayList<>();
        List<Network> networks = new ArrayList<>();

        when(seriesRepository.getGenres()).thenReturn(Single.just(genres));
        when(seriesRepository.getNetworks()).thenReturn(Single.just(networks));

        presenter.onViewAttached();

        verify(view, timeout(1000).atLeastOnce()).setFilters(genres, networks);
    }
}
