package com.example.yegor.cafeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.Utils;
import com.example.yegor.cafeapp.databinding.ItemCategoriesBinding;
import com.example.yegor.cafeapp.models.CategoryModel;
import com.example.yegor.cafeapp.storage.MySQLiteClass;
import com.example.yegor.cafeapp.view.GridDecorator;
import com.minimize.android.rxrecycleradapter.RxDataSource;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CategoriesActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private View loadingView;

    public CategoriesActivity() {
        super(R.layout.activity_categories);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingView = findViewById(R.id.loading_view);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.addItemDecoration(new GridDecorator(getResources().getDimension(R.dimen.category_card_margin)));

        RxDataSource<CategoryModel> rxDataSource = new RxDataSource<>(new ArrayList<>());
        rxDataSource
                .<ItemCategoriesBinding>bindRecyclerView(rv, R.layout.item_categories)
                .subscribe(viewHolder -> {
                    ItemCategoriesBinding layout = viewHolder.getViewDataBinding();
                    CategoryModel model = viewHolder.getItem();
                    layout.text.setText(model.getCategory());
                    layout.image.setImageResource(model.getImage());
                    layout.cv.setOnClickListener(view -> {
                        Intent intent = new Intent(CategoriesActivity.this, ListOffersActivity.class);
                        intent.putExtra(CategoryModel.ID_EXTRA, model.getId());
                        startActivity(intent);
                    });
                });

        Observable
                .create((Observable.OnSubscribe<List<CategoryModel>>) subscriber -> {
                    subscriber.onNext(getCategoryModels());
                    subscriber.onCompleted();
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setStatus(Status.LOADING))
                .doOnUnsubscribe(() -> setStatus(Status.OK))
                .map(categoryModels2 -> rxDataSource.updateDataSet(categoryModels2))
                .subscribe(categoryModelRxDataSource -> categoryModelRxDataSource.updateAdapter());
    }

    @Override
    protected void setStatus(Status status) {

        switch (status) {
            case LOADING:
                loadingView.setVisibility(View.VISIBLE);
                break;
            case OK:
                loadingView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private List<CategoryModel> getCategoryModels() {

        MySQLiteClass sqLiteClass = new MySQLiteClass(CategoriesActivity.this);

        if (!sqLiteClass.isCatTableExists()) {

            List<CategoryModel> categoryModels = Utils.getCategoryModels();
            sqLiteClass.addCategories(categoryModels);

            return categoryModels;
        } else
            return sqLiteClass.getAllCategories();
    }

}