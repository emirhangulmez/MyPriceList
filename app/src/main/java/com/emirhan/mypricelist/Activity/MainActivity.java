package com.emirhan.mypricelist.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.emirhan.mypricelist.Adapters.MyAdapter;
import com.emirhan.mypricelist.Model.PriceList;
import com.emirhan.mypricelist.R;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    SQLiteDatabase database;
    ArrayList<String> nameArray;
    ArrayList<Integer> idArray;
    ArrayList<Integer> quantityArray;
    ArrayList<Integer> PriceArray;
    ArrayList<PriceList> arrayList;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.listView);
        nameArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();
        PriceArray = new ArrayList<Integer>();
        quantityArray = new ArrayList<Integer>();


        loadDataInListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("priceId",3);
                intent.putExtra("info","old");
                startActivity(intent);
            }
        });
    }

    private void loadDataInListView() {
       arrayList = getAllData();
       myAdapter = new MyAdapter(this,arrayList);
       listView.setAdapter(myAdapter);
       myAdapter.notifyDataSetChanged();
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

    public ArrayList<PriceList> getAllData()
    {
        try {
            arrayList = new ArrayList<>();
            SQLiteDatabase db = this.openOrCreateDatabase("PriceList",MODE_PRIVATE,null);
            Cursor cursor = db.rawQuery("SELECT * FROM pricelist", null);
            int imageIx = cursor.getColumnIndex("image");
            int salePriceIx = cursor.getColumnIndex("saleprice");


            while(cursor.moveToNext()) {
                byte[] bytes = cursor.getBlob(imageIx);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                String name = cursor.getString(1);
                String salePrice = cursor.getString(salePriceIx);
                PriceList priceList = new PriceList(bitmap,name,salePrice);

                arrayList.add(priceList);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
