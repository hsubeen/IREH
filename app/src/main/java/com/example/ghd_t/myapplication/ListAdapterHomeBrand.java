package com.example.ghd_t.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by ghd-t on 2018-02-24.
 */

public class ListAdapterHomeBrand extends BaseAdapter{
    private BrandListItemData listviewitem;
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
            view.setClickable(true);
        }
        listviewitem = data.get(i);

        final ImageView brand_photo = (ImageView) view.findViewById(R.id.brand_photo);
        final TextView brand_title = (TextView) view.findViewById(R.id.brand_title);
        final TextView brand_areaname = (TextView) view.findViewById(R.id.brand_areaname);
        final TextView brand_info = (TextView) view.findViewById(R.id.brand_info);
        final TextView brand_priceinfo_min = (TextView) view.findViewById(R.id.brand_priceinfo_min);
        final TextView brand_priceinfo_max = (TextView) view.findViewById(R.id.brand_priceinfo_max);
        final TextView brand_index = (TextView)view.findViewById(R.id.brand_index);

        brand_photo.setImageBitmap(listviewitem.getIcon());
        brand_title.setText(listviewitem.getBrand_title());
        brand_areaname.setText(listviewitem.getBrand_areaname());
        brand_info.setText(listviewitem.getBrand_fieldname());
        brand_priceinfo_min.setText(listviewitem.getBrand_priceinfo_min());
        brand_priceinfo_max.setText(listviewitem.getBrand_priceinfo_max());
        brand_index.setText(listviewitem.getIndex());

        // layout 자체에 대한 클릭 이벤트.
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //리스트 클릭 시 DetailClassActivity로 전환
                Intent intent = new Intent(v.getContext(),DetailClassActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Index", brand_index.getText().toString());
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
