package com.example.ghd_t.myapplication;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Bitmap bitmap, icon;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private ImageView img;
    private Button btn_gps;
    private GPSInfo gps;
    private Spinner spinner_field, spinner_field2;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase2, mDatabase3;
    private String writePerson,uri,address,brandname,field,phone,weburl, title, contents, money_min, money_max, index, finaladdress;
    private BrandListItemData data_homelist_data;
    private ListView home_brand_list;
    ArrayList<BrandListItemData> home_brandlist = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //폰트 적용
        Typeface typeface = ResourcesCompat.getFont(getContext(),R.font.nanumsquarel);


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        img = (ImageView) view.findViewById(R.id.home_image);
        home_brand_list = (ListView) view.findViewById(R.id.home_brandlist);

        mAuth = FirebaseAuth.getInstance();

//        mDatabase3 = FirebaseDatabase.getInstance().getReference("Regclass");
//        mDatabase3.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
//                    //브랜드 인증 한 유저값만 가져오기
//                    final Object user = objSnapshot.getKey();
//                    Log.v("알림", "브랜드 인증한 유저 " + user);
//
//                    //브랜드 정보
//                    mDatabase3.child(user.toString()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            address = dataSnapshot.child("address").getValue(String.class);
//                            brandname = dataSnapshot.child("brandname").getValue(String.class);
//                            field = dataSnapshot.child("field").getValue(String.class);
//                            phone = dataSnapshot.child("phone").getValue(String.class);
//                            weburl = dataSnapshot.child("weburl").getValue(String.class);
//
//                            //띄어쓰기 기준으로 문자열 자르기, (서울 용산구)
//                            String address_arr[] = address.split(" ");
//                            address = address_arr[1] + " " + address_arr[2];
//
//                            makeClassData(user.toString());
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            Log.e("에러", "Read failed");
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        //모집글 불러오기
        makeClassData();

        btn_gps = view.findViewById(R.id.gps);
        btn_gps.setTypeface(typeface);
        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(!isPermission){
                callPermission();
                return;
            }

            gps = new GPSInfo(getContext());

            // GPS 사용유무 가져오기
            if (gps.isGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                Geocoder gCoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addr = null;
                try{
                    addr = gCoder.getFromLocation(latitude,longitude,1);
                    Address a = addr.get(0);
                    for (int i=0;i <= a.getMaxAddressLineIndex();i++) {
                        Log.v("알림", "AddressLine(" + i + ")" + a.getAddressLine(i) + "\n");
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }
                if (addr != null) {
                    if (addr.size()==0) {
                        Toast.makeText(getContext(),"주소정보 없음", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                // GPS 를 사용할수 없으므로
                gps.showSettingsAlert();
            }
            }
        });

        // 권한 요청을 해야 함
        callPermission();


        // 분야 선택하는 Spinner선언과 event listener 구현 -> 지역 선택
        spinner_field = (Spinner) view.findViewById(R.id.spinner_field);
        String[] str = getResources().getStringArray(R.array.spinnerArray_forSearch);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(),R.layout.spinner_item,str){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getContext().getAssets(), "font/nanumsquarel.ttf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getContext().getAssets(), "font/nanumsquarel.ttf");
                ((TextView) v).setTypeface(externalFont);
                v.setBackgroundColor(Color.WHITE);
                ((TextView) v).setTextColor(Color.BLACK);

                return v;
            }
        };
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_field.setAdapter(adapter);

        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_field.getSelectedItemPosition()>0){
                    Log.v("알림",spinner_field.getSelectedItem().toString()+ " is selected");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 분야 선택하는 Spinner선언과 event listener 구현 -> 분야 선택
        spinner_field2 = (Spinner) view.findViewById(R.id.spinner_field2);
        String[] str2 = getResources().getStringArray(R.array.spinnerArray_forSearch2);
        final ArrayAdapter<String> adapter2= new ArrayAdapter<String>(getContext(),R.layout.spinner_item,str2){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getContext().getAssets(), "font/nanumsquarel.ttf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getContext().getAssets(), "font/nanumsquarel.ttf");
                ((TextView) v).setTypeface(externalFont);
                v.setBackgroundColor(Color.WHITE);
                ((TextView) v).setTextColor(Color.BLACK);

                return v;
            }
        };
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_field2.setAdapter(adapter2);

        spinner_field2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_field2.getSelectedItemPosition()>0){
                    Log.v("알림",spinner_field2.getSelectedItem().toString()+ " is selected");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }


    // 리스트뷰 스크롤 상태에 따른 imageview visibility 조절
    void controlListview(){
        home_brand_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (!home_brand_list.canScrollVertically(-1)) {
                    //최상단
                    btn_gps.setVisibility(View.VISIBLE);
                    spinner_field.setVisibility(View.VISIBLE);
                    spinner_field2.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                } else if (!home_brand_list.canScrollVertically(1)) {
                    //최하단
                    btn_gps.setVisibility(View.GONE);
                    spinner_field.setVisibility(View.GONE);
                    spinner_field2.setVisibility(View.GONE);
                    img.setVisibility(View.GONE);
                } else {
                    //idle
                    btn_gps.setVisibility(View.GONE);
                    spinner_field.setVisibility(View.GONE);
                    spinner_field2.setVisibility(View.GONE);
                    img.setVisibility(View.GONE);
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
    }


    //모집글 불러오기
    void makeClassData(){

        //모집글정보
        mDatabase2 = FirebaseDatabase.getInstance().getReference("WriteClass");
        mDatabase2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot1, String s) {

                Log.v("알림","/.. " + dataSnapshot1.getValue());
                writePerson = dataSnapshot1.child("cu").getValue(String.class);

                mDatabase3 = FirebaseDatabase.getInstance().getReference("Regclass");
                mDatabase3.child(writePerson).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        address = dataSnapshot.child("address").getValue(String.class);
                        brandname = dataSnapshot.child("brandname").getValue(String.class);
                        field = dataSnapshot.child("field").getValue(String.class);
                        phone = dataSnapshot.child("phone").getValue(String.class);
                        weburl = dataSnapshot.child("weburl").getValue(String.class);

                        //띄어쓰기 기준으로 문자열 자르기, (서울 용산구)
                        String address_arr[] = dataSnapshot.child("address").getValue(String.class).split(" ");
                        finaladdress = address_arr[1] + " " + address_arr[2];

                        Log.v("데이터확인", " " +address+brandname+field+phone+weburl);

                        title = dataSnapshot1.child("title").getValue(String.class);
                        contents = dataSnapshot1.child("contents").getValue(String.class);
                        money_min = dataSnapshot1.child("money_min").getValue(String.class);
                        money_max = dataSnapshot1.child("money_max").getValue(String.class);
                        uri = dataSnapshot1.child("img1").getValue(String.class);
                        index = dataSnapshot1.getKey();
                        Log.v("데이터생성", " " +title+finaladdress+contents+money_min+money_max+index);
                        makeData(title,finaladdress,contents,money_min,money_max,index);
                        //findClass();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("에러", "Read failed");
                    }
                });
