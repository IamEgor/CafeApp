package com.example.yegor.cafeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.Utils;
import com.example.yegor.cafeapp.databinding.ItemOffersBinding;
import com.example.yegor.cafeapp.loader.ApiEndpointInterface;
import com.example.yegor.cafeapp.models.CategoryModel;
import com.example.yegor.cafeapp.models.OfferModel;
import com.example.yegor.cafeapp.models.YmlCatalogModel;
import com.example.yegor.cafeapp.storage.MySQLiteClass;
import com.minimize.android.rxrecycleradapter.RxDataSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListOffersActivity extends BaseActivity {
    //implements LoaderManager.LoaderCallbacks<ContentWrapper<List<OfferModel>>> {

    private RecyclerView rv;
    private View loadingView, errorView;
    private TextView errorMessage;

    //private ListOffersAdapter adapter;

    public ListOffersActivity() {
        super(R.layout.activity_list_offers);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv = (RecyclerView) findViewById(R.id.rv);
        loadingView = findViewById(R.id.loading_view);
        errorView = findViewById(R.id.error_view);
        errorMessage = (TextView) findViewById(R.id.error_message);

        rv.setLayoutManager(new LinearLayoutManager(this));
        /*
        adapter = new ListOffersAdapter(this, new ArrayList<>(0));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new ListDecorator(getResources().getDimension(R.dimen.offers_card_margin)));

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        findViewById(R.id.retry_btn).setOnClickListener(v ->
                getLoaderManager()
                        .restartLoader(0, getIntent().getExtras(), ListOffersActivity.this)
                        .forceLoad());

        getLoaderManager().initLoader(0, getIntent().getExtras(), this).forceLoad();
        */
        setRx(getIntent().getExtras().getInt(CategoryModel.ID_EXTRA));
    }

    private void setRx(int catId) {


        //rx binding
        Map<Integer, Integer> map = (new MySQLiteClass(ListOffersActivity.this)).getId2ImageResMap();

        RxDataSource<OfferModel> rxDataSource = new RxDataSource<>(new ArrayList<>());
        rxDataSource
                .<ItemOffersBinding>bindRecyclerView(rv, R.layout.item_offers)
                .subscribe(viewHolder -> {

                    ItemOffersBinding layout = viewHolder.getViewDataBinding();
                    OfferModel model = viewHolder.getItem();

                    int id = Integer.parseInt(model.getCategoryId());

                    if (map.get(id) == null)
                        layout.icon.setImageResource(R.drawable.ic_cat_unknown_0);
                    else
                        layout.icon.setImageResource(map.get(id));

                    String weight = model.getParams() != null ? model.getParams().get("Вес") : "не указан";
                    layout.weight.setText(weight.length() == 0 ? weight :
                            String.format(getString(R.string.weight), weight));
                    layout.label.setText(String.format(getString(R.string.name), model.getName()));
                    layout.price.setText(String.format(getString(R.string.price), model.getPrice()));

                    layout.cv.setOnClickListener(view -> {
                        Intent intent = new Intent(ListOffersActivity.this, OfferActivity.class);
                        intent.putExtra(OfferModel.EXTRA, model);
                        startActivity(intent);
                    });
                });
        //retrofit

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        System.setProperty("http.keepAlive", "false");
        long cacheSize = 1024 * 1024;
        ;
        Cache cache = new Cache(new File(Utils.getInternalDirPath()), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        String API_BASE_URL = "http://ufa.farfor.ru/getyml/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        ApiEndpointInterface service = retrofit.create(ApiEndpointInterface.class);

        Observable<YmlCatalogModel> call = service.getAllOrdersObservable();
        call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    setStatus(Status.LOADING);
                    Log.w("getAllOrdersObservable", "doOnSubscribe");
                })
                .doOnUnsubscribe(() -> {
                    setStatus(Status.OK);
                    Log.w("getAllOrdersObservable", "doOnUnsubscribe");
                })
                .map(ymlCatalogModel -> ymlCatalogModel.getShop().getOffers())
                .flatMap(offerModels -> Observable.from(offerModels))
                .filter(offerModel -> (String.valueOf(catId).equals(offerModel.getCategoryId())))
                .collect(() -> new ArrayList<OfferModel>(), (offerModels, offerModel) -> offerModels.add(offerModel) )
                .map(offerModels -> rxDataSource.updateDataSet(offerModels))
                .subscribe(offerModelRxDataSource -> offerModelRxDataSource.updateAdapter());
    }

    /*
    @Override
    public Loader<ContentWrapper<List<OfferModel>>> onCreateLoader(int id, Bundle args) {
        setStatus(Status.LOADING);
        return new AsyncLoader(this, args.getInt(CategoryModel.ID_EXTRA));
    }

    @Override
    public void onLoadFinished(Loader<ContentWrapper<List<OfferModel>>> loader,
                               ContentWrapper<List<OfferModel>> data) {

        if (data.getException() == null && data.getContent() != null) {
            adapter.setModels(data.getContent());
            setStatus(Status.OK);
        } else if (data.getException() instanceof NoConnectionException) {
            errorMessage.setText(data.getException().getMessage());
            setStatus(Status.FAILED);
        } else {
            //throw new RuntimeException("[Unknown exception] " + data.getException().getMessage());
            errorMessage.setText("Smth went wrong. Please retry.");
            setStatus(BaseActivity.Status.FAILED);
        }
    }

    @Override
    public void onLoaderReset(Loader<ContentWrapper<List<OfferModel>>> loader) {
        adapter.setModels(new ArrayList<>(0));
    }
    */
    @Override
    protected void setStatus(Status status) {

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

}
