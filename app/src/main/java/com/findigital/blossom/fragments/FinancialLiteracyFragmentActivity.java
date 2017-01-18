package com.findigital.blossom.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.adapters.FinancialLiteracyPageAdapter;
import com.findigital.blossom.adapters.LiteracyListAdapter;
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
 * Created by 14-AB109LA on 01/17/2017.
 */

public class FinancialLiteracyFragmentActivity extends FragmentActivity {

    String literacyId;
    Boolean listVisible = false;

    ViewPager viewPager;
    LiteracyListAdapter listAdapter;
    FinancialLiteracyPageAdapter pagerAdapter;
    LinearLayout llBackground;

    List<BuiltObject> literacy;
    ListView lvContentItems;

    Button btnContentDetail;
    EditText editSearch;
    TextView txtContentTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_financial_literacy);

        llBackground = (LinearLayout) findViewById(R.id.llBackground);

        ImageButton btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        lvContentItems = (ListView) findViewById(R.id.lvContentItems);
        lvContentItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                literacyId = literacy.get(position).getUid();

                Intent intent = new Intent(getApplicationContext(), CareerDetailFragment.class);
                intent.putExtra("literacyId", literacyId);
                startActivity(intent);
            }
        });

        txtContentTitle = (TextView) findViewById(R.id.txtContentTitle);

        btnContentDetail = (Button) findViewById(R.id.btnContentDetail);
        btnContentDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CareerDetailFragment.class);
                intent.putExtra("literacyId", literacyId);
                startActivity(intent);
            }
        });

        editSearch = (EditText) findViewById(R.id.editSearch);
        editSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lvContentItems.setVisibility(View.VISIBLE);
                    txtContentTitle.setText(getString(R.string.search));
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

        viewPager = (ViewPager) findViewById(R.id.pagerItems);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String bkgColor = "#" + literacy.get(position).get("fin_lit_background_color").toString();
                literacyId = literacy.get(position).getUid();

                llBackground.setBackgroundColor(Color.parseColor(bkgColor));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        try {
            final LinearLayout llHeaderProgress = (LinearLayout) findViewById(R.id.llHeaderProgress);
            llHeaderProgress.setVisibility(View.VISIBLE);
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery query = builtApplication.classWithUid("financial_literacy").query();

            query.ascending("fin_lit_title");

            query.exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        literacy = queryResultObject.getResultObjects();

                        pagerAdapter = new FinancialLiteracyPageAdapter(getApplicationContext(), literacy);
                        viewPager.setAdapter(pagerAdapter);

                        listAdapter = new LiteracyListAdapter(getApplicationContext(), literacy);
                        lvContentItems.setAdapter(listAdapter);

                        String careerColor = "#" + literacy.get(0).get("fin_lit_background_color").toString();
                        literacyId = literacy.get(0).getUid();

                        llBackground.setBackgroundColor(Color.parseColor(careerColor));
                        llHeaderProgress.setVisibility(View.GONE);

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
    public void onBackPressed() {
        if (listVisible) {
            lvContentItems.setVisibility(View.GONE);
            togglePager(true);
            txtContentTitle.setText(getString(R.string.financial_literacy_caps));
            editSearch.clearFocus();
            listVisible = false;
        } else {
            finish();
        }
    }

    public void togglePager(Boolean show) {
        if (show) {
            btnContentDetail.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
        } else {
            btnContentDetail.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
        }
    }
}
