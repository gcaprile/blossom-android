package com.findigital.blossom.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.findigital.blossom.R;

/**
 * ABA
 * Created by Ramon Zuniga on 10/1/2017.
 * Copyright © 2016 FinDigital. All rights reserved.
 */

public class VideoPlayerFragment extends Fragment {
    private String videoUrl;
    private WebView webView;

    public static VideoPlayerFragment newInstance(String videoUrl) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle args = new Bundle();
        args.putString("videoUrl", videoUrl);
        videoPlayerFragment.setArguments(args);
        return videoPlayerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoUrl = getArguments().getString("videoUrl");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);

        String frameVideo = "<iframe width='100%' height='200px' src='https://www.youtube.com/embed/" + videoUrl + "' frameborder='0' allowfullscreen></iframe>";

        webView = (WebView) view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadData(frameVideo, "text/html", "utf-8");

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (webView != null) {
                webView.onResume();
            }
        } else {
            if (webView != null) {
                webView.onPause();
            }
        }
    }
}
