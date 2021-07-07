package com.emirhan.mypricelist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emirhan.mypricelist.Model.PriceList;
import com.emirhan.mypricelist.R;

import java.io.Serializable;
import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {


    Context context;
    ArrayList<PriceList> arrayList;
    public MyAdapter(Context context, ArrayList<PriceList> arrayList)
    {
      this.context=context;
      this.arrayList=arrayList;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mycustomlistview,null);
            TextView t2_name = (TextView)convertView.findViewById(R.id.name_txt);
            TextView t3_age = (TextView)convertView.findViewById(R.id.age_txt);
            ImageView img = (ImageView)convertView.findViewById(R.id.imageView3);

            PriceList priceList = arrayList.get(position);


        t2_name.setText(priceList.getProductName());
            t3_age.setText(priceList.getSalePrice());
            img.setImageBitmap(priceList.getImg());
        return convertView;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }
}
