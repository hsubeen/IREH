package com.example.ghd_t.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.File;
import java.util.concurrent.TimeoutException;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegClassFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView brand_address_content;
    private EditText brand_name, brand_web, brand_phone;
    private Button brand_address,send_class_info;
    private Spinner spinner_field;

    public RegClassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("알림","RegClassFragment의 onResume호출됨");
        brand_address_content.setText(AddressData.getInstance().getAddress());
        Log.v("알림","받은 데이터 : " + AddressData.getInstance().getAddress());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("알림","RegClassFragment의 onCreateView호출됨");
        View view = inflater.inflate(R.layout.fragment_reg_class, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        brand_name = (EditText) view.findViewById(R.id.edit_brand_name);
        brand_web = (EditText) view.findViewById(R.id.edit_brand_web);
        brand_phone = (EditText) view.findViewById(R.id.edit_phone);
        brand_address_content = (TextView) view.findViewById(R.id.address);
        brand_address = (Button) view.findViewById(R.id.edit_address);
        send_class_info = (Button) view.findViewById(R.id.btn_send_class_info);

        // 분야 선택하는 Spinner선언과 event listener 구현
        spinner_field = (Spinner) view.findViewById(R.id.spinner_field);
        String[] str = getResources().getStringArray(R.array.spinnerArray);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(),R.layout.spinner_item,str);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_field.setAdapter(adapter);

        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if(spinner_field.getSelectedItemPosition()>0){
                   Log.v("알림",spinner_field.getSelectedItem().toString()+ "is selected");
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // 연락처 입력시 하이픈(-) 자동 입력.
        brand_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        // 주소 입력 버튼 클릭 시 daum 우편 서비스 띄우는 webview 실행
        brand_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DaumWebViewActivity.class);
                startActivity(intent);
            }
        });


        // 인증요청 버튼 클릭 이벤트 리스너
        send_class_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialog();
            }
        });

        return view;
    }



    private void makeDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        alt_bld.setMessage("입력하신 정보로 브랜드 인증 요청하시겠습니까?").setCancelable(
                false).setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 네 클릭

                        // 현재 로그인한 사용자의 Uid
                        String cu = mAuth.getUid();
                        // 작성한 클래스정보를 RegClassData에 담기
                        RegClassData regClassData = new RegClassData(brand_name.getText().toString(), brand_web.getText().toString(), brand_phone.getText().toString(),
                                spinner_field.getSelectedItem().toString(), brand_address_content.getText().toString());

                        // DB에 등록
                        mDatabase.child("Regclass").child(cu).setValue(regClassData);
                    }
                }).setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 아니오 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();

        // 대화창 클릭시 뒷 배경 어두워지는 것 막기
        //alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // 대화창 제목 설정
        alert.setTitle("인증 요청");

        // 대화창 아이콘 설정
        alert.setIcon(R.drawable.check_dialog_64);

        // 대화창 배경 색 설정
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255,62,79,92)));


        alert.show();
    }



}
