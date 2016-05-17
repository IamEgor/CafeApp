package com.example.yegor.cafeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class OfferModel implements Parcelable {

    public static final String EXTRA = "OFFER_EXTRA";
    public static final Creator<OfferModel> CREATOR = new Creator<OfferModel>() {
        @Override
        public OfferModel createFromParcel(Parcel in) {
            return new OfferModel(in);
        }

        @Override
        public OfferModel[] newArray(int size) {
            return new OfferModel[size];
        }
    };

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
    private List<ParamModel> param;

    public OfferModel() {
    }

    public OfferModel(String id, String picture, String price, String description, String name,
                      String categoryId, String url, List<ParamModel> param) {
        this.id = id;
        this.picture = picture;
        this.price = price;
        this.description = description;
        this.name = name;
        this.categoryId = categoryId;
        this.url = url;
        this.param = param;
    }

    protected OfferModel(Parcel in) {
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

    public List<ParamModel> getParams() {
        return param;
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

}