//
//                title = dataSnapshot.child("title").getValue(String.class);
//                contents = dataSnapshot.child("contents").getValue(String.class);
//                money_min = dataSnapshot.child("money_min").getValue(String.class);
//                money_max = dataSnapshot.child("money_max").getValue(String.class);
//                uri = dataSnapshot.child("img1").getValue(String.class);
//                index = dataSnapshot.getKey();
//                Log.v("데이터생성", " " +title+finaladdress+contents+money_min+money_max+index);
//                makeData(title,finaladdress,contents,money_min,money_max,index);
//                //findClass();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Log.v("알림", "mDatabase2_onChildAdded " + s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Log.v("알림", "mDatabase2_onChildAdded " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //Log.v("알림", "mDatabase2_onChildAdded " + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("에러", "mDatabase2_onChildAdded " + databaseError.getMessage());
            }
        });
    }

    void findClass(){
//        mDatabase3 = FirebaseDatabase.getInstance().getReference("Regclass");
//        mDatabase3.child(writePerson).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                address = dataSnapshot.child("address").getValue(String.class);
//                brandname = dataSnapshot.child("brandname").getValue(String.class);
//                field = dataSnapshot.child("field").getValue(String.class);
//                phone = dataSnapshot.child("phone").getValue(String.class);
//                weburl = dataSnapshot.child("weburl").getValue(String.class);
//
//                //띄어쓰기 기준으로 문자열 자르기, (서울 용산구)
//                String address_arr[] = dataSnapshot.child("address").getValue(String.class).split(" ");
//                finaladdress = address_arr[1] + " " + address_arr[2];
//                Log.v("알림", "주소 " + finaladdress);
//
//            }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.e("에러", "Read failed");
//                }
//            });
    }

    //서버에서 받은 데이터를 리스트뷰에 추가
    void makeData(String s_title,String s_finaladdress, String s_contents, String s_money_min, String s_money_max, String s_index){
//        Thread mThread = new Thread(){
//            @Override
//            public void run() {
//                try{
//                    Log.v("알림","Home_thread 시작");
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
        icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.add);
        data_homelist_data = new BrandListItemData(icon, s_title, s_finaladdress, s_contents, s_money_min ,s_money_max, s_index);
        ListAdapterHomeBrand adapter_homebrand = new ListAdapterHomeBrand(getContext(), R.layout.brandlist_listview_item, home_brandlist);
        home_brandlist.add(data_homelist_data);
        home_brand_list.setAdapter(adapter_homebrand);

        controlListview();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }
}
