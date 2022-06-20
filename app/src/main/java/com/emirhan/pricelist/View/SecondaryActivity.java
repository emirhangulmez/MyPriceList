package com.emirhan.pricelist.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.emirhan.pricelist.Modal.Price;
import com.emirhan.pricelist.R;
import com.emirhan.pricelist.RoomDB.PriceDao;
import com.emirhan.pricelist.RoomDB.PriceDatabase;
import com.emirhan.pricelist.databinding.ActivitySecondaryBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SecondaryActivity extends AppCompatActivity {

    private ActivitySecondaryBinding binding;
    PriceDatabase db;
    PriceDao priceDao;
    ActivityResultLauncher<String> permissionLauncher;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Bitmap selectedImage;
    String name,costPrice,salePrice,quantity, intentInfo;
    Price selectedPrice;
    int imageCheck;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        quantity = binding.quantityEditText.getText().toString();
        name = binding.nameEditText.getText().toString();
        costPrice = binding.costPriceEditText.getText().toString();
        salePrice = binding.salePriceEditText.getText().toString();


        db = Room.databaseBuilder(getApplicationContext(),PriceDatabase.class,"Prices").build();
        priceDao = db.priceDao();

        registerLauncher();

        Intent intent = getIntent();

        intentInfo = intent.getStringExtra("info");

        if (intentInfo.matches("new")) {
            binding.deleteButton.setVisibility(View.INVISIBLE);
            binding.updateButton.setVisibility(View.INVISIBLE);
            binding.salePriceEditText.setText("");
            binding.nameEditText.setText("");
            binding.costPriceEditText.setText("");
            binding.quantityEditText.setText("");

            binding.imageView.setImageResource(R.drawable.selectimage);


        } else if(intentInfo.matches("old")) {
            selectedPrice = (Price) intent.getParcelableExtra("price");

            binding.saveButton.setVisibility(View.INVISIBLE);



            binding.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    compositeDisposable.add(priceDao.delete(selectedPrice)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(SecondaryActivity.this::handleResponse)
                    );
                }
            });

            binding.updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity = binding.quantityEditText.getText().toString();
                    name = binding.nameEditText.getText().toString();
                    costPrice = binding.costPriceEditText.getText().toString();
                    salePrice = binding.salePriceEditText.getText().toString();
                    compositeDisposable.add(priceDao.updatePrice(selectedPrice.id,name,costPrice,salePrice,quantity)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(SecondaryActivity.this::handleResponse)
                    );
                }
            });

            binding.nameEditText.setText(selectedPrice.name);
            binding.salePriceEditText.setText(selectedPrice.salePrice);
            binding.costPriceEditText.setText(selectedPrice.costPrice);
            binding.quantityEditText.setText(selectedPrice.quantity);
            Bitmap bitmap = BitmapFactory.decodeByteArray(selectedPrice.image,0,selectedPrice.image.length);
            binding.imageView.setImageBitmap(bitmap);

            imageCheck = 0;
        }
    }

    public void save(View view) { productSave(); }

    public void productSave() {

        name = binding.nameEditText.getText().toString();
        costPrice = binding.costPriceEditText.getText().toString();
        salePrice = binding.salePriceEditText.getText().toString();
        quantity = binding.quantityEditText.getText().toString();


        if (name.matches("") || costPrice.matches("") || salePrice.matches("") || quantity.matches("")) {
            binding.resultText.setText("Please fill in the blanks!");
        } else if (imageCheck == 0) {
            binding.resultText.setText("Please add a picture!");
        } else {
                Bitmap smallImage = makeSmallerImage(selectedImage,300);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                byte[] byteArray = outputStream.toByteArray();

                Price price = new Price(name,costPrice,salePrice,quantity,byteArray);

                compositeDisposable.add(priceDao.insert(price)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(SecondaryActivity.this::handleResponse)
                );
            }
        }


    private void handleResponse() {
        Intent intent = new Intent(SecondaryActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

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

    public void selectImage(View view) {
        Intent intent = getIntent();
        intentInfo = intent.getStringExtra("info");

        if (intentInfo.matches("new")) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission need to gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }

                    }).show();

                } else {
                    // Request Permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else {
                // Gallery
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);

            }
        } else {

        }



}
    //Permission Launcher
    private void registerLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
           if (result.getResultCode() == RESULT_OK) {
               Intent intentFromResult = result.getData();
               if (intentFromResult != null) {
                   Uri imageData = intentFromResult.getData();

                   try {
                       if (Build.VERSION.SDK_INT >= 28) {
                           ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),imageData);
                           selectedImage = ImageDecoder.decodeBitmap(source);
                           binding.imageView.setImageBitmap(selectedImage);
                           imageCheck = 200;
                       } else {
                           selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(),imageData);
                           binding.imageView.setImageBitmap(selectedImage);
                           imageCheck = 200;
                       }
                   }catch (Exception e ) {
                       e.printStackTrace();
                   }
               }
           }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
               if (result) {
                   // Permission Granted
                   Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                   activityResultLauncher.launch(intentToGallery);
               } else {
                   // Permission Denied
                   Toast.makeText(SecondaryActivity.this, "Permission Needed!", Toast.LENGTH_LONG).show();
               }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();

        intentInfo = intent.getStringExtra("info");

        if (intentInfo.matches("new")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.save_product,menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_product) {
            productSave();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}