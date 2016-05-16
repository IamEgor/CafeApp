package com.example.yegor.cafeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class Offer implements Parcelable {

    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    public static final String EXTRA = "OFFER_EXTRA";

    @Attribute
    private String id;
    @Element
    private String picture;
    @Element
    private String price;
    @Element(required = false)
    private String description;
    @Element
    private String name;
    @Element
    private String categoryId;
    @Element
    private String url;
    @ElementList(inline = true, required = false)
    private List<Param> param;

    public Offer() {
    }

    public Offer(String id, String picture, String price, String description, String name,
                 String categoryId, String url, List<Param> param) {
        this.id = id;
        this.picture = picture;
        this.price = price;
        this.description = description;
        this.name = name;
        this.categoryId = categoryId;
        this.url = url;
        this.param = param;
    }

    protected Offer(Parcel in) {
        id = in.readString();
        picture = in.readString();
        price = in.readString();
        description = in.readString();
        name = in.readString();
        categoryId = in.readString();
        url = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getUrl() {
        return url;
    }

    public List<Param> getParams() {
        return param;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id='" + id + '\'' +
                ", picture='" + picture + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", url='" + url + '\'' +
                ", param=" + param +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(picture);
        dest.writeString(price);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeString(categoryId);
        dest.writeString(url);
    }

}

