package com.tutv.android.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series_carrousel.SeriesCarrouselComponent;
import com.tutv.android.ui.tv_poster_list.TvPosterListComponent;

public class HomeFragment extends Fragment implements HomeView {
    private HomePresenter presenter;
    private LinearLayout genresLayout;
    private SeriesCarrouselComponent featuredBanner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Container diContainer = ContainerLocator.locateComponent(getContext());
        final SeriesRepository seriesRepository = diContainer.getSeriesRepository();
        presenter = new HomePresenter(this, seriesRepository);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        genresLayout = (LinearLayout) root.findViewById(R.id.genres_linear_layout);

        featuredBanner = new SeriesCarrouselComponent(getContext());
        featuredBanner.build();
        this.genresLayout.addView(featuredBanner);

        return root;
    }

    public void createGenreList(int genreId, String genreName) {
        TvPosterListComponent tvl = new TvPosterListComponent(getContext(), null, genreId, genreName);
        tvl.build();

        this.genresLayout.addView(tvl);
    }
}