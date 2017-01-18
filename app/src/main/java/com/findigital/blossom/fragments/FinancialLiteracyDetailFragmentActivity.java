package com.findigital.blossom.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.adapters.VideosPageAdapter;
import com.findigital.blossom.helpers.API;
import com.findigital.blossom.helpers.DbHelper;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.utilities.BuiltConstant;
import com.raweng.twitter4j.internal.org.json.JSONArray;
import com.raweng.twitter4j.internal.org.json.JSONException;
import com.raweng.twitter4j.internal.org.json.JSONObject;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import static android.view.View.GONE;

/**
 * ABA
 * Created by Ramon Zuniga on 01/17/2017.
 * Copyright © 2016 FinDigital. All rights reserved.
 */

public class FinancialLiteracyDetailFragmentActivity extends FragmentActivity {

    String literacyId;

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    DbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_financial_literacy_detail);

        dbHelper = new DbHelper(getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.pagerVideos);

        final LinearLayout llContentDetailLinks = (LinearLayout) findViewById(R.id.llContentDetailLinks);
        final TextView txtContentVideosLabel = (TextView) findViewById(R.id.txtContentVideosLabel);
        final TextView txtContentLinksLabel = (TextView) findViewById(R.id.txtContentLinksLabel);

        Intent intent = getIntent();
        literacyId = intent.getStringExtra("literacyId");

        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("financial_literacy").query();

            queryCareers.where("uid", literacyId).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> literacy = queryResultObject.getResultObjects();

                        TextView txtContentDetailTitle = (TextView) findViewById(R.id.txtContentDetailTitle);
                        TextView txtContentDetailIntro = (TextView) findViewById(R.id.txtContentDetailIntro);
                        TextView txtContentDetailDesc = (TextView) findViewById(R.id.txtContentDetailDesc);
                        ImageView imgContentDetailCover = (ImageView) findViewById(R.id.imgContentDetailCover);
                        RelativeLayout rlContentDetail = (RelativeLayout) findViewById(R.id.rlContentDetail);

                        String contentTitle = literacy.get(0).get("fin_lit_title").toString();
                        String contentIntro = literacy.get(0).get("fin_lit_shortname").toString();
                        String contentDesc = literacy.get(0).get("fin_lit_description").toString();
                        String contentImg = literacy.get(0).get("fin_lit_image").toString();

                        String bkgColor = literacy.get(0).get("fin_lit_background_color").toString();

                        try {
                            String color = "#" + bkgColor;
                            rlContentDetail.setBackgroundColor(Color.parseColor(color));
                        } catch (NumberFormatException ex) {
                            rlContentDetail.setBackgroundColor(Color.parseColor(bkgColor));
                        }

                        txtContentDetailTitle.setText(contentTitle);
                        txtContentDetailIntro.setText(contentIntro);
                        txtContentDetailDesc.setText(contentDesc);

                        try {
                            JSONObject jsonObject = new JSONObject(contentImg);
                            String url = jsonObject.get("url").toString();

                            Picasso.with(getApplicationContext())
                                    .load(url)
                                    .into(imgContentDetailCover);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONArray jsonUrlArray = new JSONArray(literacy.get(0).get("fin_lit_urls").toString());

                            if (jsonUrlArray.length() > 0) {
                                for (int i = 0; i < jsonUrlArray.length(); i++) {
                                    JSONObject jsonObject = new JSONObject(jsonUrlArray.getString(i));
                                    TextView txtLink = new TextView(getApplicationContext());
                                    txtLink.setText(jsonObject.get("title").toString());
                                    txtLink.setTextColor(Color.WHITE);
                                    txtLink.setPaintFlags(txtLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    txtLink.setTypeface(Typeface.DEFAULT_BOLD);
                                    txtLink.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_link_m, 0, 0, 0);
                                    txtLink.setCompoundDrawablePadding(10);

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    );
                                    params.setMargins(0, 0, 0, 15);
                                    txtLink.setLayoutParams(params);

                                    final String url = jsonObject.get("href").toString();

                                    txtLink.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.setData(Uri.parse(url));
                                            getApplicationContext().startActivity(intent);
                                        }
                                    });

                                    llContentDetailLinks.addView(txtLink);
                                }
                            } else {
                                txtContentLinksLabel.setVisibility(GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
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

        try {
            final LinearLayout llHeaderProgress = (LinearLayout) findViewById(R.id.llHeaderProgress);
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryVideos = builtApplication.classWithUid("youtube_links").query();

            queryVideos.where("fit_lit_reference", literacyId).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> videos = queryResultObject.getResultObjects();

                        pagerAdapter = new VideosPageAdapter(getSupportFragmentManager(), videos);

                        viewPager.setAdapter(pagerAdapter);

                        llHeaderProgress.setVisibility(GONE);

                        if (videos.size() == 0) {
                            txtContentVideosLabel.setVisibility(GONE);
                            viewPager.setVisibility(GONE);
                        }

                    } else {
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

    @Override
    protected void onPause() {
        super.onPause();
    }

}
