package com.example.ghd_t.myapplication;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by ghd-t on 2018-02-24.
 */

public class ListAdapter extends BaseAdapter {
    private ArrayList<SearchReservationItemData> data = new ArrayList<>();
    private LayoutInflater inflater;
    private int layout;

    public ListAdapter(Context context, int layout, ArrayList<SearchReservationItemData> data){
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
       SearchReservationItemData listviewitem = data.get(i);
       TextView listItem = (TextView) view.findViewById(R.id.reservation_list);
       listItem.setText(data.get(i).getMenu_title());

        return view;
    }

}
