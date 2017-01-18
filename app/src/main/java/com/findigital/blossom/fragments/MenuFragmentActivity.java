package com.findigital.blossom.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.findigital.blossom.R;
import com.findigital.blossom.activities.MainActivity;
import com.findigital.blossom.helpers.API;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.User;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.utilities.BuiltConstant;

import org.w3c.dom.Text;

/**
 * Created by 14-AB109LA on 11/1/2017.
 */

public class MenuFragmentActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.fragment_menu);

        final DbHelper dbHelper = new DbHelper(getApplicationContext());

        final User user = dbHelper.getUser();

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

        TextView txtMenuItemMyCareer = (TextView) findViewById(R.id.txtMenuItemMyCareer);
        txtMenuItemMyCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MyCareerFragmentActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });

        TextView txtMenuItemResources = (TextView) findViewById(R.id.txtMenuItemResources);
        txtMenuItemResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyResourcesFragmentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        TextView txtMenuItemFinancialLiteracy = (TextView) findViewById(R.id.txtMenuItemFinancialLiteracy);
        // Todo: OnClickListener

        TextView txtMenuItemContact = (TextView) findViewById(R.id.txtMenuItemContact);
        txtMenuItemContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"aba@fin-digital.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Blossom for Android Feedback");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
            }
        });

        TextView txtMenuItemRestartPath = (TextView) findViewById(R.id.txtMenuItemRestartPath);
        txtMenuItemRestartPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartCareerPath(user.getId());
            }
        });

        TextView txtMenuItemLogout = (TextView) findViewById(R.id.txtMenuItemLogout);
        txtMenuItemLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove user data from device database
                dbHelper.deleteUsers();
                // Logout from Facebook
                LoginManager.getInstance().logOut();
                // Navigate to Main view
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout llMenuItemMyCareer = (LinearLayout) findViewById(R.id.llMenuItemMyCareer);
        LinearLayout llMenuItemResources = (LinearLayout) findViewById(R.id.llMenuItemResources);
        LinearLayout llMenuItemRestartPath = (LinearLayout) findViewById(R.id.llMenuItemRestartPath);


        // Verify user session status
        if (user.getId() != null) {
            // User logged in
            llMenuItemMyCareer.setVisibility(View.VISIBLE);
            llMenuItemResources.setVisibility(View.VISIBLE);
            llMenuItemRestartPath.setVisibility(View.VISIBLE);
            txtMenuItemLogout.setVisibility(View.VISIBLE);
            txtMenuItemLogin.setVisibility(View.GONE);
        } else {
            // User not logged in
            llMenuItemMyCareer.setVisibility(View.GONE);
            llMenuItemResources.setVisibility(View.GONE);
            llMenuItemRestartPath.setVisibility(View.GONE);
            txtMenuItemLogout.setVisibility(View.GONE);
            txtMenuItemLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    private void restartCareerPath(String userUid) {
        try {
            BuiltApplication builtApplication = Built.application(getApplicationContext(), API.API_KEY);
            final BuiltUser userObject  =  builtApplication.user(userUid);

            userObject.set("selected_career", "");
            userObject.set("completed_tasks", "");
            userObject.set("completed_responses", "");

            userObject.updateUserInfo(new BuiltResultCallBack() {

                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                    if (error == null) {
                        // user has logged in successfully
                        System.out.println("CAREER PATH RESTARTED");
                        Toast.makeText(getApplicationContext(),
                                "Your Career Path has been restarted",
                                Toast.LENGTH_SHORT).show();
                        // Navigate to My Career view
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        System.out.println(error);
                        Toast.makeText(getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                        // login failed
                        // refer to the 'error' object for more details
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
