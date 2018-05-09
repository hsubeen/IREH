package com.example.ghd_t.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailClassActivity extends AppCompatActivity {
    private TextView class_phone,class_title, class_content, class_name, class_field, class_address, class_web, class_person, money_min, money_max;
    private DatabaseReference mDatabase1, mDatabase2;
    private FirebaseAuth mAuth;
    private String index, writepersonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);

        Intent intent = getIntent();
        index = intent.getStringExtra("Index");
        Log.v("알림", "선택된 글 INDEX " + index);

        mAuth = FirebaseAuth.getInstance();
        final String cu = mAuth.getUid();

        class_phone = findViewById(R.id.class_phone);
        class_title = findViewById(R.id.class_title);
        class_content = findViewById(R.id.class_content);
        class_name = findViewById(R.id.class_name);
        class_field = findViewById(R.id.class_field);
        class_address = findViewById(R.id.class_address);
        class_web = findViewById(R.id.class_web);
        class_person = findViewById(R.id.class_person);
        money_min = findViewById(R.id.money_min);
        money_max = findViewById(R.id.money_max);

        //모집 글 정보 불러오기
        mDatabase1 = FirebaseDatabase.getInstance().getReference("WriteClass");
        mDatabase1.child(index).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //불러온 정보 setting
                writepersonId = dataSnapshot.child("cu").getValue(String.class);
                class_title.setText(dataSnapshot.child("title").getValue(String.class));
                class_content.setText(dataSnapshot.child("contents").getValue(String.class));
                money_min.setText(dataSnapshot.child("money_min").getValue(String.class));
                money_max.setText(dataSnapshot.child("money_max").getValue(String.class));
                class_person.setText(dataSnapshot.child("person").getValue(String.class));

                makeBrandData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    void makeBrandData(){

        mDatabase2 = FirebaseDatabase.getInstance().getReference("Regclass");
        mDatabase2.child(writepersonId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                class_name.setText(dataSnapshot.child("brandname").getValue(String.class));
                class_field.setText(dataSnapshot.child("field").getValue(String.class));
                class_web.setText(dataSnapshot.child("weburl").getValue(String.class));
                class_phone.setText(dataSnapshot.child("phone").getValue(String.class));
                class_address.setText(dataSnapshot.child("address").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
