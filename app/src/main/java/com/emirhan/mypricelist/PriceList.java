package com.emirhan.mypricelist;

public class PriceList {

    private int productID;
    private String productName;
    private int quantity;
    private int costPrice;
    private int salePrice;

    private int pictureInteger;

    public PriceList(int productID, String productName, int quantity, int costPrice, int salePrice, int pictureInteger) {
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.pictureInteger = pictureInteger;

    }

    public String getProductName() {
        return productName;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getCostPrice() {
        return costPrice;
    }
    public int getSalePrice() {
        return salePrice;
    }
    public int getPictureInteger() {
        return pictureInteger;
    }
}

