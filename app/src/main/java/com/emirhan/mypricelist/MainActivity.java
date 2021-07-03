package com.emirhan.mypricelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    SQLiteDatabase database;
    ArrayList<String> nameArray;
    ArrayList<Integer> idArray;
    ArrayList<Integer> quantityArray;
    ArrayList<Integer> PriceArray;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.listView);
        nameArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();
        PriceArray = new ArrayList<Integer>();
        quantityArray = new ArrayList<Integer>();


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,nameArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("priceId",idArray.get(position));
                intent.putExtra("info","old");
                startActivity(intent);
            }
        });
        getData();
    }


    public void getData() {
        try {
        SQLiteDatabase database = this.openOrCreateDatabase("PriceList",MODE_PRIVATE,null);

        Cursor cursor = database.rawQuery("SELECT * FROM PriceList",null);
            int idIX = cursor.getColumnIndex("id");
            int nameIX = cursor.getColumnIndex("productname");
            int quantityIx = cursor.getColumnIndex("quantity");
            int priceIx = cursor.getColumnIndex("salePrice");
            while (cursor.moveToNext()) {
                nameArray.add(cursor.getString(nameIX));
                idArray.add(cursor.getInt(idIX));

            }
            arrayAdapter.notifyDataSetChanged();
            cursor.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflater
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_product,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       SQLiteDatabase database = this.openOrCreateDatabase("PriceList",MODE_PRIVATE,null);

        if (item.getItemId() == R.id.add_product) {
            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }
        if (item.getItemId() == R.id.delete_products) {
            database.execSQL("DELETE FROM PriceList");
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}