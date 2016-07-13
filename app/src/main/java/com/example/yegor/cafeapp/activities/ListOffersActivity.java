package com.example.yegor.cafeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import okhttp3.Request;
import okhttp3.Response;
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

        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        realm = Realm.getDefaultInstance();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        int catId = getIntent().getExtras().getInt(RealmCategoryModel.ID_EXTRA);

        rv = (RecyclerView) findViewById(R.id.rv);
        loadingView = findViewById(R.id.loading_view);
        errorView = findViewById(R.id.error_view);
        errorMessage = (TextView) findViewById(R.id.error_message);

        rv.setLayoutManager(Utils.isPortrait(this) ? new LinearLayoutManager(this) : new GridLayoutManager(this, 3));
        rv.addItemDecoration(new ListDecorator(getResources().getDimension(R.dimen.offers_card_margin)));

        rxDataSource = setupRxBinding();
        apiEndpointInterface = setupRetrofitClient();
        setRx(catId);

        findViewById(R.id.retry_btn).setOnClickListener(v -> setRx(catId));
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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

                    boolean portrait = Utils.isPortrait(ListOffersActivity.this);
                    if (!portrait)
                        Glide.with(this)
                                .load(model.getPicture())
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .into(layout.icon);
                    else if (categoryModel == null)
                        layout.icon.setImageResource(R.drawable.ic_cat_unknown_0);
                    else
                        layout.icon.setImageResource(categoryModel.getImage());

                    if (portrait) {
                        String weight = model.getParams() != null ?
                                model.getParams().get("Вес") :
                                "не указан";

                        layout.weight.setText(weight.length() == 0 ? weight :
                                String.format(getString(R.string.weight), weight));
                        layout.price.setText(String.format(getString(R.string.price),
                                model.getPrice()));
                    } else {
                        ViewGroup.LayoutParams params = layout.cv.getLayoutParams();
                        params.height = Utils.getScreenWidth(this) / 3;
                        layout.cv.setLayoutParams(params);
                    }

                    layout.label.setText(String.format(getString(R.string.name), model.getName()));
                    layout.cv.setOnClickListener(view -> {
                        Intent intent = new Intent(ListOffersActivity.this, OfferActivity.class);
                        intent.putExtra(OfferModel.EXTRA, model);
                        if (Utils.isLollipop() && !Utils.isPortrait(this)) {
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(this, layout.icon, "offer_transition");
                            startActivity(intent, options.toBundle());
                        } else
                            startActivity(intent);
                    });
                });

        return rxDataSource;
    }

    private ApiEndpointInterface setupRetrofitClient() {

        //retrofit
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        System.setProperty("http.keepAlive", "false");
        final long cacheSize = 10 * 1024 * 1024;

        Cache cache = new Cache(new File(Utils.getInternalDirPath(), "responses"), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    String cacheHeaderValue = Utils.hasConnection()
                            ? "public, max-age=2419200"
                            : "public, only-if-cached, max-stale=2419200";
                    Request request = originalRequest.newBuilder().build();
                    Response response = chain.proceed(request);
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", cacheHeaderValue)
                            .build();
                })
                .addNetworkInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    String cacheHeaderValue = Utils.hasConnection()
                            ? "public, max-age=2419200"
                            : "public, only-if-cached, max-stale=2419200";
                    Request request = originalRequest.newBuilder().build();
                    Response response = chain.proceed(request);
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", cacheHeaderValue)
                            .build();
                })
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
