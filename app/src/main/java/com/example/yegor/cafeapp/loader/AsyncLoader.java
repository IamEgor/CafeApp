package com.example.yegor.cafeapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.yegor.cafeapp.Utils;
import com.example.yegor.cafeapp.exceptions.NoConnectionException;
import com.example.yegor.cafeapp.models.adapter.ContentWrapper;
import com.example.yegor.cafeapp.models.OfferModel;
import com.example.yegor.cafeapp.models.YmlCatalogModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

public class AsyncLoader extends AsyncTaskLoader<ContentWrapper<List<OfferModel>>> {

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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient())
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
