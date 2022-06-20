package com.emirhan.pricelist.RoomDB;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.emirhan.pricelist.Model.Price;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PriceDao {

    @Insert
    Completable insert(Price price);

    @Delete
    Completable delete(Price price);

    @Query("UPDATE price SET name = :name, costPrice = :costPrice, salePrice = :salePrice, quantity = :quantity WHERE id = :id")
    Completable updatePrice(int id,String name,String costPrice,String salePrice,String quantity);

    @Query("DELETE FROM Price")
    Completable deleteAll();

    @Query("SELECT * FROM Price")
    Cursor getCursorAll();


    @Query("SELECT * FROM Price")
    Flowable<List<Price>> getAll();

}
