package com.emirhan.pricelist.RoomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.emirhan.pricelist.Model.Price;

@Database(entities = {Price.class},version = 1)
public abstract class PriceDatabase extends RoomDatabase {
    public abstract PriceDao priceDao();
}
