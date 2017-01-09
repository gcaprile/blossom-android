package com.findigital.blossom.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findigital.blossom.R;
import com.findigital.blossom.adapters.CareersPageAdapter;
import com.findigital.blossom.adapters.VideosPageAdapter;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.utilities.BuiltConstant;
import com.raweng.twitter4j.internal.org.json.JSONException;
import com.raweng.twitter4j.internal.org.json.JSONObject;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

/**
 * Created by 14-AB109LA on 29/12/2016.
 */

public class CareerDetailFragment extends FragmentActivity {

    String API_KEY = "blta5ec08d170ee25c5";

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_career_detail);

        viewPager = (ViewPager) findViewById(R.id.pagerVideos);

        Intent intent = getIntent();
        String careerId = intent.getStringExtra("careerId");

        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(),API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("career_content").query();

            queryCareers.where("uid", careerId).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> careers = queryResultObject.getResultObjects();

                        TextView txtCareerDetailHeader = (TextView) findViewById(R.id.txtCareerDetailHeader);
                        TextView txtCareerDetailIntro = (TextView) findViewById(R.id.txtCareerDetailIntro);
                        ImageView imgCareerDetailImage = (ImageView) findViewById(R.id.imgCareerDetailImage);
                        LinearLayout llCareerDetail = (LinearLayout) findViewById(R.id.llCareerDetail);

                        String careerColor = "#" + careers.get(0).get("career_color").toString();
                        String careerHeader = careers.get(0).get("career_intro_header").toString();
                        String careerIntro = careers.get(0).get("career_intro").toString();
                        String careerImage = careers.get(0).get("featured_image").toString();

                        llCareerDetail.setBackgroundColor(Color.parseColor(careerColor));
                        txtCareerDetailHeader.setText(careerHeader);
                        txtCareerDetailIntro.setText(careerIntro);

                        try {
                            JSONObject careerImageObject = new JSONObject(careerImage);
                            String url = careerImageObject.get("url").toString();

                            Picasso.with(getApplicationContext())
                                    .load(url)
                                    .into(imgCareerDetailImage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        System.out.println(error.getErrorMessage());
                        // query failed
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            final LinearLayout llHeaderProgress = (LinearLayout) findViewById(R.id.llHeaderProgress);
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("youtube_links").query();

            queryCareers.where("career_reference", careerId).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> videos = queryResultObject.getResultObjects();

                        pagerAdapter = new VideosPageAdapter(CareerDetailFragment.this, videos);
                        viewPager.setAdapter(pagerAdapter);

                        llHeaderProgress.setVisibility(View.GONE);

                    }else{
                        System.out.println(error.getErrorMessage());
                        // query failed
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            final LinearLayout llHeaderProgress = (LinearLayout) findViewById(R.id.llHeaderProgress);
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("required_courses").query();

            queryCareers.where("referenced_careers", careerId).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> requirements = queryResultObject.getResultObjects();
                        String content = "";

                        for(int i=0; i < requirements.size(); i++){
                            content += requirements.get(i).get("course_description").toString() + "\r\n";
                        }

                        TextView txtCareerDetailReqs = (TextView) findViewById(R.id.txtCareerDetailReqs);
                        txtCareerDetailReqs.setText(content);

                        llHeaderProgress.setVisibility(View.GONE);

                    } else {
                        System.out.println(error.getErrorMessage());
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
