package com.example.yegor.cafeapp;

import android.app.Application;
import android.content.Context;

import com.example.yegor.cafeapp.models.RealmCategoryModel;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();

        if (realm.where(RealmCategoryModel.class).findAll().size() == 0)
            realm.executeTransactionAsync(realm1 -> {
                for (RealmCategoryModel model : Utils.getRealmCategoryModels())
                    realm1.copyToRealm(model);
            });
    }

    public static Context getContext() {
        return context;
    }

}
