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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tutv.android.R;
import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Network;
import com.tutv.android.ui.tv_poster_list.TvPosterListComponent;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView {
    private SearchPresenter presenter;

    private ProgressBar searchProgressBar;
    private AlertDialog filterDialog;
    private View filterView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        searchProgressBar = findViewById(R.id.search_progressbar);
        filterDialog = createDialog();
        //setFilters(new ArrayList<>(), new ArrayList<>());

        LinearLayout layout = findViewById(R.id.search_layout);
        TvPosterListComponent tvl = new TvPosterListComponent(layout.getContext(), null);
        tvl.build();
        layout.addView(tvl);

        presenter = new SearchPresenter();

        handleIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //ToDo: Buscar
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_action_bar, menu);
        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionAndStatusBar)));

        MenuItem searchItem = menu.findItem(R.id.search_button);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        /* searchViewModel.getSearchQuery().observe(this, value -> {
            searchItem.expandActionView();
            searchView.setQuery(value, false);
            searchView.clearFocus();
        }); */
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
    public void setLoadingStatus(boolean status) {
        if(status)
            searchProgressBar.setVisibility(View.VISIBLE);
        else
            searchProgressBar.setVisibility(View.GONE);
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
                // ToDo: applyFilters(g.getId(), n.getId());
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
