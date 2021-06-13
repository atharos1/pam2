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
import com.tutv.android.domain.Series;

import java.util.List;

public class TvPosterListComponent extends LinearLayout implements TvPosterListView {
    private TextView listName;
    private RecyclerView listRecycleView;
    private ProgressBar progressBar;
    private ProgressBar scrollProgressBar;

    private final Context context;
    private boolean buildFinished = false;
    private TvPosterListPresenter presenter;

    public TvPosterListComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.tv_poster_list, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if(buildFinished)
            return;

        buildFinished = true;

        this.listName = findViewById(R.id.list_name);
        this.listRecycleView = findViewById(R.id.list_recyclerview);
        this.progressBar = findViewById(R.id.tv_poster_progressbar);
        this.scrollProgressBar = findViewById(R.id.tv_poster_scroll_progressbar);

        listRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        presenter = new TvPosterListPresenter(this, "drama");

        listRecycleView.setAdapter(new TvPosterListAdapter(presenter, getContext()));
    }
}
