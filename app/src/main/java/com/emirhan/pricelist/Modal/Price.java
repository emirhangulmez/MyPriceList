package com.emirhan.pricelist.Modal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Blob;

@Entity
public class Price implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "image")
    public byte[] image;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "costPrice")
    public String costPrice;

    @ColumnInfo(name = "salePrice")
    public String salePrice;

    @ColumnInfo(name = "quantity")
    public String quantity;


    public Price(String name, String costPrice, String salePrice, String quantity, byte[] image) {
        this.name = name;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.image = image;
    }


    protected Price(Parcel in) {
        id = in.readInt();
        image = in.createByteArray();
        name = in.readString();
        costPrice = in.readString();
        salePrice = in.readString();
        quantity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByteArray(image);
        dest.writeString(name);
        dest.writeString(costPrice);
        dest.writeString(salePrice);
        dest.writeString(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Price> CREATOR = new Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel in) {
            return new Price(in);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };
}
