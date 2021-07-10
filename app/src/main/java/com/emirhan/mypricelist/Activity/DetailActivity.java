package com.emirhan.mypricelist.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.emirhan.mypricelist.R;

public class DetailActivity extends AppCompatActivity {
    //  Variables Head //

    Bitmap selectedImage;
    ImageView imageView;
    EditText productName, costPrice, salePrice, quantityNumber;
    Button save;
    SQLiteDatabase database;

    // Variables End //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //  Variables Head //
        imageView = findViewById(R.id.imageView2);
        productName = findViewById(R.id.productNameEditText);
        costPrice = findViewById(R.id.costPriceEditText);
        salePrice = findViewById(R.id.salePriceEditText);
        quantityNumber = findViewById(R.id.quantityEditText);

        //save = findViewById(R.id.save);
        database = this.openOrCreateDatabase("PriceList",MODE_PRIVATE,null);
        // Variables End //

        Intent intent = getIntent();
        int priceId = intent.getIntExtra("priceId",1);
        /*
        Since the data is taken according to the array, I had to add something above it.
        Because the sequence has to be according to SQLite, not according to the array.
         */

        try {
            Cursor cursor = database.rawQuery("SELECT * FROM pricelist WHERE id = ?",new String[] {String.valueOf(priceId)});
            int productNameIx = cursor.getColumnIndex("productname");
            int costPriceIx = cursor.getColumnIndex("costprice");
            int salePriceIx = cursor.getColumnIndex("saleprice");
            int quantityNumberIx = cursor.getColumnIndex("quantity");
            int imageIx = cursor.getColumnIndex("image");
            while (cursor.moveToNext()) {

                productName.setText(cursor.getString(productNameIx));
                costPrice.setText(cursor.getString(costPriceIx));
                salePrice.setText(cursor.getString(salePriceIx));
                quantityNumber.setText(cursor.getString(quantityNumberIx));
                byte[] bytes = cursor.getBlob(imageIx);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void delete(View view) {
        database = this.openOrCreateDatabase("PriceList",MODE_PRIVATE,null);
        Intent intent = getIntent();
        int priceId = intent.getIntExtra("priceId",1);
        database.execSQL("DELETE FROM PriceList WHERE id = ?", new String[] {String.valueOf(priceId)});
        Intent ok = new Intent (DetailActivity.this,MainActivity.class);
        finish();
        startActivity(ok);
    }

    public void update(View view){
        String productNameText = productName.getText().toString();
        String costPriceText = costPrice.getText().toString();
        String salePriceText = salePrice.getText().toString();
        String quantityNumberText = quantityNumber.getText().toString();
        database = this.openOrCreateDatabase("PriceList",MODE_PRIVATE,null);
        Intent intent = getIntent();
        int priceId = intent.getIntExtra("priceId",1);
        if (productNameText.matches("") || costPriceText.matches("") || salePriceText.matches("") || quantityNumberText.matches("")) {
            System.out.println("Please fill in blanks");
        } else {
            try {
                database = this.openOrCreateDatabase("PriceList",MODE_PRIVATE,null);
                database.execSQL("UPDATE pricelist SET productname = ? WHERE id = ?",new String[] {productNameText,String.valueOf(priceId)});
                database.execSQL("UPDATE pricelist SET costprice = ? WHERE id = ?",new String[] {costPriceText,String.valueOf(priceId)});
                database.execSQL("UPDATE pricelist SET saleprice = ? WHERE id = ?",new String[] {salePriceText,String.valueOf(priceId)});
                database.execSQL("UPDATE pricelist SET quantity = ? WHERE id = ?",new String[] {quantityNumberText,String.valueOf(priceId)});
                finish();
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        }
}