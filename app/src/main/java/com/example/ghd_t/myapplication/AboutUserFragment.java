package com.example.ghd_t.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


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

        ArrayList<SearchReservationItemData> data = new ArrayList<>();
        SearchReservationItemData list1 = new SearchReservationItemData("내가 오픈한 클래스 예약 현황");
        SearchReservationItemData list2 = new SearchReservationItemData("다른 클래스 예약 현황");
        data.add(list1);
        data.add(list2);

        ListAdapter adapter = new ListAdapter(getContext(), R.layout.search_reservation_listview_item, data);
        search_reservation_list.setAdapter(adapter);



        //String[] menu_item = {"내가 오픈한 클래스 예약 현황","다른 클래스 예약 현황"};
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.listview_setting,
                menu_item
        );
        search_reservation_list.setAdapter(adapter);
        */
        return view;
    }

}
