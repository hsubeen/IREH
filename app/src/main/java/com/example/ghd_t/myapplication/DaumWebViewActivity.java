package com.example.ghd_t.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DaumWebViewActivity extends AppCompatActivity {
    private WebView daum_webView;
    private TextView daum_result;
    private Handler handler;
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_web_view);
        daum_result = (TextView) findViewById(R.id.daum_result);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();

    }


    public void init_webView() {
        // WebView 설정
        daum_webView = (WebView) findViewById(R.id.daum_webview);
        // JavaScript 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        daum_webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        daum_webView.setWebChromeClient(new WebChromeClient());
        // webview url load
        daum_webView.loadUrl("http://192.168.0.31:80/daum_address.php");
    }


    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    address = String.format(" %s %s,(%s)", arg2, arg3, arg1);
                    AddressData.getInstance().setAddress(address);

                    //ad.setAddress(address);

                    Log.v("알림","주소 저장 완료 : " + AddressData.getInstance().getAddress());
                    //daum_result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                    finish();
                }
            });
        }
    }

}
