package com.example.yegor.cafeapp;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;

import com.example.yegor.cafeapp.models.ParamModel;
import com.example.yegor.cafeapp.models.RealmCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String getParamByName(List<ParamModel> params, String paramName) {

        String val = "";

        if (params != null)
            for (ParamModel param : params)
                if (paramName.equals(param.getName()))
                    val = param.getText();

        return val;

    }

    public static String getString(@StringRes int id) {
        return App.getContext().getString(id);
    }

    public static boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int dp2pixel(float dp) {
        Resources resources = App.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getInternalDirPath() {
        return App.getContext().getFilesDir().getAbsolutePath();
    }

    public static List<RealmCategoryModel> getRealmCategoryModels() {

        ArrayList<RealmCategoryModel> categoryModels = new ArrayList<>(14);

        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(24)
                .setCategory("Шашлыки")
                .setImage(R.drawable.ic_cat_kebab_24)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(23)
                .setCategory("Добавки")
                .setImage(R.drawable.ic_cat_sauce_23)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(18)
                .setCategory("Роллы")
                .setImage(R.drawable.ic_cat_maki_18)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder().setId(10)
                .setCategory("Десерты")
                .setImage(R.drawable.ic_cat_ice_cream_10).create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(9)
                .setCategory("Напитки")
                .setImage(R.drawable.ic_cat_glass_9)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(5)
                .setCategory("Суши")
                .setImage(R.drawable.ic_cat_sushi_5)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(2)
                .setCategory("Сеты")
                .setImage(R.drawable.ic_cat_sushi_2).create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(6)
                .setCategory("Супы")
                .setImage(R.drawable.ic_cat_soup_bowl_6)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(7)
                .setCategory("Салаты")
                .setImage(R.drawable.ic_cat_salad_7)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(8)
                .setCategory("Теплое")
                .setImage(R.drawable.ic_cat_first_8)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(20)
                .setCategory("Закуски")
                .setImage(R.drawable.ic_cat_nachos_20)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(3)
                .setCategory("Лапша")
                .setImage(R.drawable.ic_cat_noodles_3)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(25)
                .setCategory("Конструктор")
                .setImage(R.drawable.ic_cat_custom_25)
                .create());
        categoryModels.add(new RealmCategoryModel.Builder()
                .setId(1)
                .setCategory("Пицца")
                .setImage(R.drawable.ic_cat_pizza_1)
                .create());

        return categoryModels;
    }

}
