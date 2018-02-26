package com.example.ghd_t.myapplication;


import android.content.Context;
import android.content.Intent;
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


import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegClassFragment extends Fragment {


    public RegClassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg_class, container, false);


        EditText brand_name = (EditText) view.findViewById(R.id.edit_brand_name);
        EditText brand_web = (EditText) view.findViewById(R.id.edit_brand_web);
        EditText brand_phone = (EditText) view.findViewById(R.id.edit_phone);
        EditText brand_address_content = (EditText) view.findViewById(R.id.address);
        ImageButton brand_address = (ImageButton) view.findViewById(R.id.edit_address);



        // 연락처 입력시 하이픈(-) 자동 입력.
        brand_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

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
