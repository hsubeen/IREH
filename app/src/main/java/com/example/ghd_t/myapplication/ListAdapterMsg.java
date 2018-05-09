package com.example.ghd_t.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ghd-t on 2018-02-25.
 */

public class ListAdapterMsg extends BaseAdapter {

    private ArrayList<MsgItemData> data = new ArrayList<>();
    private LayoutInflater inflater;
    private int layout;

    public ListAdapterMsg(Context context, int layout, ArrayList<MsgItemData> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int i,  View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(layout,viewGroup,false);
            view.setClickable(true);
        }
        MsgItemData listviewitem = data.get(i);

        final ImageView msg_photo = (ImageView) view.findViewById(R.id.msg_photo);
        final TextView msg_name = (TextView) view.findViewById(R.id.msg_name);
        final TextView msg_content = (TextView) view.findViewById(R.id.msg_content);
        final View v = view;
        msg_photo.setImageDrawable(data.get(i).getMsg_photo());
        msg_name.setText(data.get(i).getMsg_name());
        msg_content.setText(data.get(i).getMsg_content());


        // layout 자체에 대한 클릭 이벤트.
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v("알림", "msg 내용 클릭");

                //대화상대 이름을 intent를 통해 ChatActivity로 전달
                Intent intent = new Intent(v.getContext(),ChatActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("chatPartner", msg_name.getText().toString());
                v.getContext().startActivity(intent);
            }
        });


        return view;
    }

}
