package com.example.ghd_t.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegClassFragment extends Fragment {
    private Bitmap bitmap;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase1, mDatabase2;
    private Button btn_write_class;
    private String uri, address,brandname,field,phone,weburl, title, contents, money_min, money_max;
    private BrandListItemData data_brandlist_data;
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
                makeClassData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("에러", "Read failed");
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
    void runThread(){
        Thread mThread = new Thread(){
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(uri);
                    conn = (HttpURLConnection)
                            url.openConnection();
                    conn.setDoInput(true);
                    conn.setConnectTimeout(8000);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(conn!=null)
                        conn.disconnect();
                }
            }
        };
        mThread.start();
    }
    void makeClassData(){

        final String cu = mAuth.getUid();
        //모집글정보
        mDatabase2 = FirebaseDatabase.getInstance().getReference("WriteClass");
        mDatabase2.child(cu).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Log.v("알림", "mDatabase2_onChildAdded " + dataSnapshot.getValue());

                title = dataSnapshot.child("title").getValue(String.class);
                contents = dataSnapshot.child("contents").getValue(String.class);
                money_min = dataSnapshot.child("money_min").getValue(String.class);
                money_max = dataSnapshot.child("money_max").getValue(String.class);
                uri = dataSnapshot.child("img1").getValue(String.class);

                Log.v("알림", "title " + title);
                Log.v("알림", "contents " + contents);
                Log.v("알림", "money_min " + money_min);
                Log.v("알림", "money_max " + money_max);
                Log.v("알림", "uri " + uri);
                runThread();
                makeData();
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
    }
    void makeData(){
        Thread mThread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL(uri);
                    HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        data_brandlist_data = new BrandListItemData(bitmap, title, address, contents, money_min ,money_max);
        data_brandlist.add(data_brandlist_data);
        ListAdapterHomeBrand adapter_homebrand = new ListAdapterHomeBrand(getContext(), R.layout.brandlist_listview_item, data_brandlist);
        home_brand_list.setAdapter(adapter_homebrand);
    }



}
