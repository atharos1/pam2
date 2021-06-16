package com.tutv.android.ui.series_carrousel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

public class SeriesCarrouselComponent extends LinearLayout implements SeriesCarrouselView {
    private final SeriesCarrouselPresenter presenter;
    private boolean buildFinished = false;
    private ViewPager2 sliderPager;
    private TabLayout sliderIndicator;

    public SeriesCarrouselComponent(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.series_carrousel, this, true);

        Container container = ContainerLocator.locateComponent(context);
        SeriesRepository seriesRepository = container.getSeriesRepository();
        BaseSchedulerProvider schedulerProvider = container.getSchedulerProvider();
        presenter = new SeriesCarrouselPresenter(this, seriesRepository, schedulerProvider);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        build();
    }

    public void build() {
        if(buildFinished)
            return;

        buildFinished = true;

        sliderPager = findViewById(R.id.slider_pager);
        sliderIndicator = findViewById((R.id.slider_indicator));

        sliderPager.setAdapter(new SeriesCarrouselAdapter(presenter, getContext()));

        new TabLayoutMediator(sliderIndicator, sliderPager,
                (tab, position) -> {}
        ).attach();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        presenter.onViewAttached();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        presenter.onViewDetached();
    }

    @Override
    public void setLoadingStatus(boolean status) {
        if(!status) {
            sliderPager.getAdapter().notifyDataSetChanged();
        }
    }
}
