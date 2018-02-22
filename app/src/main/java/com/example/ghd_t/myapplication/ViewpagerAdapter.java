package com.example.ghd_t.myapplication;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghd-t on 2018-02-23.
 */

public class ViewpagerAdapter extends FragmentStatePagerAdapter {
    public List<Fragment> fl = new ArrayList<>();

    public ViewpagerAdapter(FragmentManager manager){
        super(manager);
    }

    public void addFragment(Fragment fragment){
        fl.add(fragment);
    }

    public void switchFragment(int position, Fragment fragment){
        fl.remove(position);
        fl.add(position, fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fl.get(position);
    }

    @Override
    public int getCount() {
        return fl.size();
    }


}
