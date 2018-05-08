package com.example.ghd_t.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegClassFragment extends Fragment {
    private DatabaseReference mDatabase1, mDatabase2,mDatabase3;
    private Button btn_write_class;
    private String uri, address,brandname,field,phone,weburl, title, contents, money_min, money_max;
    private BrandListItemData data_brandlist_data;
    private ListView home_brand_list;
    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
        final String cu = mAuth.getUid();

        home_brand_list = (ListView) view.findViewById(R.id.reg_class_my);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences mPrefs = getContext().getSharedPreferences("BrandAuth",0);
        final SharedPreferences.Editor mEdit = mPrefs.edit();

        mDatabase3 = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase3.child(cu).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mEdit.putString("userID",cu);
                mEdit.putString("userName", dataSnapshot.child("userName").getValue(String.class));
                mEdit.commit();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("에러", "Read failed");
            }
        });

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


                if(dataSnapshot.getValue()==null){
                    mEdit.putInt("exists",0);
                }else{
                    mEdit.putInt("exists",1);
                    //현재 로그인한 유저의 브랜드 인증 정보를 SharedPreferences로 저장
                    mEdit.putString("address",address);
                    mEdit.putString("brandname",brandname);
                    mEdit.putString("field",field);
                    mEdit.putString("phone",phone);
                    mEdit.putString("weburl",weburl);
                }
                mEdit.commit();

                //띄어쓰기 기준으로 문자열 자르기, (서울 용산구)
                if(address!=null) {
                    String address_arr[] = address.split(" ");
                    address = address_arr[1] + " " + address_arr[2];
                }
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
//        Thread mThread = new Thread(){
//            @Override
//            public void run() {
//                try{
//                    Log.v("알림","Reg_thread 시작");
//                    URL url = new URL(uri);
//                    HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
//                    conn.setDoInput(true);
//                    conn.connect();
//
//                    InputStream is = conn.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(is);
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        mThread.start();
//        try{
//            mThread.join();
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }

        //이 부분은 Firebase storage 사용량때문에 임시..
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.add);
        data_brandlist_data = new BrandListItemData(icon, title, address, contents, money_min ,money_max);
        data_brandlist.add(data_brandlist_data);
        ListAdapterHomeBrand adapter_homebrand = new ListAdapterHomeBrand(getContext(), R.layout.brandlist_listview_item, data_brandlist);
        home_brand_list.setAdapter(adapter_homebrand);
    }

}

