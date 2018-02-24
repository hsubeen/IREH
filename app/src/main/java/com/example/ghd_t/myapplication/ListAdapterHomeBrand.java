package com.example.ghd_t.myapplication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by ghd-t on 2018-02-24.
 */

public class ListAdapterHomeBrand extends BaseAdapter{

    private ArrayList<BrandListItemData> data = new ArrayList<>();
    private LayoutInflater inflater;
    private int layout;

    public ListAdapterHomeBrand(Context context, int layout, ArrayList<BrandListItemData> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view =inflater.inflate(layout,viewGroup,false);
        }
        BrandListItemData listviewitem = data.get(i);

        final ImageView brand_photo = (ImageView) view.findViewById(R.id.brand_photo);
        final TextView brand_title = (TextView) view.findViewById(R.id.brand_title);
        final TextView brand_areaname = (TextView) view.findViewById(R.id.brand_areaname);
        final TextView brand_info = (TextView) view.findViewById(R.id.brand_info);
        final TextView brand_priceinfo = (TextView) view.findViewById(R.id.brand_priceinfo);

        brand_photo.setImageDrawable(data.get(i).getIcon());
        brand_title.setText(data.get(i).getBrand_title());
        brand_areaname.setText(data.get(i).getBrand_areaname());
        brand_info.setText(data.get(i).getBrand_fieldname());
        brand_priceinfo.setText(data.get(i).getBrand_priceinfo());

        brand_photo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(v.getContext(), brand_title.getText().toString(),Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }
}
