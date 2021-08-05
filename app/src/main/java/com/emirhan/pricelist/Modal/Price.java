package com.emirhan.pricelist.Modal;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Blob;

@Entity
public class Price implements Serializable {


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


}
