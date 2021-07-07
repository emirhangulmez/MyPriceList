package com.emirhan.mypricelist.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PriceList implements Parcelable {
    Bitmap img;
    String productName,salePrice;

    public PriceList(Bitmap img, String productName, String salePrice) {
        this.img = img;
        this.productName = productName;
        this.salePrice = salePrice;
    }

    protected PriceList(Parcel in) {
        img = in.readParcelable(Bitmap.class.getClassLoader());
        productName = in.readString();
        salePrice = in.readString();
    }

    public static final Creator<PriceList> CREATOR = new Creator<PriceList>() {
        @Override
        public PriceList createFromParcel(Parcel in) {
            return new PriceList(in);
        }

        @Override
        public PriceList[] newArray(int size) {
            return new PriceList[size];
        }
    };

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSalePrice() {
        return salePrice + "$";
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(img, flags);
        dest.writeString(productName);
        dest.writeString(salePrice);
    }
}
