package com.example.ghd_t.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUserFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    Bitmap bitmap;

    public AboutUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final FirebaseUser user = mAuth.getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_about_user, container, false);
        final ListView search_reservation_list = (ListView) view.findViewById(R.id.search_reservation);
        final ListView user_info_list = (ListView) view.findViewById(R.id.user_info);


        //접속한 사용자 프로필 이미지띄우기
        CircularImageView user_profile = view.findViewById(R.id.user_profile);

        Thread mThread= new Thread(){
            @Override
            public void run() {
                try{
                    //uri정보를 통해 Bitmap으로 변환하여 보여줌
                    URL url = new URL(user.getPhotoUrl().toString());
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException ee) {
                    Log.e("알림" , "bitmap 이미지 변환 실패");
                    ee.printStackTrace();
                }catch (IOException e){
                    Log.e("알림" , "bitmap 이미지 변환 실패");
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try{
            mThread.join();
            //변환한 bitmap이미지로 setting
            user_profile.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            Log.e("알림" , "bitmap 이미지 설정 실패");
            e.printStackTrace();
        }

        TextView user_name = view.findViewById(R.id.user_name);
        //현재 로그인한 사용자 이름으로 setting
        user_name.setText(user.getDisplayName());

        final ArrayList<SearchReservationItemData> data_reservation = new ArrayList<>();
        ArrayList<UserInfoItemData> data_userinfo = new ArrayList<>();

        final SearchReservationItemData data_reservation_1 = new SearchReservationItemData("내가 오픈한 클래스 예약 현황");
        SearchReservationItemData data_reservation_2 = new SearchReservationItemData("다른 클래스 예약 현황");


        UserInfoItemData data_userinfo1 = new UserInfoItemData("프로필 이미지 변경");
        UserInfoItemData data_userinfo2 = new UserInfoItemData("브랜드 인증 정보");
        UserInfoItemData data_userinfo3 = new UserInfoItemData("닉네임 변경");

        data_reservation.add(data_reservation_1);
        data_reservation.add(data_reservation_2);

        data_userinfo.add(data_userinfo1);
        data_userinfo.add(data_userinfo2);
        data_userinfo.add(data_userinfo3);


        ListAdapterSearchReservation adapter_reservation = new ListAdapterSearchReservation(getContext(), R.layout.search_reservation_listview_item, data_reservation);
        search_reservation_list.setAdapter(adapter_reservation);


        ListAdapterUserInfo adapter_userinfo = new ListAdapterUserInfo(getContext(), R.layout.search_reservation_listview_item, data_userinfo);
        user_info_list.setAdapter(adapter_userinfo);
        user_info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("알림","선택된 position " + i);
                if(i==1){
                    //브랜드 인증하지 않았으면 인증창으로 가기
                    //인
                    Intent intent = new Intent(getActivity(), BrandAuth.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }



}
