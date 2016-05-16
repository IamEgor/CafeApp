package com.example.yegor.cafeapp.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class Shop {

    @ElementList(name = "categories")
    private List<Category> categories;
    @ElementList
    private List<Offer> offers;

    public Shop() {
    }

    public Shop(List<Category> categories, List<Offer> offers) {
        this.categories = categories;
        this.offers = offers;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Offer> getOffers() {
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