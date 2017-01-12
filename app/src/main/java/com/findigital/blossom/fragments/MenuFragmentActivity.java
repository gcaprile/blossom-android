package com.findigital.blossom.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.findigital.blossom.R;

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

        TextView txtMenuItemScholly = (TextView) findViewById(R.id.txtMenuItemScholly);
        txtMenuItemScholly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SchollyFragmentActivity.class));
            }
        });

        TextView txtMenuItemLogin = (TextView) findViewById(R.id.txtMenuItemLogin);
        txtMenuItemLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginFragmentActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
