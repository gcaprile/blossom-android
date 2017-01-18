package com.findigital.blossom.fragments;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.findigital.blossom.R;
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
    String[] introColors;

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

        introColors = new String[] {
                "#3023AE",
                "#1EE196",
                "#F6A623"
        };

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new IntroPageAdapter(getApplicationContext(), introContent, introTitles);
        viewPager.setAdapter(pagerAdapter);

        final ScrollView background = (ScrollView) findViewById(R.id.background);
        final ImageView imgIntroCover = (ImageView) findViewById(R.id.imgIntroCover);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                background.setBackgroundColor(Color.parseColor(introColors[position]));

                if (position == 0) {
                    imgIntroCover.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.intro_cover_1));
                } else if (position == 1) {
                    imgIntroCover.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.intro_cover_2));
                } else if (position == 2) {
                    imgIntroCover.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.intro_cover_3));
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
