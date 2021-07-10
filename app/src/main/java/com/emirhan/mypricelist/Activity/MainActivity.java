package com.emirhan.mypricelist.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.emirhan.mypricelist.Adapters.MyAdapter;
import com.emirhan.mypricelist.Model.PriceList;
import com.emirhan.mypricelist.R;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<PriceList> arrayList;

    ListView listView;
    ArrayList<String> nameArray;
    ArrayList<Integer> idArray;
    ArrayList<Integer> quantityArray;
    ArrayList<Integer> PriceArray;
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
        MyAdapter myAdapter = new MyAdapter(MainActivity.this,arrayList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("priceId", Integer.valueOf((int) myAdapter.getItemId(position) + 1));
                System.out.println(myAdapter.getItemId(position));
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

        Cursor c = database.rawQuery("SELECT * FROM pricelist WHERE id = 1",null);

        if (item.getItemId() == R.id.delete_products) {
            if (c.moveToFirst()) {
                database.execSQL("DELETE FROM PriceList");
                // cursor has a data, print here
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } else {
                // cursor is empty, print here
                Toast.makeText(MainActivity.this,"Something went wrong.\nHave you tried adding products?",Toast.LENGTH_LONG).show();
            }

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
