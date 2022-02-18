package com.tutv.android.ui.tv_poster_list

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tutv.android.R
import com.tutv.android.di.ContainerLocator
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class TvPosterListComponent : LinearLayout, TvPosterListView {
    private var listName: TextView? = null
    private var listRecycleView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var scrollProgressBar: ProgressBar? = null
    private var textError: TextView? = null
    private val gridLayout: Boolean
    private var buildFinished = false
    private val presenter: TvPosterListPresenter?

    constructor(context: Context?, attrs: AttributeSet?, genreId: Int, genreName: String?) : super(context, attrs) {
        gridLayout = false
        LayoutInflater.from(context).inflate(R.layout.tv_poster_list, this, true)
        val container = ContainerLocator.locateComponent(context)
        val seriesRepository = container.seriesRepository
        val schedulerProvider = container.schedulerProvider
        presenter = TvPosterListPresenter(this, seriesRepository, schedulerProvider, genreId, genreName, 6)
    }

    constructor(context: Context?, attrs: AttributeSet?, query: String?, genre: Int?, network: Int?) : super(context, attrs) {
        gridLayout = true
        LayoutInflater.from(context).inflate(R.layout.tv_poster_list, this, true)
        val container = ContainerLocator.locateComponent(context)
        val seriesRepository = container.seriesRepository
        val schedulerProvider = container.schedulerProvider
        presenter = TvPosterListPresenter(this, seriesRepository, schedulerProvider, query, genre, network, 18)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        build()
    }

    fun build() {
        if (buildFinished) return
        buildFinished = true
        listName = findViewById<TextView?>(R.id.list_name)
        listRecycleView = findViewById(R.id.list_recyclerview)
        progressBar = findViewById<ProgressBar?>(R.id.tv_poster_progressbar)
        textError = findViewById<TextView?>(R.id.tv_poster_error)
        if (gridLayout) {
            val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
            val statusBarHeight = resources.getDimensionPixelSize(statusBarHeightId)
            var actionBarHeight = 0
            val tv = TypedValue()
            if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            listRecycleView.setPadding(0, statusBarHeight + actionBarHeight, 0, 160)
            scrollProgressBar = findViewById<ProgressBar?>(R.id.tv_poster_scroll_progressbar_bottom)
            listRecycleView.setLayoutManager(GridLayoutManager(context, 3))
        } else {
            scrollProgressBar = findViewById<ProgressBar?>(R.id.tv_poster_scroll_progressbar_right)
            listRecycleView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))
            listRecycleView.setPadding(0, 0, 160, 0)
        }
        listRecycleView.setAdapter(TvPosterListAdapter(presenter, context))
        addOnScrollListener(listRecycleView)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onViewAttached()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onViewDetached()
    }

    override fun setListName(listName: String?) {
        if (listName == null) {
            this.listName.setVisibility(View.GONE)
        } else {
            this.listName.setVisibility(View.VISIBLE)
            this.listName.setText(listName)
        }
    }

    override fun setLoadingStatus(status: Boolean) {
        if (status) scrollProgressBar.setVisibility(View.VISIBLE) else {
            listRecycleView.getAdapter().notifyDataSetChanged()
            progressBar.setVisibility(View.GONE)
            scrollProgressBar.setVisibility(View.GONE)
        }
    }

    override fun notifyEndReached() {
        listRecycleView.clearOnScrollListeners()
        scrollProgressBar.setVisibility(View.GONE)
        listRecycleView.setPadding(0, listRecycleView.getPaddingTop(), 0, 0)
    }

    override fun showLoadError() {
        notifyEndReached()
        progressBar.setVisibility(View.GONE)
        listRecycleView.setVisibility(View.GONE)
        textError.setVisibility(View.VISIBLE)
    }

    private fun addOnScrollListener(recycleView: RecyclerView?) {
        val mLayoutManager = recycleView.getLayoutManager() as LinearLayoutManager?
        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val visibleItemCount = mLayoutManager.getChildCount()
                val totalItemCount = mLayoutManager.getItemCount()
                val pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                    presenter.getNextPage()
                }
            }
        })
    }
}