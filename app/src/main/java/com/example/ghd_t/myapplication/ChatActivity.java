package com.example.ghd_t.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends Activity {

    private ListView chatList;
    private ListAdapterChat chatAdapter;
    private ImageButton send_msg;
    private EditText chat_message;
    private String chatPartner, chatRoomIndex;
    private TextView msg_name;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        chatAdapter = new ListAdapterChat();
        chatList = (ListView)findViewById(R.id.chat_list);
        msg_name = (TextView)findViewById(R.id.msg_name);
        send_msg = (ImageButton)findViewById(R.id.chat_btn);
        chat_message = (EditText)findViewById(R.id.chat_message);

        //대화상대 이름 설정
        Intent intent = getIntent();
        chatPartner = intent.getStringExtra("chatPartner");
        chatRoomIndex = intent.getStringExtra("chatRoomIndex");
        Log.v("알림","입장한 방 번호 " + chatRoomIndex);
        msg_name.setText(chatPartner);

        chatList.setAdapter(chatAdapter);


//        mDatabase.child("Chattings").addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
//                adapter.add(chatData.getUserName() + ": " + chatData.getMessage());  // adapter에 추가합니다.
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) { }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) { }
//        });




//        chatAdapter.add("2017/03/02",2);
//        chatAdapter.add("긴 내용이 필요하다!! 어떻게 ㅎ면 긴 내용ㅇ 나ㅗㅇㄹ까 어디까지 길어질까??????!!!",0);
//        chatAdapter.add("안녕",1);
//        chatAdapter.add("ㅋㅋㅋ",1);
//        chatAdapter.add("2017/03/03",2);
//        chatAdapter.add("안녕하세요!!!",0);
//        chatAdapter.add("zㅋㅋㅋㅋ 반갑네용 히히!!!",0);
//        chatAdapter.add("긴 내용이 필요하다!! 어떻게 ㅎ면 긴 내용ㅇ",0);
//        chatAdapter.add("안녕",1);
//        chatAdapter.add("ㅋㅋㅋ",1);
//        chatAdapter.add("안녕하세요!!!",0);
//        chatAdapter.add("zㅋㅋㅋㅋ 반갑네용 히히!!!",0);
//        chatAdapter.add("2017/03/04",2);


        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "메세지 전송",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
