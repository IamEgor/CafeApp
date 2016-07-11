package com.example.yegor.cafeapp.loader;

import com.example.yegor.cafeapp.models.YmlCatalogModel;

import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by yegor on 11.07.16.
 */
public interface ApiEndpointInterface {

    @GET("?key=ukAXxeJYZN")
    Call<YmlCatalogModel> getAllOrders();

    @GET("?key=ukAXxeJYZN")
    Observable<YmlCatalogModel> getAllOrdersObservable();
}
