package com.example.yegor.cafeapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.adapters.CategoriesAdapter;
import com.example.yegor.cafeapp.models.CategoryModel;
import com.example.yegor.cafeapp.storage.MySQLiteClass;
import com.example.yegor.cafeapp.view.MyDecorator;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CategoriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        assert rv != null;
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new CategoriesAdapter(new ArrayList<>(0));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new MyDecorator(4));

        (new SetAdapterModels()).execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.content_categories;
    }

    class SetAdapterModels extends AsyncTask<Void, Void, List<CategoryModel>> {

        @Override
        protected void onPreExecute() {
            findViewById(R.id.loading_view).setVisibility(View.VISIBLE);
        }

        @Override
        protected List<CategoryModel> doInBackground(Void... params) {

            MySQLiteClass sqLiteClass = new MySQLiteClass(CategoriesActivity.this);
            if (!sqLiteClass.isCatTableExists()) {
                ArrayList<CategoryModel> categoryModels = new ArrayList<CategoryModel>() {{
                    add(new CategoryModel.Builder()
                            .setId(24)
                            .setCategory("Шашлыки")
                            .setImage(R.drawable.ic_cat_kebab_24)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(23)
                            .setCategory("Добавки")
                            .setImage(R.drawable.ic_cat_sauce_23)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(18)
                            .setCategory("Роллы")
                            .setImage(R.drawable.ic_cat_maki_18)
                            .create());
                    add(new CategoryModel.Builder().setId(10)
                            .setCategory("Десерты")
                            .setImage(R.drawable.ic_cat_ice_cream_10).create());
                    add(new CategoryModel.Builder()
                            .setId(9)
                            .setCategory("Напитки")
                            .setImage(R.drawable.ic_cat_glass_9)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(5)
                            .setCategory("Суши")
                            .setImage(R.drawable.ic_cat_sushi_5)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(2)
                            .setCategory("Сеты")
                            .setImage(R.drawable.ic_cat_sushi_2).create());
                    add(new CategoryModel.Builder()
                            .setId(6)
                            .setCategory("Супы")
                            .setImage(R.drawable.ic_cat_soup_bowl_6)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(7)
                            .setCategory("Салаты")
                            .setImage(R.drawable.ic_cat_salad_7)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(8)
                            .setCategory("Теплое")
                            .setImage(R.drawable.ic_cat_first_8)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(20)
                            .setCategory("Закуски")
                            .setImage(R.drawable.ic_cat_nachos_20)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(3)
                            .setCategory("Лапша")
                            .setImage(R.drawable.ic_cat_noodles_3)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(25)
                            .setCategory("Конструктор")
                            .setImage(R.drawable.ic_cat_custom_25)
                            .create());
                    add(new CategoryModel.Builder()
                            .setId(1)
                            .setCategory("Пицца")
                            .setImage(R.drawable.ic_cat_pizza_1)
                            .create());
                }};
                sqLiteClass.addCategories(categoryModels);
                return categoryModels;
            } else
                return sqLiteClass.getAllCategories();

        }

        @Override
        protected void onPostExecute(List<CategoryModel> categoryModels) {
            adapter.setModels(categoryModels);
            findViewById(R.id.loading_view).setVisibility(View.GONE);
        }

    }

}
