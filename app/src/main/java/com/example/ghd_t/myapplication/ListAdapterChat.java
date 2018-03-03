package com.example.ghd_t.myapplication;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ghd-t on 2018-03-03.
 */

public class ListAdapterChat extends BaseAdapter{
    private ArrayList<ListContents> arrayList;

    public class ListContents{

        String msg;
        int type;
        ListContents(String msg,int type)
        {
            this.msg = msg;
            this.type = type;
        }
    }


    public ListAdapterChat() {
        arrayList = new ArrayList();
    }

    public void add(String msg,int type){
        arrayList.add(new ListContents(msg,type));
    }

    public void remove(int i){
        arrayList.remove(i);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int position = i;
        final Context context = viewGroup.getContext();

        TextView text = null;
        ChatHolder holder = null;
        LinearLayout layout = null;
        View rightView = null;
        View leftView = null;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_item,viewGroup,false);
            layout = (LinearLayout)view.findViewById(R.id.layout);
            text = (TextView) view.findViewById(R.id.chat_text);
            leftView = (View) view.findViewById(R.id.view_left);
            rightView = (View) view.findViewById(R.id.view_right);

            // holder 생성 , tag로 등록
            holder = new ChatHolder();
            holder.chat_text = text;
            holder.layout = layout;
            holder.leftView = leftView;
            holder.rightView = rightView;
            view.setTag(holder);
        }

        else{
            holder = (ChatHolder)view.getTag();
            text = holder.chat_text;
            layout = holder.layout;
            leftView = holder.leftView;
            rightView = holder.rightView;
        }

        text.setText(arrayList.get(position).msg);

        if(arrayList.get(position).type == 0){
            // 받은메세지
            text.setBackgroundResource(R.drawable.btn_maincolor);
            layout.setGravity(Gravity.LEFT);
            rightView.setVisibility(View.GONE);
            leftView.setVisibility(View.GONE);
        } else if(arrayList.get(position).type ==1 ){
            // 보낸메세지
            text.setBackgroundResource(R.drawable.btn_redcolor);
            layout.setGravity(Gravity.RIGHT);
            rightView.setVisibility(View.GONE);
            leftView.setVisibility(View.GONE);
        } else if(arrayList.get(position).type == 2){
            // 날짜
            text.setBackgroundResource(R.color.white);
            layout.setGravity(Gravity.CENTER);
            rightView.setVisibility(View.VISIBLE);
            leftView.setVisibility(View.VISIBLE);
        }

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "리스트 롱 클릭 : "+ arrayList.get(position), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return view;
    }

    private class ChatHolder {
        TextView chat_text;
        LinearLayout layout;
        View leftView;
        View rightView;
    }
}
