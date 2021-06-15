package com.tutv.android.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series_carrousel.SeriesCarrouselComponent;
import com.tutv.android.ui.tv_poster_list.TvPosterListComponent;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;

public class HomeFragment extends Fragment implements HomeView {
    private HomePresenter presenter;
    private LinearLayout genresLayout;
    private SeriesCarrouselComponent featuredBanner;
    private ProgressBar progressBar;
    private TextView textError;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Container diContainer = ContainerLocator.locateComponent(getContext());
        final SeriesRepository seriesRepository = diContainer.getSeriesRepository();
        final BaseSchedulerProvider schedulerProvider = diContainer.getSchedulerProvider();
        presenter = new HomePresenter(this, seriesRepository, schedulerProvider);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        genresLayout = root.findViewById(R.id.genres_linear_layout);
        progressBar = root.findViewById(R.id.home_progressbar);
        textError = root.findViewById(R.id.home_error);

        featuredBanner = new SeriesCarrouselComponent(getContext());
        featuredBanner.build();
        this.genresLayout.addView(featuredBanner);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter.onViewAttached();
    }

    @Override
    public void onStop() {
        super.onStop();

        presenter.onViewDetached();
    }

    public void createGenreList(int genreId, String genreName) {
        progressBar.setVisibility(View.GONE);

        TvPosterListComponent tvl = new TvPosterListComponent(getContext(), null, genreId, genreName);
        tvl.build();

        this.genresLayout.addView(tvl);
    }

    public void showError() {
        progressBar.setVisibility(View.GONE);
        textError.setVisibility(View.VISIBLE);
    }
}