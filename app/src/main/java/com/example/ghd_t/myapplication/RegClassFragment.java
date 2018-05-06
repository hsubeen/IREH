package com.example.ghd_t.myapplication;


import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegClassFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase1, mDatabase2;
    private Button btn_write_class;
    private String address,brandname,field,phone,weburl, title, contents, money_min, money_max;
    private BrandListItemData data_brandlist_1,data_brandlist_2,data_brandlist_3;
    private ListView home_brand_list;
    ArrayList<BrandListItemData> data_brandlist = new ArrayList<>();
    public RegClassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("알림","받은 데이터 : " + AddressData.getInstance().getAddress());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("알림","RegClassFragment의 onCreateView호출됨");
        View view = inflater.inflate(R.layout.fragment_reg_class, container, false);
        home_brand_list = (ListView) view.findViewById(R.id.reg_class_my);
        mAuth = FirebaseAuth.getInstance();
        final String cu = mAuth.getUid();



        //브랜드 정보
        mDatabase1 = FirebaseDatabase.getInstance().getReference("Regclass");
        mDatabase1.child(cu).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("알림", "datasnapshot value : " + dataSnapshot.getValue());
                address = dataSnapshot.child("address").getValue(String.class);
                brandname = dataSnapshot.child("brandname").getValue(String.class);
                field = dataSnapshot.child("field").getValue(String.class);
                phone = dataSnapshot.child("phone").getValue(String.class);
                weburl = dataSnapshot.child("weburl").getValue(String.class);

                //띄어쓰기 기준으로 문자열 자르기, (서울 용산구)
                String address_arr[] = address.split(" ");
                address = address_arr[1] + " " + address_arr[2];

                Log.v("알림", "address " + address);
                Log.v("알림", "brandname " + brandname);
                Log.v("알림", "field " + field);
                Log.v("알림", "phone " + phone);
                Log.v("알림", "weburl " + weburl);
                makeData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("에러", "Read failed");
            }
        });

        //모집글정보
        mDatabase2 = FirebaseDatabase.getInstance().getReference("WriteClass");
        mDatabase2.child(cu).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("알림", "mDatabase2_onChildAdded " + dataSnapshot.getValue());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("알림", "mDatabase2_onChildAdded " + s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("알림", "mDatabase2_onChildAdded " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v("알림", "mDatabase2_onChildAdded " + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("에러", "mDatabase2_onChildAdded " + databaseError.getMessage());
            }
        });



        btn_write_class = view.findViewById(R.id.write_class);
        btn_write_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), WriteClassActivity.class);
                startActivity(intent);
                Log.v("알림","글쓰기 창으로 전환");
            }
        });


        return view;
    }

    void makeData(){
        Drawable temp = getResources().getDrawable(R.drawable.temp);
        data_brandlist_1 = new BrandListItemData(temp, brandname, address, "안녕하세요. 이것은 열심히 쥐어 짜는 것입니다. 안녕하시죠???", "180,000" ,"90,000");
        data_brandlist_2 = new BrandListItemData(temp, "뚜비네","경기도 성남시","안녕하세요! 여기는 뚜비공간이에요. 놀러오세요.", "30,000","90,000");
        data_brandlist_3 = new BrandListItemData(temp, "브레드엔케이크","서울시 양천구","빵만들러와요!", "20,000","90,000");

        data_brandlist.add(data_brandlist_1);
        data_brandlist.add(data_brandlist_2);
        data_brandlist.add(data_brandlist_3);
        ListAdapterHomeBrand adapter_homebrand = new ListAdapterHomeBrand(getContext(), R.layout.brandlist_listview_item, data_brandlist);
        home_brand_list.setAdapter(adapter_homebrand);
    }
}
