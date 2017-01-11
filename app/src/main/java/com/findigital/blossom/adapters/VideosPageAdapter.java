package com.findigital.blossom.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.findigital.blossom.fragments.VideoPlayerFragment;
import com.raweng.built.BuiltObject;

import java.util.List;

/**
 * ABA
 * Created by Ramon Zuniga on 29/12/2016.
 * Copyright © 2016 FinDigital. All rights reserved.
 */

public class VideosPageAdapter extends FragmentPagerAdapter {

    List<BuiltObject> videos;

    public VideosPageAdapter(FragmentManager fm, List<BuiltObject> videos) {
        super(fm);
        this.videos = videos;
    }

    @Override
    public int getCount() { return videos.size(); }

    @Override
    public Fragment getItem(int position) {
        String videoUrl = videos.get(position).get("youtube_url").toString();
        videoUrl = videoUrl.substring(videoUrl.lastIndexOf("=") + 1);

        return VideoPlayerFragment.newInstance(videoUrl);
    }

}
