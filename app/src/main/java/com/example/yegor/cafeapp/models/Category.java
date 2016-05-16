package com.example.yegor.cafeapp.models;

import android.support.annotation.DrawableRes;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root
public class Category {

    public static final String ID_EXTRA = "ID_EXTRA";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String IMAGE = "IMAGE";

    @Attribute
    private int id;
    @Text
    private String category;
    @DrawableRes
    private int image;

    public Category() {
    }

    public Category(int id, String category, int image) {
        this.id = id;
        this.category = category;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", image=" + image +
                '}';
    }

    public static class Builder {

        private int id;
        private String category;
        private int image;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder setImage(int image) {
            this.image = image;
            return this;
        }

        public Category create() {
            return new Category(id, category, image);
        }
    }

}