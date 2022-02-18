package com.tutv.android.ui.search

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator
import com.tutv.android.domain.Genre
import com.tutv.android.domain.Network
import com.tutv.android.ui.tv_poster_list.TvPosterListComponent
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class SearchActivity : AppCompatActivity(), SearchView {
    private var presenter: SearchPresenter? = null
    private var filterDialog: AlertDialog? = null
    private var filterView: View? = null
    private var searchItem: MenuItem? = null
    private var layout: LinearLayout? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        filterDialog = createDialog()
        layout = findViewById<LinearLayout?>(R.id.search_layout)
        val container = ContainerLocator.locateComponent(this)
        val seriesRepository = container.seriesRepository
        val schedulerProvider = container.schedulerProvider
        presenter = SearchPresenter(this, seriesRepository, schedulerProvider)
    }

    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (Intent.ACTION_SEARCH == intent.getAction()) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            presenter.performSearch(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_action_bar, menu)
        supportActionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.actionAndStatusBar)))
        val searchItem = menu.findItem(R.id.search_button)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = getString(R.string.search_hint)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                finish()
                return false
            }
        })
        this.searchItem = searchItem
        handleIntent(intent)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item.getItemId() == R.id.filter_button) {
            filterDialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached()
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun setSearchQuery(searchQuery: String?, genre: Int?, network: Int?) {
        searchItem.expandActionView()
        val searchView = searchItem.getActionView() as androidx.appcompat.widget.SearchView
        searchView.setQuery(searchQuery, false)
        searchView.clearFocus()
        val tvl = TvPosterListComponent(layout.getContext(), null,
                searchQuery, genre, network)
        tvl.build()
        layout.removeAllViews()
        layout.addView(tvl)
    }

    private fun createDialog(): AlertDialog? {
        val mBuilder = AlertDialog.Builder(this@SearchActivity)
        val mView = layoutInflater.inflate(R.layout.dialog_search, null)
        mBuilder.setTitle(getString(R.string.apply_filters))
        val gSpinner = mView.findViewById<Spinner?>(R.id.spinner_genre)
        val nSpinner = mView.findViewById<Spinner?>(R.id.spinner_network)
        mBuilder.setPositiveButton("Ok") { dialog: DialogInterface?, which: Int ->
            val g = gSpinner.getSelectedItem() as Genre
            val n = nSpinner.getSelectedItem() as Network
            if (g != null && n != null) {
                presenter.applyFilters(g.id, n.id)
            }
        }
        mBuilder.setView(mView)
        filterView = mView
        return mBuilder.create()
    }

    override fun setFilters(genres: MutableList<Genre?>?, networks: MutableList<Network?>?) {
        val progressBar = filterView.findViewById<ProgressBar?>(R.id.filter_progressbar)
        val gText = filterView.findViewById<TextView?>(R.id.text_genre)
        val gSpinner = filterView.findViewById<Spinner?>(R.id.spinner_genre)
        val nText = filterView.findViewById<TextView?>(R.id.text_network)
        val nSpinner = filterView.findViewById<Spinner?>(R.id.spinner_network)
        progressBar.setVisibility(View.GONE)
        gText.setVisibility(View.VISIBLE)
        gSpinner.setVisibility(View.VISIBLE)
        nText.setVisibility(View.VISIBLE)
        nSpinner.setVisibility(View.VISIBLE)
        genres.add(0, Genre(-1, getString(R.string.all_genres)))
        val genreAdapter = ArrayAdapter(this@SearchActivity,
                android.R.layout.simple_spinner_item,
                genres)
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gSpinner.setAdapter(genreAdapter)
        networks.add(0, Network(-1, getString(R.string.all_networks)))
        val networkAdapter = ArrayAdapter(this@SearchActivity,
                android.R.layout.simple_spinner_item,
                networks)
        networkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nSpinner.setAdapter(networkAdapter)
    }

    override fun showFilterError() {
        val progressBar = filterView.findViewById<ProgressBar?>(R.id.filter_progressbar)
        val eText = filterView.findViewById<TextView?>(R.id.filter_error)
        progressBar.setVisibility(View.GONE)
        eText.setVisibility(View.VISIBLE)
    }
}