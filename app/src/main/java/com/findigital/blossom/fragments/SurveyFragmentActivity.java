package com.findigital.blossom.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.findigital.blossom.R;
import com.findigital.blossom.adapters.QuestionsPageAdapter;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.SurveyQuestion;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.utilities.BuiltConstant;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by 14-AB109LA on 8/1/2017.
 */

public class SurveyFragmentActivity extends FragmentActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    DbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_survey);

        dbHelper = new DbHelper(getApplicationContext());

        LinearLayout llHeaderProgress = (LinearLayout) findViewById(R.id.llHeaderProgress);
        viewPager = (ViewPager) findViewById(R.id.pagerQuestions);

        pagerAdapter = new QuestionsPageAdapter(getApplicationContext(), dbHelper.getAllQuestions());
        viewPager.setAdapter(pagerAdapter);

        // This prevents rewriting questions UI (Android does that at third page by default)
        // Set the limit to the amount of pages
        Integer totalPages = dbHelper.getAllQuestions().size() + 1;
        viewPager.setOffscreenPageLimit(totalPages);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        llHeaderProgress.setVisibility(View.GONE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.deleteAllSurveyResults();
        dbHelper.deleteAllSurveyCareerResults();
    }

}
