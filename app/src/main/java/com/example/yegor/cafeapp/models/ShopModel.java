package com.example.yegor.cafeapp.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class ShopModel {

    @ElementList(name = "categories")
    private List<RealmCategoryModel> categories;
    @ElementList
    private List<OfferModel> offers;

    public ShopModel() {
    }

    public ShopModel(List<RealmCategoryModel> categories, List<OfferModel> offers) {
        this.categories = categories;
        this.offers = offers;
    }

    public List<RealmCategoryModel> getCategories() {
        return categories;
    }

    public List<OfferModel> getOffers() {
        return offers;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "categories=" + categories +
                ", offers=" + offers +
                '}';
    }
}