package com.findigital.blossom.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.findigital.blossom.R;
import com.findigital.blossom.activities.MainActivity;
import com.findigital.blossom.adapters.IntroPageAdapter;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by 14-AB109LA on 27/12/2016.
 */

public class IntroFragmentActivity extends FragmentActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    String[] introContent;
    String[] introTitles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_intro);

        introContent = new String[] {
                getResources().getString(R.string.intro_text_1),
                getResources().getString(R.string.intro_text_2),
                getResources().getString(R.string.intro_text_3)
        };

        introTitles = new String[] {
                getResources().getString(R.string.intro_title_1),
                getResources().getString(R.string.intro_title_2),
                getResources().getString(R.string.intro_title_3)
        };

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new IntroPageAdapter(getApplicationContext(), introContent, introTitles);
        viewPager.setAdapter(pagerAdapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        Button btnGetStarted = (Button) findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
