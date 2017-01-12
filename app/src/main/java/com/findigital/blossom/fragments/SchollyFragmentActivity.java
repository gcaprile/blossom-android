package com.findigital.blossom.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.findigital.blossom.R;

/**
 * Created by 14-AB109LA on 11/1/2017.
 */

public class SchollyFragmentActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scholly);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl("https://app.myscholly.com/aba");
    }
}
