package com.example.ghd_t.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ghd-t on 2018-04-24.
 */

public class BrandAuth extends Activity{
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView brand_address_content, brandname, brandweb, brandphone, brandaddr, brandfield, info_text;
    private EditText brand_name, brand_web, brand_phone;
    private Button brand_address,send_class_info;
    private Spinner spinner_field;
    private SharedPreferences mPref;
    private String cu_addr,cu_brand,cu_web,cu_field,cu_phone;
    int editFlag = 0;
    public BrandAuth() {
        // Required empty public constructor
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mPref.getInt("exists",0) == 1 && editFlag == 0){
            //주소정보 있음. 불러오기
            Log.v("알림","이미 정보가 있습니다. 주소를 불러옵니다.");
            editFlag = 1;
            brand_address_content.setText(cu_addr);
        }else if(mPref.getInt("exists",0) == 1 && editFlag==1){
            Log.v("알림","주소를 수정합니다.");
            brand_address_content.setText(AddressData.getInstance().getAddress());
        }else if (mPref.getInt("exists",0) == 0){
            Log.v("알림","webview로부터 주소정보를 생성합니다.");
            brand_address_content.setText(AddressData.getInstance().getAddress());
        }

    }

    //브랜드인증 글 작성 중 뒤로가기 버튼 눌렸을 때
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(BrandAuth.this,R.style.MyAlertDialogStyle);
                alt_bld.setTitle("글을 작성중입니다.").setMessage("작성을 중지하시겠습니까?").setIcon(R.drawable.check_dialog_64).setCancelable(
                        false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //현재 액티비티 종료
                                finish();
                            }
                        }).setNegativeButton("아니오", null);
                AlertDialog alert = alt_bld.create();
                alert.show();
        }
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_auth);
        // Inflate the layout for this fragment
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String cu = mAuth.getUid();

        brandname = (TextView) findViewById(R.id.brand_name_text);
        brandweb = (TextView) findViewById(R.id.brand_web_text);
        brandaddr = (TextView)findViewById(R.id.addr_text);
        brandfield = (TextView) findViewById(R.id.field_text);
        brandphone = (TextView) findViewById(R.id.phone_text);
        info_text = (TextView) findViewById(R.id.info_text);

        brand_name = (EditText) findViewById(R.id.edit_brand_name);
        brand_web = (EditText) findViewById(R.id.edit_brand_web);
        brand_phone = (EditText) findViewById(R.id.edit_phone);
        brand_address_content = (TextView) findViewById(R.id.address);
        brand_address = (Button) findViewById(R.id.edit_address);
        send_class_info = (Button) findViewById(R.id.btn_send_class_info);
        spinner_field = (Spinner) findViewById(R.id.spinner_field);

        //폰트적용
        Typeface typeface = ResourcesCompat.getFont(this,R.font.nanumsquarel);
        brand_name.setTypeface(typeface);
        brand_web.setTypeface(typeface);
        brand_phone.setTypeface(typeface);
        brand_address_content.setTypeface(typeface);
        brand_address.setTypeface(typeface);
        send_class_info.setTypeface(typeface);

        typeface = ResourcesCompat.getFont(this,R.font.nanumsquareb);
        brandname.setTypeface(typeface);
        brandweb.setTypeface(typeface);
        brandphone.setTypeface(typeface);
        brandaddr.setTypeface(typeface);
        brandfield.setTypeface(typeface);
        info_text.setTypeface(typeface);


        // 분야 선택하는 Spinner event listener 구현
        String[] str = getResources().getStringArray(R.array.spinnerArray);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(BrandAuth.this,R.layout.spinner_item,str){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "font/nanumsquarel.ttf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "font/nanumsquarel.ttf");
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


        // 현재 로그인한 사용자의 브랜드 인증 정보를 SharedPreferences를 통해 가져옴
        mPref = getSharedPreferences("BrandAuth",0);
        cu_brand = mPref.getString("brandname","");
        cu_web = mPref.getString("weburl", "");
        cu_addr = mPref.getString("address", "");
        cu_field = mPref.getString("field","");
        cu_phone = mPref.getString("phone","");

        // 브랜드 인증 정보가 있을 때, 조회/수정할 수 있도록
        if(mPref.getInt("exists",0) == 1){
            info_text.setText("등록한 브랜드 정보를 조회/수정합니다.");
            brand_name.setText(cu_brand);
            brand_web.setText(cu_web);
            Log.v("알림", "address " + cu_addr);

            //spinner_field.setSelection(3);
            brand_phone.setText(cu_phone);
            send_class_info.setText("수정 요청");
        }
    }

    private void makeDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(BrandAuth.this,R.style.MyAlertDialogStyle);
        alt_bld.setTitle("인증요청").setIcon(R.drawable.check_dialog_64).setMessage("입력하신 정보로 브랜드 인증을 요청 합니다.\n사실과 일치하지 않을 시 삭제조치 될 수 있습니다.").setCancelable(
                false).setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 네 클릭

                        // 모두 입력되었는지 확인. 입력되어있지 않다면
                        if(brand_name.getText().length()==0
                                || brand_web.getText().length() == 0
                                || brand_phone.getText().length() == 0
                                || brand_address_content.getText().length() == 0
                                || spinner_field.getSelectedItemId() == 0){
                            Toast.makeText(BrandAuth.this,"모든 정보를 입력해주세요", Toast.LENGTH_LONG).show();
                        }else{
                            // 현재 로그인한 사용자의 Uid
                            String cu = mAuth.getUid();

                            // 브랜드 인증 정보가 있다면
                            if(mPref.getInt("exists",0) == 1){
                                //수정
                                mDatabase = FirebaseDatabase.getInstance().getReference("Regclass");
                                Map<String , Object> newvalue = new HashMap<>();
                                newvalue.put("/address/", brand_address_content.getText().toString());
                                newvalue.put("/brandname/",brand_name.getText().toString());
                                newvalue.put("/field/", spinner_field.getSelectedItem().toString());
                                newvalue.put("/phone/", brand_phone.getText().toString());
                                newvalue.put("/weburl/", brand_web.getText().toString());
                                mDatabase.child(cu).updateChildren(newvalue);
                            }else if(mPref.getInt("exists",0) == 0){
                                // 브랜드 인증 정보가 없다면 새로 생성
                                // 작성한 클래스정보를 RegClassData에 담기
                                BrandAuthData regClassData = new BrandAuthData(brand_name.getText().toString(), brand_web.getText().toString(), brand_phone.getText().toString(),
                                        spinner_field.getSelectedItem().toString(), brand_address_content.getText().toString());
                                // DB에 등록
                                mDatabase.child("Regclass").child(cu).setValue(regClassData);
                            }

                            // 등록완료 알림창 발생
                            makeConfirmDialog();
                        }
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
                finish();
                startActivity(intent);
            }
        }, 1500);
    }

}
