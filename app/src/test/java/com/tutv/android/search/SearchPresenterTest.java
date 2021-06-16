package com.tutv.android.search;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
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
    public void givenTheViewWasAttachedWhenGenreErrorThenShowError() {
        List<Network> networks = new ArrayList<>();

        when(seriesRepository.getGenres()).thenReturn(Single.error(new Throwable()));
        when(seriesRepository.getNetworks()).thenReturn(Single.just(networks));

        presenter.onViewAttached();

        verify(view, timeout(1000).atLeastOnce()).showFilterError();
    }

    @Test
    public void givenTheViewWasAttachedWhenNetworkErrorThenShowError() {
        List<Genre> genres = new ArrayList<>();

        when(seriesRepository.getGenres()).thenReturn(Single.just(genres));
        when(seriesRepository.getNetworks()).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();

        verify(view, timeout(1000).atLeastOnce()).showFilterError();
    }

    @Test
    public void givenASearchWasPerformedThenSetSearchQuery() {
        String searchQuery = "a search query";

        presenter.performSearch(searchQuery);

        verify(view, times(1)).setSearchQuery(searchQuery, null, null);
    }

    @Test
    public void givenASearchWasPerformedWhenQueryStaysTheSameThenDontSetSearchQuery() {
        String searchQuery = "a search query";

        presenter.performSearch(searchQuery);

        verify(view, times(1)).setSearchQuery(searchQuery, null, null);

        /* should not call setSearchQuery again if the query stays the same */
        presenter.performSearch(searchQuery);

        verify(view, times(1)).setSearchQuery(any(), any(), any());
    }

    @Test
    public void givenASearchWasPerformedWhenQueryChangesThenSetSearchQuery() {
        String searchQuery = "a search query";
        String anotherSearchQuery = "another search query";

        presenter.performSearch(searchQuery);

        verify(view, times(1)).setSearchQuery(searchQuery, null, null);

        /* should call setSearchQuery again since the query changed */
        presenter.performSearch(anotherSearchQuery);

        verify(view, times(2)).setSearchQuery(any(), any(), any());
    }

    @Test
    public void givenFiltersAreAppliedThenSetSearchQuery() {
        int genre = 1;
        int network = 1;

        presenter.applyFilters(genre, network);

        verify(view, times(1)).setSearchQuery(null, genre, network);
    }

    @Test
    public void givenFiltersAreAppliedWhenFiltersStayTheSameThenDontSetSearchQuery() {
        int genre = 1;
        int network = 1;

        presenter.applyFilters(genre, network);

        verify(view, times(1)).setSearchQuery(null, genre, network);

        /* should not call setSearchQuery again if filters stay the same */
        presenter.applyFilters(genre, network);

        verify(view, times(1)).setSearchQuery(any(), any(), any());
    }

    @Test
    public void givenFiltersAreAppliedWhenFiltersChangeTheSameThenSetSearchQuery() {
        int genre = 1;
        int network = 1;

        presenter.applyFilters(genre, network);

        verify(view, times(1)).setSearchQuery(null, genre, network);

        /* should call setSearchQuery again if at least one filter changed */
        genre = 2;
        network = 1;
        presenter.applyFilters(genre, network);

        verify(view, times(2)).setSearchQuery(any(), any(), any());

        genre = 2;
        network = 2;
        presenter.applyFilters(genre, network);

        verify(view, times(3)).setSearchQuery(any(), any(), any());
    }

    @Test
    public void givenFiltersAreAppliedWhenFiltersAreNullThenSetSearchQuery() {
        int genre = 1;
        int network = 1;

        presenter.applyFilters(genre, network);

        verify(view, times(1)).setSearchQuery(null, genre, network);

        /* should get null instead of -1 */
        genre = -1;
        network = -1;

        presenter.applyFilters(genre, network);

        verify(view).setSearchQuery(null, null, null);
    }
}
