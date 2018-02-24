package com.example.ghd_t.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;


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

        // 연락처 입력시 하이픈(-) 자동 입력.
        EditText edit_phone = (EditText) view.findViewById(R.id.edit_phone);
        edit_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        return view;
    }

}
