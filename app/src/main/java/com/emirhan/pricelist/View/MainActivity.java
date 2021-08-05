package com.emirhan.pricelist.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.emirhan.pricelist.Adapter.PriceAdapter;
import com.emirhan.pricelist.Modal.Price;
import com.emirhan.pricelist.R;
import com.emirhan.pricelist.RoomDB.PriceDao;
import com.emirhan.pricelist.RoomDB.PriceDatabase;
import com.emirhan.pricelist.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ActivityMainBinding binding;
    PriceDao priceDao;
    PriceDatabase db;
    GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = Room.databaseBuilder(getApplicationContext(),PriceDatabase.class,"Prices")
                .allowMainThreadQueries()
                .build();
        priceDao = db.priceDao();

        try {
            compositeDisposable.add(
                    priceDao.getAll()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(MainActivity.this::handleResponse)
            );

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleResponse(List<Price> priceList) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PriceAdapter priceAdapter = new PriceAdapter(priceList);
        gridLayoutManager = new GridLayoutManager(MainActivity.this,2,GridLayoutManager.VERTICAL,false);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setAdapter(priceAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_product) {
            Intent intent = new Intent(MainActivity.this,SecondaryActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }

        if (item.getItemId() == R.id.delete_products) {
            if (priceDao.getCursorAll().moveToFirst()) {
                compositeDisposable.add(priceDao.deleteAll().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(MainActivity.this::onDestroy));
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else{
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),"Something went wrong.\nHave you tried adding products?",Snackbar.LENGTH_LONG).setAction("Add Product", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,SecondaryActivity.class);
                        intent.putExtra("info","new");
                        startActivity(intent);
                    }
                }).show();


            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}