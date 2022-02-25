package com.tutv.android.series_carrousel;

import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series_carrousel.SeriesCarrouselPresenter;
import com.tutv.android.ui.series_carrousel.SeriesCarrouselView;
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
import static org.junit.Assert.*;

public class SeriesCarrouselPresenterTest {
    private SeriesCarrouselView view;
    private SeriesRepository seriesRepository;

    private List<Series> series;

    private SeriesCarrouselPresenter presenter;

    @Before
    public void setup() {
        view = mock(SeriesCarrouselView.class);
        seriesRepository = mock(SeriesRepository.class);
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();

        series = new ArrayList<>();
        series.add(new Series());

        presenter = new SeriesCarrouselPresenter(view, seriesRepository, schedulerProvider);
    }

    @Test
    public void givenTheViewWasAttachedThenLoadSuccessful() throws InterruptedException {
        when(seriesRepository.getFeatured()).thenReturn(Single.just(series));

        presenter.onViewAttached();

        verify(view, timeout(1000).times(1)).setLoadingStatus(false);
        assertEquals(presenter.getItemCount(), 1);
    }

}
