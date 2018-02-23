package com.example.ghd_t.myapplication;


import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUserFragment extends Fragment {


    public AboutUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // fragment에서 findViewById 사용하기
        View view = inflater.inflate(R.layout.fragment_about_user, container, false);
        //ImageView imageView = (ImageView) view.findViewById(R.id.user_profile);




        return view;
    }

}
