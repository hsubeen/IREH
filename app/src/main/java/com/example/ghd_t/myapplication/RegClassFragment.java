package com.example.ghd_t.myapplication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegClassFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button btn_write_class;

    //private TextView brand_address_content;

    public RegClassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("알림","RegClassFragment의 onResume호출됨");
        //brand_address_content.setText(AddressData.getInstance().getAddress());
        Log.v("알림","받은 데이터 : " + AddressData.getInstance().getAddress());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("알림","RegClassFragment의 onCreateView호출됨");
        View view = inflater.inflate(R.layout.fragment_reg_class, container, false);

        btn_write_class = view.findViewById(R.id.write_class);
        btn_write_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("알림","글쓰기 버튼 클릭");
            }
        });
        Drawable temp = getResources().getDrawable(R.drawable.temp);
        final ListView home_brand_list = (ListView) view.findViewById(R.id.reg_class_my);
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
        return view;
    }
}
