package com.tutv.android.ui.tv_poster_list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;


public class TvPosterListComponent extends LinearLayout implements TvPosterListView {
    private TextView listName;
    private RecyclerView listRecycleView;
    private ProgressBar progressBar;
    private ProgressBar scrollProgressBar;
    private TextView textError;
    private final boolean gridLayout;

    private boolean buildFinished = false;
    private final TvPosterListPresenter presenter;

    public TvPosterListComponent(Context context, @Nullable AttributeSet attrs, int genreId, String genreName) {
        super(context, attrs);

        this.gridLayout = false;

        LayoutInflater.from(context).inflate(R.layout.tv_poster_list, this, true);

        Container container = ContainerLocator.locateComponent(context);
        SeriesRepository seriesRepository = container.getSeriesRepository();
        BaseSchedulerProvider schedulerProvider = container.getSchedulerProvider();
        presenter = new TvPosterListPresenter(this, seriesRepository, schedulerProvider, genreId, genreName);
    }

    public TvPosterListComponent(Context context, @Nullable AttributeSet attrs, String query, Integer genre, Integer network) {
        super(context, attrs);

        this.gridLayout = true;

        LayoutInflater.from(context).inflate(R.layout.tv_poster_list, this, true);

        Container container = ContainerLocator.locateComponent(context);
        SeriesRepository seriesRepository = container.getSeriesRepository();
        BaseSchedulerProvider schedulerProvider = container.getSchedulerProvider();
        presenter = new TvPosterListPresenter(this, seriesRepository, schedulerProvider, query, genre, network);
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

        this.listName = findViewById(R.id.list_name);
        this.listRecycleView = findViewById(R.id.list_recyclerview);
        this.progressBar = findViewById(R.id.tv_poster_progressbar);
        this.textError = findViewById(R.id.tv_poster_error);

        if (this.gridLayout) {
            int statusBarHeightId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = getResources().getDimensionPixelSize(statusBarHeightId);
            int actionBarHeight = 0;

            TypedValue tv = new TypedValue();
            if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());

            listRecycleView.setPadding(0, statusBarHeight + actionBarHeight, 0, 160);
            scrollProgressBar = findViewById(R.id.tv_poster_scroll_progressbar_bottom);
            listRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else {
            scrollProgressBar = findViewById(R.id.tv_poster_scroll_progressbar_right);
            listRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            listRecycleView.setPadding(0, 0, 160, 0);
        }

        listRecycleView.setAdapter(new TvPosterListAdapter(presenter, getContext()));
        addOnScrollListener(listRecycleView);
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
    public void setListName(String listName) {
        if (listName == null) {
            this.listName.setVisibility(View.GONE);
        } else {
            this.listName.setVisibility(View.VISIBLE);
            this.listName.setText(listName);
        }
    }

    @Override
    public void setLoadingStatus(boolean status) {
        if(status)
            scrollProgressBar.setVisibility(View.VISIBLE);
        else {
            listRecycleView.getAdapter().notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            scrollProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void finishLoading() {
        listRecycleView.clearOnScrollListeners();
        scrollProgressBar.setVisibility(View.GONE);
        listRecycleView.setPadding(0, listRecycleView.getPaddingTop(), 0, 0);
    }

    @Override
    public void showLoadError() {
        finishLoading();
        progressBar.setVisibility(View.GONE);
        listRecycleView.setVisibility(View.GONE);
        textError.setVisibility(View.VISIBLE);
    }

    private void addOnScrollListener(RecyclerView recycleView) {
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) recycleView.getLayoutManager();

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    presenter.getNextPage();
                }
            }
        });
    }
}
