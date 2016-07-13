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
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class AsyncLoader extends AsyncTaskLoader<ContentWrapper<List<OfferModel>>> {

    private static final int CACHE_SIZE = 10 * 1024 * 1024;// 10 MB
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
                Thread.sleep(500);//for beauty
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new ContentWrapper<>(new NoConnectionException());
        }

        System.setProperty("http.keepAlive", "false");
        Cache cache = new Cache(new File(Utils.getInternalDirPath(), "responses"), CACHE_SIZE);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    if (Utils.hasConnection()) {
                        int maxAge = 10 * 60; // read from cache for 10 minutes
                        return originalResponse.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .build();
                    } else {
                        int maxStale = 60 * 60 * 24; // tolerate 1 day stale
                        return originalResponse.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .build();
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        ApiEndpointInterface service = retrofit.create(ApiEndpointInterface.class);

        Call<YmlCatalogModel> data = service.getAllOrders();

        try

        {
            List<OfferModel> offers = new ArrayList<>();

            for (OfferModel offer : data.execute().body().getShop().getOffers())
                if (String.valueOf(catId).equals(offer.getCategoryId()))
                    offers.add(offer);

            return new ContentWrapper<>(offers);
        } catch (
                IOException e
                )

        {
            return new ContentWrapper<>(e);
        }
    }

}
