package com.example.yegor.cafeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.databinding.ItemCategoriesBinding;
import com.example.yegor.cafeapp.models.RealmCategoryModel;
import com.example.yegor.cafeapp.view.GridDecorator;
import com.minimize.android.rxrecycleradapter.RxDataSource;

import java.util.ArrayList;

import io.realm.Realm;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;


public class CategoriesActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private View loadingView;

    Realm realm;

    public CategoriesActivity() {
        super(R.layout.activity_categories);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        loadingView = findViewById(R.id.loading_view);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.addItemDecoration(new GridDecorator(getResources().getDimension(R.dimen.category_card_margin)));

        RxDataSource<RealmCategoryModel> rxDataSource = new RxDataSource<>(new ArrayList<>());
        rxDataSource
                .<ItemCategoriesBinding>bindRecyclerView(rv, R.layout.item_categories)
                .subscribe(viewHolder -> {
                    ItemCategoriesBinding layout = viewHolder.getViewDataBinding();
                    RealmCategoryModel model = viewHolder.getItem();
                    layout.text.setText(model.getCategory());
                    layout.image.setImageResource(model.getImage());
                    layout.cv.setOnClickListener(view -> {
                        Intent intent = new Intent(CategoriesActivity.this, ListOffersActivity.class);
                        intent.putExtra(RealmCategoryModel.ID_EXTRA, model.getId());
                        startActivity(intent);
                    });
                });

        Subscription subscriber = realm.where(RealmCategoryModel.class)
                .findAllAsync()
                .asObservable()
                .doOnSubscribe(() -> setStatus(Status.LOADING))
                .doOnNext(realmCategoryModels -> setStatus(Status.OK))
                //.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(rxDataSource::updateDataSet)
                .subscribe(RxDataSource::updateAdapter);

        compositeSubscription.add(subscriber);
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
                loadingView.setVisibility(View.VISIBLE);
                break;
            case OK:
                loadingView.setVisibility(View.GONE);
                break;
        }
    }

}