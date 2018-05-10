package com.example.ghd_t.myapplication;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MsgFragment extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private MsgItemData data_msg_data;
    private ArrayList<MsgItemData> data_msg;
    private ListAdapterMsg adapter_msg;
    private ListView msg_list;
    private String participantId,participantName,chatRoomIndex;
    Drawable temp;

    public MsgFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        temp = getResources().getDrawable(R.drawable.temp);
        msg_list = (ListView) view.findViewById(R.id.msglist);
        data_msg = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        final String cu = mAuth.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("Chattings");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {

                    chatRoomIndex = objSnapshot.getKey();
                    Log.v("알림","현재 존재하는 chatRoomIndex " + chatRoomIndex);
                    String user1 = objSnapshot.child("participants").child("user1").getValue().toString();
                    String user2 = objSnapshot.child("participants").child("user2").getValue().toString();

                    if(cu.equals(user1)){
                        //내가 속한 채팅방 찾기
                        participantId = user2;
                        setChatParticipantName(chatRoomIndex);

                    }else if(cu.equals(user2)){
                        participantId = user1;
                        setChatParticipantName(chatRoomIndex);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }

    //참여자 id찾은 후 name을 찾아 name으로 setting
    void setChatParticipantName(final String chatRoomIndex_param){
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.child(participantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                participantName = dataSnapshot.child("userName").getValue().toString();
                makeData(participantName, chatRoomIndex_param);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //listdata만들기
    void makeData(String name, String chatRoomIndex_param){
        Log.v("알림","list data 만들기 시작 " + name + " " + chatRoomIndex_param );
        data_msg_data = new MsgItemData(temp, name, "문의 있네요!", chatRoomIndex_param);
        data_msg.add(data_msg_data);
        adapter_msg = new ListAdapterMsg(getContext(), R.layout.msg_listview_item, data_msg);
        msg_list.setAdapter(adapter_msg);


    }
}
