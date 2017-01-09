package com.findigital.blossom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.findigital.blossom.R;
import com.raweng.built.BuiltObject;

import java.util.List;

/**
 * Created by 14-AB109LA on 29/12/2016.
 */

public class VideosPageAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    List<BuiltObject> videos;

    public VideosPageAdapter(Context context, List<BuiltObject> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public int getCount() { return videos.size(); }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.videospager_item, container,
                false);

        String videoUrl = videos.get(position).get("youtube_url").toString();
        videoUrl = videoUrl.substring(videoUrl.lastIndexOf("=") + 1);

        String frameVideo = "<iframe width='100%' height='200px' src='https://www.youtube.com/embed/" + videoUrl + "' frameborder='0' allowfullscreen></iframe>";

        WebView displayVideo = (WebView) itemView.findViewById(R.id.webView);
        displayVideo.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = displayVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayVideo.setBackgroundColor(Color.TRANSPARENT);
        displayVideo.loadData(frameVideo, "text/html", "utf-8");

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((View) object);
    }
}
