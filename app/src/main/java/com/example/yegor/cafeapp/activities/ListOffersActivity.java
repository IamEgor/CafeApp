package com.example.yegor.cafeapp.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.adapters.ListOffersAdapter;
import com.example.yegor.cafeapp.exceptions.NoConnectionException;
import com.example.yegor.cafeapp.loader.AsyncLoader;
import com.example.yegor.cafeapp.models.Category;
import com.example.yegor.cafeapp.models.ContentWrapper;
import com.example.yegor.cafeapp.models.Offer;

import java.util.ArrayList;
import java.util.List;

public class ListOffersActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<ContentWrapper<List<Offer>>> {

    private RecyclerView rv;
    private View loadingView, errorView;
    private TextView errorMessage;

    private ListOffersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv = (RecyclerView) findViewById(R.id.rv);
        loadingView = findViewById(R.id.loading_view);
        errorView = findViewById(R.id.error_view);
        errorMessage = (TextView) findViewById(R.id.error_message);

        adapter = new ListOffersAdapter(this, new ArrayList<Offer>(0));

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        findViewById(R.id.retry_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager()
                        .restartLoader(0, getIntent().getExtras(), ListOffersActivity.this)
                        .forceLoad();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getLoaderManager().initLoader(0, getIntent().getExtras(), this).forceLoad();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_offers;
    }

    @Override
    public Loader<ContentWrapper<List<Offer>>> onCreateLoader(int id, Bundle args) {
        setStatus(Status.LOADING);
        return new AsyncLoader(this, args.getInt(Category.ID_EXTRA));
    }

    @Override
    public void onLoadFinished(Loader<ContentWrapper<List<Offer>>> loader,
                               ContentWrapper<List<Offer>> data) {

        if (data.getException() == null && data.getContent() != null) {
            adapter.setModels(data.getContent());
            setStatus(Status.OK);
        } else if (data.getException() instanceof NoConnectionException) {
            errorMessage.setText(data.getException().getMessage());
            setStatus(Status.FAILED);
        } else
            throw new RuntimeException("[Unknown exception] " + data.getException().getMessage());

    }

    @Override
    public void onLoaderReset(Loader<ContentWrapper<List<Offer>>> loader) {
        adapter.setModels(new ArrayList<Offer>(0));
    }

    private void setStatus(Status status) {

        switch (status) {
            case LOADING:
                rv.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                break;
            case OK:
                errorView.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                break;
            case FAILED:
                loadingView.setVisibility(View.GONE);
                rv.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                break;
        }

    }

    private enum Status {
        LOADING, OK, FAILED
    }


}