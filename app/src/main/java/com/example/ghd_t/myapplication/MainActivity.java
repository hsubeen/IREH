package com.example.ghd_t.myapplication;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import java.security.MessageDigest;

public class MainActivity extends FragmentActivity {
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment;
    MsgFragment msgFragment;
    RegClassFragment regclassFragment;
    AboutUserFragment aboutuserFragment;

    int menu_here;
    MenuItem menu_prev;

    private ViewPager viewpager;
    ViewpagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);  //클릭 시 움직이는 모양 없애기. 하단 네비게이션 바 고정



        menu_here = R.id.action_home;   //처음 페이지를 home으로 지정
        viewpager = (ViewPager) findViewById(R.id.mainViewPager);
        viewpager.setOffscreenPageLimit(4);
        setViewpager(viewpager); //view pager에 fragment 등록
        menu_prev = bottomNavigationView.getMenu().getItem(0);
        getAppKeyHash();        // API 사용 위한 Hash key 구하기

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        menu_here = R.id.action_home;
                        viewpager.setCurrentItem(0);
                        Log.v("알림","홈 뷰페이저 선택");
                        break;
                    case R.id.action_msg:
                        menu_here = R.id.action_msg;
                        viewpager.setCurrentItem(1);
                        Log.v("알림","메세지 뷰페이저 선택");
                        break;
                    case R.id.action_registclass:
                        menu_here = R.id.action_registclass;
                        viewpager.setCurrentItem(2);
                        Log.v("알림","클래스 추가 뷰페이저 선택");
                        break;
                    case R.id.action_aboutuser:
                        menu_here = R.id.action_aboutuser;
                        viewpager.setCurrentItem(3);
                        Log.v("알림","회원정보 뷰페이저 선택");
                        break;
                }
                return true;
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                menu_here = bottomNavigationView.getMenu().getItem(position).getItemId();
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                menu_prev = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //viewpager에 순서대로 fragment 추가
    private void setViewpager(ViewPager viewpager){
        adapter = new ViewpagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        msgFragment = new MsgFragment();
        regclassFragment = new RegClassFragment();
        aboutuserFragment = new AboutUserFragment();

        adapter.addFragment(homeFragment);
        adapter.addFragment(msgFragment);
        adapter.addFragment(regclassFragment);
        adapter.addFragment(aboutuserFragment);

        viewpager.setAdapter(adapter);
    }






    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
}
