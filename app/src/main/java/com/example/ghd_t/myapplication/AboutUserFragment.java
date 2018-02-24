package com.example.ghd_t.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUserFragment extends Fragment {



    public AboutUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_about_user, container, false);
        ListView search_reservation_list = (ListView) view.findViewById(R.id.search_reservation);
        ListView user_info_list = (ListView) view.findViewById(R.id.user_info);

        ArrayList<SearchReservationItemData> data_reservation = new ArrayList<>();
        ArrayList<SearchReservationItemData> data_userinfo = new ArrayList<>();

        SearchReservationItemData data_reservation_1 = new SearchReservationItemData("내가 오픈한 클래스 예약 현황");
        SearchReservationItemData data_reservation_2 = new SearchReservationItemData("다른 클래스 예약 현황");

        SearchReservationItemData data_userinfo1 = new SearchReservationItemData("프로필 이미지 변경");
        SearchReservationItemData data_userinfo2 = new SearchReservationItemData("브랜드 인증하기");
        SearchReservationItemData data_userinfo3 = new SearchReservationItemData("닉네임 변경");
        SearchReservationItemData data_userinfo4 = new SearchReservationItemData("로그아웃");

        data_reservation.add(data_reservation_1);
        data_reservation.add(data_reservation_2);

        data_userinfo.add(data_userinfo1);
        data_userinfo.add(data_userinfo2);
        data_userinfo.add(data_userinfo3);
        data_userinfo.add(data_userinfo4);

        ListAdapter adapter_reservation = new ListAdapter(getContext(), R.layout.search_reservation_listview_item, data_reservation);
        search_reservation_list.setAdapter(adapter_reservation);

        ListAdapter adapter_userinfo = new ListAdapter(getContext(), R.layout.search_reservation_listview_item, data_userinfo);
        user_info_list.setAdapter(adapter_userinfo);

        return view;
    }

}
