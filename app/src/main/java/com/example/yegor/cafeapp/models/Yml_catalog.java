package com.example.yegor.cafeapp.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "yml_catalog")
    public class Yml_catalog {

    @Attribute(name = "date")
    private String date;
    @Element(name = "shop")
    private Shop shop;

    public Yml_catalog() {
    }

    public Yml_catalog(String date, Shop shop) {
        this.date = date;
        this.shop = shop;
    }

    public String getDate() {
        return date;
    }

    public Shop getShop() {
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