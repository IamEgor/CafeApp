package com.example.yegor.cafeapp.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "yml_catalog")
    public class YmlCatalogModel {

    @Attribute(name = "date")
    private String date;
    @Element(name = "shop")
    private ShopModel shop;

    public YmlCatalogModel() {
    }

    public YmlCatalogModel(String date, ShopModel shop) {
        this.date = date;
        this.shop = shop;
    }

    public String getDate() {
        return date;
    }

    public ShopModel getShop() {
        return shop;
    }

    @Override
    public String toString() {
        return "Yml_catalog{" +
                "date='" + date + '\'' +
                ", shop=" + shop +
                '}';
    }
}