package com.example.ghd_t.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class WriteClassActivity extends AppCompatActivity {
    private Spinner spinner_money_min, spinner_money_max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_class);

        spinner_money_min = findViewById(R.id.spinner_money_min);
        spinner_money_max = findViewById(R.id.spinner_money_max);

        String[] str = getResources().getStringArray(R.array.spinnerArray_forWrite_money);
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(WriteClassActivity.this,R.layout.spinner_item,str);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_money_min.setAdapter(adapter);
        spinner_money_max.setAdapter(adapter);

    }
}
