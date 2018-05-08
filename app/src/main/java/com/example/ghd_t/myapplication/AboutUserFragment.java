package com.example.ghd_t.myapplication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUserFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView user_name;
    private String cu, uid, value;
    private Typeface typeface;
    private Button button;
    Bitmap bitmap;

    public AboutUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        final FirebaseUser user = mAuth.getCurrentUser();
        typeface = ResourcesCompat.getFont(getContext(),R.font.nanumsquarel);
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
                    Log.v("알림","사용자 프로필 uri " + user.getPhotoUrl().toString());
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

        user_name = view.findViewById(R.id.user_name);


        SharedPreferences mPref = getContext().getSharedPreferences("BrandAuth",0);
        cu = mPref.getString("userName","");
        //현재 로그인한 사용자 이름으로 setting
        user_name.setText(cu);

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
                switch (i){
                    case 1:
                        //브랜드 인증 정보
                        Intent intent = new Intent(getActivity(), BrandAuth.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //닉네임 변경
                        final EditText et = new EditText(getContext());
                        final TextView tv = new TextView(getContext());
                        et.setSingleLine(true);
                        tv.setTypeface(typeface);
                        et.setTypeface(typeface);
                        tv.setText("닉네임은 2~8글자만 등록할 수 있습니다.");

                        value = "";
                        LinearLayout container = new LinearLayout(getContext());
                        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                        tv.setLayoutParams(params);
                        et.setLayoutParams(params);
                        et.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                if(et.getText().length() >= 2 && et.getText().length() <= 8){
                                    button.setEnabled(true);
                                }else{
                                    button.setEnabled(false);
                                }
                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                        container.setOrientation(LinearLayout.VERTICAL);
                        container.addView(et);
                        container.addView(tv);
                        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext(),R.style.MyAlertDialogStyle);
                        alt_bld.setTitle("닉네임 변경").setMessage("변경할 닉네임을 입력하세요").setIcon(R.drawable.check_dialog_64).setCancelable(
                        false).setView(container).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                value = et.getText().toString();
                                user_name.setText(value);
                                mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                                Map<String , Object> newvalue = new HashMap<>();
                                newvalue.put("/userName/", value);
                                mDatabase.child(uid).updateChildren(newvalue);

                            }
                        }).setNegativeButton("취소", null);
                        final AlertDialog alert = alt_bld.create();
                        alert.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                if(value.length() < 2)
                                {
                                    button = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                                    if (button != null) {
                                        button.setEnabled(false);
                                    }
                                }
                            }
                        });
                        alert.show();
                        break;
                }
            }
        });
        return view;
    }



}
