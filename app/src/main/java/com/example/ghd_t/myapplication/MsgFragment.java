package com.example.ghd_t.myapplication;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MsgFragment extends Fragment {


    public MsgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        Drawable temp = getResources().getDrawable(R.drawable.temp);
        ListView msg_list = (ListView) view.findViewById(R.id.msglist);
        ArrayList<MsgItemData> data_msg = new ArrayList<>();

        MsgItemData data_msg_1 = new MsgItemData(temp, "햄찌씨", "문의 있네요!");
        MsgItemData data_msg_2 = new MsgItemData(temp, "홍수빈", "여보쇼");
        MsgItemData data_msg_3 = new MsgItemData(temp, "케이크조아", "케이크사고싶은데요오옹");

        data_msg.add(data_msg_1);
        data_msg.add(data_msg_2);
        data_msg.add(data_msg_3);

        ListAdapterMsg adapter_msg = new ListAdapterMsg(getContext(), R.layout.msg_listview_item, data_msg);
        msg_list.setAdapter(adapter_msg);
/*
        msg_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("알림","MsgFragment -> ChatActivity전환 완료");
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });

         */
        return view;
    }
}
