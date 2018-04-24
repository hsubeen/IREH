package com.example.ghd_t.myapplication;


import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    private GPSInfo gps;


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

        Button btn_gps = view.findViewById(R.id.gps);
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

        callPermission();  // 권한 요청을 해야 함


        // 분야 선택하는 Spinner선언과 event listener 구현 -> 지역 선택
        final Spinner spinner_field = (Spinner) view.findViewById(R.id.spinner_field);
        String[] str = getResources().getStringArray(R.array.spinnerArray_forSearch);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(),R.layout.spinner_item,str);
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
        final Spinner spinner_field2 = (Spinner) view.findViewById(R.id.spinner_field2);
        String[] str2 = getResources().getStringArray(R.array.spinnerArray_forSearch2);
        final ArrayAdapter<String> adapter2= new ArrayAdapter<String>(getContext(),R.layout.spinner_item,str2);
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
