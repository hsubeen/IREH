package com.example.ghd_t.myapplication;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Drawable temp = getResources().getDrawable(R.drawable.temp);


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ListView home_brand_list = (ListView) view.findViewById(R.id.home_brandlist);
        ArrayList<BrandListItemData> data_brandlist = new ArrayList<>();
        BrandListItemData data_brandlist_1 = new BrandListItemData(temp, "엔플레노", "서울시 노원구", "요리, 베이킹", "80,000" );
        data_brandlist.add(data_brandlist_1);
        ListAdapterHomeBrand adapter_homebrand = new ListAdapterHomeBrand(getContext(), R.layout.brandlist_listview_item, data_brandlist);
        home_brand_list.setAdapter(adapter_homebrand);

        return view;
    }

}
