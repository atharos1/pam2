package com.tutv.android.ui.tv_poster_list;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tutv.android.R;
import com.tutv.android.di.Container;
import com.tutv.android.di.ContainerLocator;
import com.tutv.android.domain.Series;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.repository.UserRepository;

import java.util.List;

public class TvPosterListComponent extends LinearLayout implements TvPosterListView {
    private TextView listName;
    private RecyclerView listRecycleView;
    private ProgressBar progressBar;
    private ProgressBar scrollProgressBar;

    private boolean buildFinished = false;
    private TvPosterListPresenter presenter;

    public TvPosterListComponent(Context context, @Nullable AttributeSet attrs, int genreId, String genreName) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.tv_poster_list, this, true);

        Container container = ContainerLocator.locateComponent(context);
        SeriesRepository seriesRepository = container.getSeriesRepository();
        presenter = new TvPosterListPresenter(this, genreId, genreName, seriesRepository);
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
        this.scrollProgressBar = findViewById(R.id.tv_poster_scroll_progressbar);

        listRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

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
        this.listName.setText(listName);
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
