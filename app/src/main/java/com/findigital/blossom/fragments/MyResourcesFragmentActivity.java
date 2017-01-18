package com.findigital.blossom.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.adapters.LinksPageAdapter;
import com.findigital.blossom.adapters.ResponsiveViewPager;
import com.findigital.blossom.adapters.ScholarshipsPageAdapter;
import com.findigital.blossom.adapters.VideosPageAdapter;
import com.findigital.blossom.helpers.API;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.MyCareer;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.utilities.BuiltConstant;

import java.util.List;

/**
 * Created by 14-AB109LA on 16/1/2017.
 */

public class MyResourcesFragmentActivity extends FragmentActivity {

    String careerId;
    ViewPager videosViewPager;
    ResponsiveViewPager scholarshipsViewPager;
    ResponsiveViewPager linksViewPager;
    PagerAdapter pagerAdapter;
    ScholarshipsPageAdapter scholarshipsPageAdapter;
    LinksPageAdapter linksPageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_my_resources);

        videosViewPager = (ViewPager) findViewById(R.id.pagerVideos);
        scholarshipsViewPager = (ResponsiveViewPager) findViewById(R.id.pagerScholarships);
        linksViewPager = (ResponsiveViewPager) findViewById(R.id.pagerLinks);

        DbHelper dbHelper = new DbHelper(getApplicationContext());
        MyCareer myCareer = dbHelper.getMyCareer();
        careerId = myCareer.getId();

        String color = "#" + myCareer.getColor();

        LinearLayout llMyResourcesLayout = (LinearLayout) findViewById(R.id.llMyResourcesLayout);

        try {
            llMyResourcesLayout.setBackgroundColor(Color.parseColor(color));
        } catch (NumberFormatException ex) {
            llMyResourcesLayout.setBackgroundColor(Color.parseColor(myCareer.getColor()));
        }

        ImageButton btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        getVideos();
        getScholarships();
        getLinks();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getVideos() {
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("youtube_links").query();

            queryCareers.where("career_reference", careerId).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> videos = queryResultObject.getResultObjects();
                        pagerAdapter = new VideosPageAdapter(getSupportFragmentManager(), videos);
                        videosViewPager.setAdapter(pagerAdapter);
                    }else{
                        System.out.println(error.getErrorMessage());
                        Toast.makeText(
                                getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                        // query failed
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getScholarships() {
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("scholarships").query();

            queryCareers.where("career_reference", careerId).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> scholarships = queryResultObject.getResultObjects();
                        scholarshipsPageAdapter =
                                new ScholarshipsPageAdapter(getApplicationContext(), scholarships);
                        scholarshipsViewPager.setAdapter(scholarshipsPageAdapter);

                    }else{
                        System.out.println(error.getErrorMessage());
                        Toast.makeText(
                                getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                        // query failed
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLinks() {
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("resource_links").query();

            queryCareers.where("career_reference", careerId).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> links = queryResultObject.getResultObjects();
                        linksPageAdapter =
                                new LinksPageAdapter(getApplicationContext(), links);
                        linksViewPager.setAdapter(linksPageAdapter);
                    }else{
                        System.out.println(error.getErrorMessage());
                        Toast.makeText(
                                getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                        // query failed
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
