package com.emirhan.mypricelist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    Bitmap selectedImage;
    ImageView imageView;
    EditText productName, costPrice, salePrice, quantityNumber;
    Button save;
    SQLiteDatabase database;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        productName = findViewById(R.id.productName);
        costPrice = findViewById(R.id.costPrice);
        salePrice = findViewById(R.id.salePrice);
        quantityNumber = findViewById(R.id.quantityNumber);
        save = findViewById(R.id.saveus);
        database = this.openOrCreateDatabase("PriceList",MODE_PRIVATE,null);


        Bitmap selectImage = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.selectimage);
        imageView.setImageBitmap(selectImage);
    }

    public void selectImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);

        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            try {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveToDatabase() {
        // SQLite Definition Object
        textView = findViewById(R.id.textView);
        String productNameText = productName.getText().toString();
        String costPriceText = costPrice.getText().toString();
        String salePriceText = salePrice.getText().toString();
        String quantityNumberText = quantityNumber.getText().toString();
        if (productNameText.matches("") || costPriceText.matches("") || salePriceText.matches("") || quantityNumberText.matches("")) {
            textView.setText("Please fill in blanks.");
        } else {
            Bitmap smallImage = makeSmallerImage(selectedImage,300);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
            byte[] byteArray = outputStream.toByteArray();

            try {
                database = this.openOrCreateDatabase("PriceList",MODE_PRIVATE,null);
                database.execSQL("CREATE TABLE IF NOT EXISTS pricelist (id INTEGER PRIMARY KEY, productname VARCHAR,costprice VARCHAR,saleprice VARCHAR, quantity VARCHAR,image BLOB)");

                String sqlString = "INSERT INTO pricelist (productname, costprice, saleprice, quantity, image) VALUES (?,?,?,?,?)";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                sqLiteStatement.bindString(1,productNameText);
                sqLiteStatement.bindString(2,costPriceText);
                sqLiteStatement.bindString(3,salePriceText);
                sqLiteStatement.bindString(4,quantityNumberText);
                sqLiteStatement.bindBlob(5,byteArray);
                sqLiteStatement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(SecondActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    public void save(View view) {
        saveToDatabase();
    }

    public Bitmap makeSmallerImage(Bitmap image, int maximumSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);
    }

    // Inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_object,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.saveus) {
            saveToDatabase();
        }
        return super.onOptionsItemSelected(item);
    }
}