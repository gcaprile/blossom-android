package com.findigital.blossom.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.View;
import android.view.WindowManager;

import com.findigital.blossom.R;
import com.findigital.blossom.helpers.API;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Ramon Zuniga on 21/12/2016.
 */

public class SplashActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = API.TWITTER_KEY;
    private static final String TWITTER_SECRET = API.TWITTER_SECRET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();

        handler.postDelayed(runnable, 3000);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(getApplicationContext(), new Twitter(authConfig));

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
