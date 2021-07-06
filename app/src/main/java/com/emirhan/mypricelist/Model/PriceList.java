package com.emirhan.mypricelist.Model;

import android.graphics.Bitmap;

public class PriceList {
    Bitmap img;
    String productName,salePrice;

    public PriceList(Bitmap img, String productName, String salePrice) {
        this.img = img;
        this.productName = productName;
        this.salePrice = salePrice;
    }
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
}
