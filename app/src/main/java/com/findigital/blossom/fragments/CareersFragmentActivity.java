package com.findigital.blossom.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.findigital.blossom.R;
import com.findigital.blossom.adapters.CareersListAdapter;
import com.findigital.blossom.adapters.CareersPageAdapter;
import com.findigital.blossom.helpers.API;
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
 * Created by 14-AB109LA on 28/12/2016.
 */

public class CareersFragmentActivity extends FragmentActivity {

    String careerId;
    Boolean listVisible = false;

    ViewPager viewPager;
    CareersListAdapter listAdapter;
    PagerAdapter pagerAdapter;
    LinearLayout llFindCareer;

    List<BuiltObject> careers;
    ListView lvCareers;

    Button btnCareerDetail;
    EditText editSearch;
    TextView txtCareersTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_careers);

        llFindCareer = (LinearLayout) findViewById(R.id.llFindCareer);

        ImageButton btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        lvCareers = (ListView) findViewById(R.id.lvCareers);
        lvCareers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                careerId = careers.get(position).getUid();

                Intent intent = new Intent(getApplicationContext(), CareerDetailFragment.class);
                intent.putExtra("careerId", careerId);
                startActivity(intent);
            }
        });

        txtCareersTitle = (TextView) findViewById(R.id.txtCareersTitle);

        btnCareerDetail = (Button) findViewById(R.id.btnCareerDetail);
        btnCareerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CareerDetailFragment.class);
                intent.putExtra("careerId", careerId);
                startActivity(intent);
            }
        });

        editSearch = (EditText) findViewById(R.id.editSearch);
        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lvCareers.setVisibility(View.VISIBLE);
                    txtCareersTitle.setText(getString(R.string.search));
                    listVisible = true;
                    togglePager(false);
                }
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewPager = (ViewPager) findViewById(R.id.pagerCareers);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String careerColor = "#" + careers.get(position).get("career_color").toString();
                careerId = careers.get(position).getUid();

                llFindCareer.setBackgroundColor(Color.parseColor(careerColor));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        try {
            final LinearLayout llHeaderProgress = (LinearLayout) findViewById(R.id.llHeaderProgress);
            llHeaderProgress.setVisibility(View.VISIBLE);
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("career_content").query();

            queryCareers.ascending("career_name");

            queryCareers.exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        careers = queryResultObject.getResultObjects();

                        pagerAdapter = new CareersPageAdapter(getApplicationContext(), careers);
                        viewPager.setAdapter(pagerAdapter);

                        listAdapter = new CareersListAdapter(getApplicationContext(), careers);
                        lvCareers.setAdapter(listAdapter);

                        String careerColor = "#" + careers.get(0).get("career_color").toString();
                        careerId = careers.get(0).getUid();

                        llFindCareer.setBackgroundColor(Color.parseColor(careerColor));
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
    }

    @Override
    public void onBackPressed() {
        if (listVisible) {
            lvCareers.setVisibility(View.GONE);
            togglePager(true);
            txtCareersTitle.setText(getString(R.string.find_a_career));
            editSearch.clearFocus();
            listVisible = false;
        } else {
            finish();
        }
    }

    public void togglePager(Boolean show) {
        if (show) {
            btnCareerDetail.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
        } else {
            btnCareerDetail.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
        }
    }
}
