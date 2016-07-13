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
import com.example.yegor.cafeapp.models.OfferModel;
import com.example.yegor.cafeapp.models.RealmCategoryModel;
import com.example.yegor.cafeapp.models.YmlCatalogModel;
import com.example.yegor.cafeapp.view.ListDecorator;
import com.minimize.android.rxrecycleradapter.RxDataSource;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;

import io.realm.Realm;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ListOffersActivity extends BaseActivity {
    //implements LoaderManager.LoaderCallbacks<ContentWrapper<List<OfferModel>>> {

    private RecyclerView rv;
    private View loadingView, errorView;
    private TextView errorMessage;

    private ApiEndpointInterface apiEndpointInterface;
    private RxDataSource<OfferModel> rxDataSource;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private Realm realm;

    public ListOffersActivity() {
        super(R.layout.activity_list_offers);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm= Realm.getDefaultInstance();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        int catId = getIntent().getExtras().getInt(RealmCategoryModel.ID_EXTRA);

        rv = (RecyclerView) findViewById(R.id.rv);
        loadingView = findViewById(R.id.loading_view);
        errorView = findViewById(R.id.error_view);
        errorMessage = (TextView) findViewById(R.id.error_message);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new ListDecorator(getResources().getDimension(R.dimen.offers_card_margin)));

        rxDataSource = setupRxBinding();
        apiEndpointInterface = setupRetrofitClient();
        setRx(catId);

        findViewById(R.id.retry_btn).setOnClickListener(v -> setRx(catId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeSubscription.unsubscribe();
        realm.close();
    }

    @Override
    protected void setStatus(Status status) {

        switch (status) {
            case LOADING:
                rv.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                break;
            case OK:
                rv.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);
                break;
            case FAILED:
                rv.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
                break;
        }

    }

    private RxDataSource<OfferModel> setupRxBinding() {

       // Map<Integer, Integer> map = new HashMap<>();

        RxDataSource<OfferModel> rxDataSource = new RxDataSource<>(new ArrayList<>());

        rxDataSource
                .<ItemOffersBinding>bindRecyclerView(rv, R.layout.item_offers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewHolder -> {

                    ItemOffersBinding layout = viewHolder.getViewDataBinding();
                    OfferModel model = viewHolder.getItem();

                    RealmCategoryModel categoryModel = realm.where(RealmCategoryModel.class)
                            .equalTo("id", Integer.parseInt(model.getCategoryId()))
                            .findFirst();

                    if (categoryModel == null)
                        layout.icon.setImageResource(R.drawable.ic_cat_unknown_0);
                    else
                        layout.icon.setImageResource(categoryModel.getImage());

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

        return rxDataSource;
    }

    private ApiEndpointInterface setupRetrofitClient() {

        //retrofit
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        System.setProperty("http.keepAlive", "false");
        long cacheSize = 1024 * 1024;

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

        return retrofit.create(ApiEndpointInterface.class);
    }

    private void setRx(int catId) {

        Observable<YmlCatalogModel> call = apiEndpointInterface.getAllOrdersObservable();
        Subscription subscribe = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    setStatus(Status.LOADING);
                    Log.w("getAllOrdersObservable", "doOnSubscribe");
                })
                .map(ymlCatalogModel -> ymlCatalogModel.getShop().getOffers())
                .flatMap(Observable::from)
                .filter(offerModel -> (String.valueOf(catId).equals(offerModel.getCategoryId())))
                .collect(() -> new ArrayList<OfferModel>(), ArrayList::add)
                .map(offerModels -> rxDataSource.updateDataSet(offerModels))
                //.subscribe(offerModelRxDataSource -> offerModelRxDataSource.updateAdapter());
                .subscribe(
                        offerModelRxDataSource -> {
                            offerModelRxDataSource.updateAdapter();
                            setStatus(Status.OK);
                        },
                        throwable ->
                        {
                            if (throwable instanceof UnknownHostException ||
                                    UnknownHostException.class.isInstance(throwable))

                                errorMessage.setText(R.string.no_connection_exception);
                            else
                                errorMessage.setText(throwable.getMessage() + " " + throwable.toString());

                            setStatus(Status.FAILED);
                        });

        compositeSubscription.add(subscribe);
    }

}
