package com.example.yegor.cafeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map;

@Root
public class OfferModel implements Parcelable {

    public static final String EXTRA = "OFFER_EXTRA";
    public static final Parcelable.Creator<OfferModel> CREATOR = new Parcelable.Creator<OfferModel>() {
        @Override
        public OfferModel createFromParcel(Parcel source) {
            return new OfferModel(source);
        }

        @Override
        public OfferModel[] newArray(int size) {
            return new OfferModel[size];
        }
    };

    @Attribute
    private String id;
    @Element(required = false)
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
    @ElementMap(entry = "param", key = "name", attribute = true, inline = true, required = false)
    private Map<String, String> param;

    public OfferModel() {
    }

    public OfferModel(String id, String picture, String price, String description, String name,
                      String categoryId, String url, Map<String, String> param) {
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
        this.id = in.readString();
        this.picture = in.readString();
        this.price = in.readString();
        this.description = in.readString();
        this.name = in.readString();
        this.categoryId = in.readString();
        this.url = in.readString();
        int paramSize = in.readInt();
        this.param = new HashMap<String, String>(paramSize);
        for (int i = 0; i < paramSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.param.put(key, value);
        }
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

    public Map<String, String> getParams() {
        return param;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.picture);
        dest.writeString(this.price);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeString(this.categoryId);
        dest.writeString(this.url);
        dest.writeInt(this.param.size());
        for (Map.Entry<String, String> entry : this.param.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
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

