package com.tutv.android.ui.search;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Network;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.tv_poster_list.TvPosterListComponent;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements com.tutv.android.ui.search.SearchView {
    private SearchPresenter presenter;

    private AlertDialog filterDialog;
    private View filterView;
    private MenuItem searchItem;
    private LinearLayout layout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        filterDialog = createDialog();
        //setFilters(new ArrayList<>(), new ArrayList<>());
        layout = findViewById(R.id.search_layout);

        Container container = ContainerLocator.locateComponent(this);
        SeriesRepository seriesRepository = container.getSeriesRepository();
        presenter = new SearchPresenter(this, seriesRepository);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            presenter.performSearch(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_action_bar, menu);
        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionAndStatusBar)));

        MenuItem searchItem = menu.findItem(R.id.search_button);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });

        this.searchItem = searchItem;

        handleIntent(getIntent());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter_button) {
            filterDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.onViewAttached();
    }

    @Override
    protected void onStop() {
        super.onStop();

        presenter.onViewDetached();
    }

    @Override
    public void setSearchQuery(String searchQuery, Integer genre, Integer network) {
        this.searchItem.expandActionView();
        SearchView searchView = (SearchView) this.searchItem.getActionView();
        searchView.setQuery(searchQuery, false);
        searchView.clearFocus();

        TvPosterListComponent tvl = new TvPosterListComponent(layout.getContext(), null,
                searchQuery, genre, network);
        tvl.build();
        layout.removeAllViews();
        layout.addView(tvl);
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_search, null);

        mBuilder.setTitle(getString(R.string.apply_filters));

        Spinner gSpinner = mView.findViewById(R.id.spinner_genre);
        Spinner nSpinner = mView.findViewById(R.id.spinner_network);

        mBuilder.setPositiveButton("Ok", (dialog, which) -> {
            Genre g = (Genre) gSpinner.getSelectedItem();
            Network n = (Network) nSpinner.getSelectedItem();
            if (g != null && n != null) {
                presenter.applyFilters(g.getId(), n.getId());
            }
        });

        mBuilder.setView(mView);
        filterView = mView;
        return mBuilder.create();
    }

    public void setFilters(List<Genre> genres, List<Network> networks) {
        ProgressBar progressBar = filterView.findViewById(R.id.filter_progressbar);
        TextView gText = filterView.findViewById(R.id.text_genre);
        Spinner gSpinner = filterView.findViewById(R.id.spinner_genre);
        TextView nText = filterView.findViewById(R.id.text_network);
        Spinner nSpinner = filterView.findViewById(R.id.spinner_network);

        progressBar.setVisibility(View.GONE);
        gText.setVisibility(View.VISIBLE);
        gSpinner.setVisibility(View.VISIBLE);
        nText.setVisibility(View.VISIBLE);
        nSpinner.setVisibility(View.VISIBLE);

        genres.add(0, new Genre(-1, getString(R.string.all_genres)));
        ArrayAdapter<Genre> genreAdapter = new ArrayAdapter<>(SearchActivity.this,
                android.R.layout.simple_spinner_item,
                genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gSpinner.setAdapter(genreAdapter);

        networks.add(0, new Network(-1, getString(R.string.all_networks)));
        ArrayAdapter<Network> networkAdapter = new ArrayAdapter<>(SearchActivity.this,
                android.R.layout.simple_spinner_item,
                networks);
        networkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nSpinner.setAdapter(networkAdapter);
    }
}
