package com.findigital.blossom.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.findigital.blossom.R;
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
import com.raweng.twitter4j.internal.org.json.JSONException;
import com.raweng.twitter4j.internal.org.json.JSONObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 14-AB109LA on 16/1/2017.
 */

public class MyCareerFragmentActivity extends FragmentActivity {

    String careerColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_my_career);

        DbHelper dbHelper = new DbHelper(getApplicationContext());

        MyCareer myCareer = dbHelper.getMyCareer();

        final TextView txtMyCareerName = (TextView) findViewById(R.id.txtMyCareerName);
        final TextView txtMyCareerIntro = (TextView) findViewById(R.id.txtMyCareerIntro);
        final LinearLayout rlMyCareerLayout = (LinearLayout) findViewById(R.id.rlMyCareerLayout);
        final LinearLayout loader = (LinearLayout) findViewById(R.id.llHeaderProgress);
        final ImageView imgMyCareerCover = (ImageView) findViewById(R.id.imgMyCareerCover);

        ImageButton btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        Button btnMyCareerResources = (Button) findViewById(R.id.btnMyCareerResources);
        btnMyCareerResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyResourcesFragmentActivity.class));
            }
        });

        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("career_content").query();

            loader.setVisibility(View.VISIBLE);

            queryCareers.where("uid", myCareer.getId()).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> careers = queryResultObject.getResultObjects();

                        careerColor = "#" + careers.get(0).get("career_color").toString();
                        String careerName = careers.get(0).get("career_name").toString();
                        String careerIntro = careers.get(0).get("career_intro").toString();
                        String careerImage = careers.get(0).get("featured_image").toString();

                        rlMyCareerLayout.setBackgroundColor(Color.parseColor(careerColor));
                        txtMyCareerName.setText(careerName);
                        txtMyCareerIntro.setText(careerIntro);

                        try {
                            JSONObject careerImageObject = new JSONObject(careerImage);
                            String url = careerImageObject.get("url").toString();

                            Picasso.with(getApplicationContext())
                                    .load(url)
                                    .into(imgMyCareerCover);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(error.getErrorMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    loader.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
