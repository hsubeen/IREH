package com.example.ghd_t.myapplication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegClassFragment extends Fragment {

    private TextView brand_address_content;

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



        EditText brand_name = (EditText) view.findViewById(R.id.edit_brand_name);
        EditText brand_web = (EditText) view.findViewById(R.id.edit_brand_web);
        EditText brand_phone = (EditText) view.findViewById(R.id.edit_phone);
        brand_address_content = (TextView) view.findViewById(R.id.address);
        ImageButton brand_address = (ImageButton) view.findViewById(R.id.edit_address);


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
        return view;
    }


}
