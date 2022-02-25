package com.tutv.android.home;

import com.tutv.android.domain.Genre;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.home.HomePresenter;
import com.tutv.android.ui.home.HomeView;
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

public class HomePresenterTest {
    private HomeView view;
    private SeriesRepository seriesRepository;

    private List<Genre> genres;
    private final int genreId1 = 1;
    private final String genreName1 = "Genre 1";
    private final int genreId2 = 2;
    private final String genreName2 = "Genre 2";

    private HomePresenter presenter;

    @Before
    public void setup() {
        view = mock(HomeView.class);
        seriesRepository = mock(SeriesRepository.class);
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();

        genres = new ArrayList<>();
        genres.add(new Genre(genreId1, genreName1));
        genres.add(new Genre(genreId2, genreName2));

        presenter = new HomePresenter(view, seriesRepository, schedulerProvider);
    }

    @Test
    public void givenTheViewWasAttachedThenCreateGenreList() {
        when(seriesRepository.getGenres()).thenReturn(Single.just(genres));

        presenter.onViewAttached();

        verify(view, timeout(1000).times(1)).createGenreList(genreId1, genreName1);
        verify(view, timeout(1000).times(1)).createGenreList(genreId2, genreName2);
    }

    @Test
    public void givenTheViewWasAttachedWhenNetworkErrorThenShowError() {
        when(seriesRepository.getGenres()).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();

        verify(view, timeout(1000).times(1)).showError();
    }
}
