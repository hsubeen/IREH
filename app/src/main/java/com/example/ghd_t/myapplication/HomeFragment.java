package com.example.ghd_t.myapplication;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
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
        final ImageView img = (ImageView) view.findViewById(R.id.home_image);

        final ListView home_brand_list = (ListView) view.findViewById(R.id.home_brandlist);
        ArrayList<BrandListItemData> data_brandlist = new ArrayList<>();
        BrandListItemData data_brandlist_1 = new BrandListItemData(temp, "엔플레노", "서울시 노원구", "안녕하세요. 이것은 열심히 쥐어 짜는 것입니다. 안녕하시죠???", "180,000~" );
        BrandListItemData data_brandlist_2 = new BrandListItemData(temp, "뚜비네","경기도 성남시","안녕하세요! 여기는 뚜비공간이에요. 놀러오세요.", "30,000");
        BrandListItemData data_brandlist_3 = new BrandListItemData(temp, "브레드엔케이크","서울시 양천구","빵만들러와요!", "20,000~");

        data_brandlist.add(data_brandlist_1);
        data_brandlist.add(data_brandlist_2);
        data_brandlist.add(data_brandlist_3);

        data_brandlist.add(data_brandlist_1);
        data_brandlist.add(data_brandlist_2);
        data_brandlist.add(data_brandlist_3);

        data_brandlist.add(data_brandlist_1);
        data_brandlist.add(data_brandlist_2);
        data_brandlist.add(data_brandlist_3);

        data_brandlist.add(data_brandlist_1);
        data_brandlist.add(data_brandlist_2);
        data_brandlist.add(data_brandlist_3);


        ListAdapterHomeBrand adapter_homebrand = new ListAdapterHomeBrand(getContext(), R.layout.brandlist_listview_item, data_brandlist);
        home_brand_list.setAdapter(adapter_homebrand);

        // 리스트뷰 스크롤 상태에 따른 imageview visibility 조절
        home_brand_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (!home_brand_list.canScrollVertically(-1)) {
                    //최상단
                    img.setVisibility(View.VISIBLE);
                    Log.v("알림","home list 최상단. image띄우기");
                } else if (!home_brand_list.canScrollVertically(1)) {
                    //최하단
                    img.setVisibility(View.GONE);
                    Log.v("알림","home list 최하단. image없애기");
                } else {
                    //idle
                    img.setVisibility(View.GONE);
                    Log.v("알림","home list idle. image없애기");
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        return view;
    }

}
