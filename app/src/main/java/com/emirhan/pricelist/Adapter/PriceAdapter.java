package com.emirhan.pricelist.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emirhan.pricelist.Modal.Price;
import com.emirhan.pricelist.View.SecondaryActivity;
import com.emirhan.pricelist.databinding.RecyclerRowBinding;

import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.PriceHolder> {

    List<Price> priceList;
    public PriceAdapter(List<Price> priceList) {
        this.priceList = priceList;
    }
    @NonNull
    @Override
    public PriceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new PriceHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceHolder holder,@SuppressLint("RecyclerView") int position) {
        holder.recyclerRowBinding.recyclerViewTextTextView.setText(priceList.get(position).name);
        Bitmap bitmap = BitmapFactory.decodeByteArray(priceList.get(position).image,0,priceList.get(position).image.length);
        holder.recyclerRowBinding.imageView2.setImageBitmap(bitmap);

        holder.recyclerRowBinding.recyclerViewTextTextView2.setText(priceList.get(position).salePrice + " $ "  + "(" + priceList.get(position).quantity + ")");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), SecondaryActivity.class);
            intent.putExtra("info","old");
            intent.putExtra("price",priceList.get(position));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    public static class PriceHolder extends RecyclerView.ViewHolder {
        RecyclerRowBinding recyclerRowBinding;

        public PriceHolder(@NonNull RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }
    }
}
