package com.example.ghd_t.myapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUserFragment extends Fragment {

    private FirebaseAuth mAuth;
    Bitmap bitmap;

    public AboutUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_about_user, container, false);
        ListView search_reservation_list = (ListView) view.findViewById(R.id.search_reservation);
        final ListView user_info_list = (ListView) view.findViewById(R.id.user_info);


        //접속한 사용자 프로필 이미지띄우기
        CircularImageView user_profile = view.findViewById(R.id.user_profile);

        Thread mThread= new Thread(){
            @Override
            public void run() {
                try{
                    //uri정보를 통해 Bitmap으로 변환하여 보여줌
                    URL url = new URL(user.getPhotoUrl().toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (MalformedURLException ee) {
                    ee.printStackTrace();
                }catch (IOException e){
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
            e.printStackTrace();
        }

        TextView user_name = view.findViewById(R.id.user_name);
        //현재 로그인한 사용자 이름으로 setting
        user_name.setText(user.getDisplayName());

        ArrayList<SearchReservationItemData> data_reservation = new ArrayList<>();
        ArrayList<UserInfoItemData> data_userinfo = new ArrayList<>();

        SearchReservationItemData data_reservation_1 = new SearchReservationItemData("내가 오픈한 클래스 예약 현황");
        SearchReservationItemData data_reservation_2 = new SearchReservationItemData("다른 클래스 예약 현황");

        UserInfoItemData data_userinfo1 = new UserInfoItemData("프로필 이미지 변경");
        UserInfoItemData data_userinfo2 = new UserInfoItemData("브랜드 정보");
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


        return view;
    }



}
