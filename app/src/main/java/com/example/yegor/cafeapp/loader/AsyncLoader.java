package com.example.yegor.cafeapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.yegor.cafeapp.Utils;
import com.example.yegor.cafeapp.exceptions.NoConnectionException;
import com.example.yegor.cafeapp.models.OfferModel;
import com.example.yegor.cafeapp.models.YmlCatalogModel;
import com.example.yegor.cafeapp.models.adapter.ContentWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

public class AsyncLoader extends AsyncTaskLoader<ContentWrapper<List<OfferModel>>> {

    private static final int cacheSize = 1024 * 1024;// 1 MiB
    private static final String API_BASE_URL = "http://ufa.farfor.ru/getyml/";

    private int catId;

    public AsyncLoader(Context context, int catId) {
        super(context);

        this.catId = catId;
    }

    @Override
    public ContentWrapper<List<OfferModel>> loadInBackground() {

        if (!Utils.hasConnection()) {
            try {
                Thread.sleep(350);//for beauty
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new ContentWrapper<>(new NoConnectionException());
        }

        System.setProperty("http.keepAlive", "false");
        Cache cache = new Cache(new File(Utils.getInternalDirPath()), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        Json service = retrofit.create(Json.class);

        Call<YmlCatalogModel> data = service.getAllOrders();

        try {
            List<OfferModel> offers = new ArrayList<>();

            for (OfferModel offer : data.execute().body().getShop().getOffers())
                if (String.valueOf(catId).equals(offer.getCategoryId()))
                    offers.add(offer);

            return new ContentWrapper<>(offers);
        } catch (IOException e) {
            return new ContentWrapper<>(e);
        }

    }

    interface Json {
        @GET("?key=ukAXxeJYZN")
        Call<YmlCatalogModel> getAllOrders();
    }

}
