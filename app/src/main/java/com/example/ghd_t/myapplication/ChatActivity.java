package com.example.ghd_t.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ChatActivity extends Activity {

    ListView chatList;
    ListAdapterChat chatAdapter;
    ImageButton send_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatAdapter = new ListAdapterChat();
        chatList = (ListView)findViewById(R.id.chat_list);

        send_msg = (ImageButton)findViewById(R.id.chat_btn);

        chatList.setAdapter(chatAdapter);
        chatAdapter.add("2017/03/02",2);
        chatAdapter.add("긴 내용이 필요하다!! 어떻게 ㅎ면 긴 내용ㅇ 나ㅗㅇㄹ까 어디까지 길어질까??????!!!",0);
        chatAdapter.add("안녕",1);
        chatAdapter.add("ㅋㅋㅋ",1);
        chatAdapter.add("2017/03/03",2);
        chatAdapter.add("안녕하세요!!!",0);
        chatAdapter.add("zㅋㅋㅋㅋ 반갑네용 히히!!!",0);
        chatAdapter.add("긴 내용이 필요하다!! 어떻게 ㅎ면 긴 내용ㅇ",0);
        chatAdapter.add("안녕",1);
        chatAdapter.add("ㅋㅋㅋ",1);
        chatAdapter.add("안녕하세요!!!",0);
        chatAdapter.add("zㅋㅋㅋㅋ 반갑네용 히히!!!",0);
        chatAdapter.add("2017/03/04",2);


        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "메세지 전송",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
