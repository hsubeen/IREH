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
    private TextView class_title, class_detail, class_name, class_field, class_address, class_web;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Object writeClass_index = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);

        Intent intent = getIntent();
        String index = intent.getStringExtra("Index");

        mAuth = FirebaseAuth.getInstance();
        final String cu = mAuth.getUid();

        class_title = findViewById(R.id.class_title);
        class_detail = findViewById(R.id.class_detail);
        class_name = findViewById(R.id.class_name);
        class_field = findViewById(R.id.class_field);
        class_address = findViewById(R.id.class_address);
        class_web = findViewById(R.id.class_web);

        //모집 글 정보 불러오기
        mDatabase = FirebaseDatabase.getInstance().getReference("WriteClass");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                    writeClass_index = objSnapshot.getKey();
                    Log.v("알림","게시글 번호 : " + writeClass_index);
                    if(writeClass_index == null){
                        Toast.makeText(getApplicationContext(),"게시글을 불러올 수 없습니다.", Toast.LENGTH_LONG).show();
                    }else{
                        findWriteClass();
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
    void findWriteClass(){
        //게시글 번호 찾아 title 찾기
        mDatabase.child(writeClass_index.toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("알림", "get childeren : " + dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
