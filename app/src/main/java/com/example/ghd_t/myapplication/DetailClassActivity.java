package com.example.ghd_t.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailClassActivity extends AppCompatActivity {
    private TextView class_phone,class_title, class_content, class_name, class_field, class_address, class_web, class_person, money_min, money_max;
    private DatabaseReference mDatabase1, mDatabase2, mDatabase3, mDatabase4, mDatabase5;
    private FirebaseAuth mAuth;
    private String index, writepersonId,ct_str, chatRoomIndex, participantName;
    private Button btn_reservation, btn_chat, btn_modify, btn_delete;
    private int findFlag = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);

        Intent intent = getIntent();
        index = intent.getStringExtra("Index");

        mAuth = FirebaseAuth.getInstance();
        mDatabase3 = FirebaseDatabase.getInstance().getReference();
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

        btn_chat = findViewById(R.id.btn_chat);
        btn_reservation = findViewById(R.id.btn_reservation);
        btn_delete = findViewById(R.id.btn_delete);
        btn_modify = findViewById(R.id.btn_modify);

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

                if(cu.equals(writepersonId)){
                    //본인이 쓴 글. 예약과 문의 버튼 없애기
                    btn_reservation.setVisibility(View.GONE);
                    btn_chat.setVisibility(View.GONE);
                    btn_delete.setVisibility(View.VISIBLE);
                    btn_modify.setVisibility(View.VISIBLE);
                    Toast.makeText(DetailClassActivity.this, "어머 내가 쓴 글이다",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //1:1문의 클릭
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재시간
                long ct = System.currentTimeMillis();
                ct_str = Long.toString(ct);

                //채팅방 없는지 확인 하고, 없으면 생성 아니면 기존챗방으로 이동!
                mDatabase4 = FirebaseDatabase.getInstance().getReference("Chattings");
                mDatabase4.addListenerForSingleValueEvent(new ValueEventListener() {
                    
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue()==null){
                            //채팅방 정보가 아예 없는 경우
                            //user1은 나(채팅 신청한사람) , user2는 글쓴이(모집 글 올린사람)
                            Participants participants = new Participants(cu, writepersonId);
                            mDatabase3.child("Chattings").child(ct_str).child("participants").setValue(participants);
                            Messages messages = new Messages("TIME_MESSAGE", getDate());
                            mDatabase3.child("Chattings").child(ct_str).child("messages").push().setValue(messages);

                            Log.v("알림", "채팅방이 아예 없음. 생성 완료");
                        }else{
                            //채팅방 정보가 있는 경우 내가 속한 채팅방 모두 찾기
                            for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {


                                String user1 = objSnapshot.child("participants").child("user1").getValue().toString();
                                String user2 = objSnapshot.child("participants").child("user2").getValue().toString();

                                if(cu.equals(user1) || cu.equals(user2)){
                                    //내가 속한 채팅방 찾음.
                                    findFlag=1;
                                    Log.v("알림", "내가 속한 채팅방 발견");
                                    chatRoomIndex = objSnapshot.getKey();
                                    Log.v("알림","채팅방 number " + chatRoomIndex);
                                    findName();
                                }
                            }

                            if(findFlag!=1){
                                //내가 속한 채팅방이 없음. 생성
                                Participants participants = new Participants(cu, writepersonId);
                                mDatabase3.child("Chattings").child(ct_str).child("participants").setValue(participants);

                                Messages messages = new Messages("TIME_MESSAGE", getDate());
                                mDatabase3.child("Chattings").child(ct_str).child("messages").push().setValue(messages);
                                Log.v("알림", "내가 속한 채팅방 없음 / 생성 완료");
                            }

                        }


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


            }
        });
    }

    void findName(){
        mDatabase5 = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase5.child(writepersonId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                participantName = dataSnapshot.child("userName").getValue().toString();

                //글쓴이의 name을 구해 ChatActivity로 전달
                Intent intent = new Intent(DetailClassActivity.this, ChatActivity.class);
                intent.putExtra("chatPartner", participantName);
                intent.putExtra("chatRoomIndex",chatRoomIndex);
                startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    void makeBrandData(){
        //클래스 정보 불러오기
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



    private String getDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String nowTime = sdf.format(date);
        Log.v("알림","현재시간 " + nowTime);
        return nowTime;
    }


}
