package com.findigital.blossom.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.activities.MainActivity;
import com.findigital.blossom.helpers.API;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.MyCareer;
import com.findigital.blossom.models.User;
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

    DbHelper dbHelper;

    String careerColor;

    TextView txtMyCareerName;
    TextView txtMyCareerIntro;
    LinearLayout rlMyCareerLayout;
    LinearLayout loader;
    ImageView imgMyCareerCover;
    ImageButton btnMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_my_career);

        dbHelper = new DbHelper(getApplicationContext());

        MyCareer myCareer = dbHelper.getMyCareer();
        User user = dbHelper.getUser();

        txtMyCareerName = (TextView) findViewById(R.id.txtMyCareerName);
        txtMyCareerIntro = (TextView) findViewById(R.id.txtMyCareerIntro);
        rlMyCareerLayout = (LinearLayout) findViewById(R.id.rlMyCareerLayout);
        loader = (LinearLayout) findViewById(R.id.llHeaderProgress);
        imgMyCareerCover = (ImageView) findViewById(R.id.imgMyCareerCover);

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        Button btnMyProgress = (Button) findViewById(R.id.btnMyCareerProgress);
        btnMyProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyProgressFragmentActivity.class));
            }
        });

        Button btnMyCareerResources = (Button) findViewById(R.id.btnMyCareerResources);
        btnMyCareerResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyResourcesFragmentActivity.class));
            }
        });

        // Verify user career
        if (myCareer.getId() != null) {
            getMyCareer(myCareer.getId());
        } else if (user.getCareerPathId() != null && user.getCareerPathId().length() > 0) {
            getMyCareer(user.getCareerPathId());
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void getMyCareer(final String careerId) {
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("career_content").query();

            loader.setVisibility(View.VISIBLE);

            queryCareers.where("uid", careerId).exec(new QueryResultsCallBack() {
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

                        drawMenuButton(careerColor);

                        MyCareer myCareer = new MyCareer(careerId, careerName, careerColor);
                        dbHelper.deleteMyCareer();
                        dbHelper.addMyCareer(myCareer);

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
                                "Something went wrong, please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                    loader.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawMenuButton(String color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor(color));
        shape.setStroke(3, Color.WHITE);
        btnMenu.setBackground(shape);
    }
}
