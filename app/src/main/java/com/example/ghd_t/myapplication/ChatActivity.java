package com.example.ghd_t.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ChatActivity extends Activity {

    ListView chatList;
    ListAdapterChat chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatAdapter = new ListAdapterChat();

        chatList = (ListView)findViewById(R.id.chat_list);

        chatList.setAdapter(chatAdapter);

        chatAdapter.add("긴 내용이 필요하다!! 어떻게 ㅎ면 긴 내용ㅇ 나ㅗㅇㄹ까 어디까지 길어질까??????!!!",0);
        chatAdapter.add("안녕",1);
        chatAdapter.add("ㅋㅋㅋ",1);
        chatAdapter.add("안녕하세요!!!",0);
        chatAdapter.add("2017/3/03",2);

    }
}
