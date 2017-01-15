package com.findigital.blossom.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.findigital.blossom.R;
import com.findigital.blossom.adapters.SurveyResultListAdapter;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.Career;

import java.util.ArrayList;

/**
 * Created by 14-AB109LA on 9/1/2017.
 */

public class SurveyResultFragmentActivity extends FragmentActivity {

    ListView lvCareers;
    DbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_survey_result);

        dbHelper = new DbHelper(getApplicationContext());

        final LinearLayout llHeaderProgress = (LinearLayout) findViewById(R.id.llHeaderProgress);

        ArrayList<Career> careers = new ArrayList<>();

        Intent intent = getIntent();
        Integer totalPoints = Integer.valueOf(intent.getStringExtra("totalPoints"));

        lvCareers = (ListView) findViewById(R.id.lvCareers);

        if (totalPoints >= 10 && totalPoints <= 12) {
            careers.add(dbHelper.getCareer("blt97cfafea7c57e1f4")); // Teacher
            careers.add(dbHelper.getCareer("bltdfcc3b584c48aa59")); // Nurse
            careers.add(dbHelper.getCareer("blta3031a7045f6142e")); // Photographer
        } else if (totalPoints >= 13 && totalPoints <= 15) {
            careers.add(dbHelper.getCareer("blta3031a7045f6142e")); // Photographer
            careers.add(dbHelper.getCareer("blt97cfafea7c57e1f4")); // Teacher
            careers.add(dbHelper.getCareer("blt0ec7e6c9e5f18824")); // Flight Attendant / Pilot
        } else if (totalPoints >= 16 && totalPoints <= 18) {
            careers.add(dbHelper.getCareer("blt0ec7e6c9e5f18824")); // Flight Attendant / Pilot
            careers.add(dbHelper.getCareer("blta3031a7045f6142e")); // Photographer
            careers.add(dbHelper.getCareer("blt0dcea7e71afb68d0")); // Engineer)
        } else if (totalPoints >= 19 && totalPoints <= 21) {
            careers.add(dbHelper.getCareer("blt0dcea7e71afb68d0")); // Engineer
            careers.add(dbHelper.getCareer("blt0ec7e6c9e5f18824")); // Flight Attendant / Pilot
            careers.add(dbHelper.getCareer("blt0e334ce41688ab54")); // Auto-Mechanic
        } else if (totalPoints >= 22 && totalPoints <= 24) {
            careers.add(dbHelper.getCareer("blt0e334ce41688ab54")); // Auto-Mechanic
            careers.add(dbHelper.getCareer("blt0dcea7e71afb68d0")); // Engineer
            careers.add(dbHelper.getCareer("blt78c91eb5e26f7719")); // Welder
        } else if (totalPoints >= 25 && totalPoints <= 27) {
            careers.add(dbHelper.getCareer("blt78c91eb5e26f7719")); // Welder
            careers.add(dbHelper.getCareer("blt0e334ce41688ab54")); // Auto-Mechanic
            careers.add(dbHelper.getCareer("blt78f80012508bfbe3")); // Electrician
        } else if (totalPoints >= 28 && totalPoints <= 30) {
            careers.add(dbHelper.getCareer("blt78f80012508bfbe3")); // Electrician
            careers.add(dbHelper.getCareer("blt78c91eb5e26f7719")); // Welder
            careers.add(dbHelper.getCareer("bltee237aa3c6214e70")); // Video Game Designer
        } else if (totalPoints >= 31 && totalPoints <= 33) {
            careers.add(dbHelper.getCareer("bltee237aa3c6214e70")); // Video Game Designer
            careers.add(dbHelper.getCareer("blt78f80012508bfbe3")); // Electrician
            careers.add(dbHelper.getCareer("bltb0b0253a269124ff")); // Military
        } else if (totalPoints >= 34 && totalPoints <= 35) {
            careers.add(dbHelper.getCareer("bltb0b0253a269124ff")); // Military
            careers.add(dbHelper.getCareer("bltee237aa3c6214e70")); // Video Game Designer
            careers.add(dbHelper.getCareer("bltdfcc3b584c48aa59")); // Nurse
        } else if (totalPoints >= 36 && totalPoints <= 40) {
            careers.add(dbHelper.getCareer("bltdfcc3b584c48aa59")); // Nurse
            careers.add(dbHelper.getCareer("bltb0b0253a269124ff")); // Military
            careers.add(dbHelper.getCareer("blt97cfafea7c57e1f4")); // Teacher
        }

        SurveyResultListAdapter listAdapter =
                new SurveyResultListAdapter(getApplicationContext(), SurveyResultFragmentActivity.this, careers);
        lvCareers.setAdapter(listAdapter);

        llHeaderProgress.setVisibility(View.GONE);

    }
}
