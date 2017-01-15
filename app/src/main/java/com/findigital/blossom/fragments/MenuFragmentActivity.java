package com.findigital.blossom.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.findigital.blossom.R;
import com.findigital.blossom.activities.MainActivity;

/**
 * Created by 14-AB109LA on 11/1/2017.
 */

public class MenuFragmentActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);

        Button btnCloseMenu = (Button) findViewById(R.id.btnCloseMenu);
        btnCloseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        Button btnMenuHome = (Button) findViewById(R.id.btnMenuHome);
        btnMenuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        TextView txtMenuCareerSearch = (TextView) findViewById(R.id.txtMenuItemCareerSearch);
        txtMenuCareerSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), CareersFragmentActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        TextView txtMenuItemScholly = (TextView) findViewById(R.id.txtMenuItemScholly);
        txtMenuItemScholly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), SchollyFragmentActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        TextView txtMenuItemLogin = (TextView) findViewById(R.id.txtMenuItemLogin);
        txtMenuItemLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginFragmentActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
