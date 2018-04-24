package com.example.ghd_t.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ghd-t on 2018-04-24.
 */

public class BrandAuth extends Activity{
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView brand_address_content;
    private EditText brand_name, brand_web, brand_phone;
    private Button brand_address,send_class_info;
    private Spinner spinner_field;

    public BrandAuth() {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_auth);
        // Inflate the layout for this fragment
        Log.v("알림","RegClassFragment의 onCreateView호출됨");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //클래스 추가 정보가 있으면 게시글 작성페이지로 전환
        mDatabase.child("Regclass").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.v("알림", "Single ValueEventListener : " + snapshot.getValue());
                    if(snapshot.getValue() == null){
                        //브랜드를 추가한 정보가 없음

                    }else{
                        //브랜드를 추가한 정보가 있음

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        brand_name = (EditText) findViewById(R.id.edit_brand_name);
        brand_web = (EditText) findViewById(R.id.edit_brand_web);
        brand_phone = (EditText) findViewById(R.id.edit_phone);
        brand_address_content = (TextView) findViewById(R.id.address);
        brand_address = (Button) findViewById(R.id.edit_address);
        send_class_info = (Button) findViewById(R.id.btn_send_class_info);

        // 분야 선택하는 Spinner선언과 event listener 구현
        spinner_field = (Spinner) findViewById(R.id.spinner_field);
        String[] str = getResources().getStringArray(R.array.spinnerArray);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(BrandAuth.this,R.layout.spinner_item,str);
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

    }

    private void makeDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(BrandAuth.this,R.style.MyAlertDialogStyle);
        alt_bld.setTitle("인증 요청").setIcon(R.drawable.check_dialog_64).setMessage("입력하신 정보로 브랜드 인증을 요청합니다.\n사실과 일치하지 않을 시 삭제조치 될 수 있습니다.").setCancelable(
                false).setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 네 클릭

                        // 현재 로그인한 사용자의 Uid
                        String cu = mAuth.getUid();
                        // 작성한 클래스정보를 RegClassData에 담기
                        BrandAuthData regClassData = new BrandAuthData(brand_name.getText().toString(), brand_web.getText().toString(), brand_phone.getText().toString(),
                                spinner_field.getSelectedItem().toString(), brand_address_content.getText().toString());

                        // DB에 등록
                        mDatabase.child("Regclass").child(cu).setValue(regClassData);
                        // 등록완료 알림창 발생
                        makeConfirmDialog();
                    }
                }).setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 아니오 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    // DB등록 완료 후 알림창
    private void makeConfirmDialog(){
        AlertDialog.Builder alt_bld2 = new AlertDialog.Builder(BrandAuth.this,R.style.MyAlertDialogStyle);
        //alt_bld2.setMessage("\n\n                  인증되었습니다\n\n");

        alt_bld2.setIcon(R.drawable.check_dialog_64).setTitle("인증요청 성공").setMessage("인증되었습니다").setCancelable(
                false);

        AlertDialog alert2 = alt_bld2.create();

        alt_bld2.show();
        new Handler().postDelayed(new Runnable()
        {
            @Override public void run()
            {
                // DB등록 성공 1.5초 후 MainActivity로 전환
                Intent intent = new Intent(BrandAuth.this, MainActivity.class);
                Log.v("알림","클래스정보 추가 완료, MainActivity로 이동");
                startActivity(intent);
            }
        }, 1500);
    }

}
