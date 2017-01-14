package com.findigital.blossom.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.findigital.blossom.R;
import com.findigital.blossom.helpers.API;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.utilities.BuiltConstant;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import static com.raweng.built.utilities.BuiltConstant.LogType.error;

/**
 * Created by 14-AB109LA on 11/1/2017.
 */

public class LoginFragmentActivity extends FragmentActivity {

    String TAG = FragmentActivity.class.getSimpleName();
    CallbackManager callbackManager;
    TwitterLoginButton twitterLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.fragment_login);

        callbackManager = CallbackManager.Factory.create();

        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.btnFacebookLogin);
        fbLoginButton.setReadPermissions("email");

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.btnTwitterLogin);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                // TwitterSession session = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = result.data.getAuthToken();
                String token = authToken.token;
                String tokenSecret = authToken.secret;

                loginUsingTwitter(token, tokenSecret);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.e(TAG, exception.getMessage());
                Toast.makeText(getApplicationContext(),
                        exception.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            // Logged In with Facebook
            // Todo: Continue to My Career view
        }

        // Callback registration
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginUsingFacebook(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, exception.getMessage());
                Toast.makeText(getApplicationContext(),
                        exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        TextView txtLoginItemSignUp = (TextView) findViewById(R.id.txtLoginItemSignUp);
        txtLoginItemSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpFragmentActivity.class));
            }
        });

        Button btnLoginSubmit = (Button) findViewById(R.id.btnLoginSubmit);
        btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BuiltApplication builtApplication = Built.application(getApplicationContext(), API.API_KEY);
                    BuiltUser userObject  =  builtApplication.user();

                    EditText editLoginEmail = (EditText) findViewById(R.id.editLoginEmail);
                    EditText editLoginPassword = (EditText) findViewById(R.id.editLoginPassword);

                    String userEmail = editLoginEmail.getText().toString();
                    String userPwd = editLoginPassword.getText().toString();

                    userObject.login(userEmail, userPwd, new BuiltResultCallBack() {

                        @Override
                        public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                            if (error == null) {
                                // user has logged in successfully
                                System.out.println("LOGGED IN");
                            } else {
                                System.out.println(error);
                                Toast.makeText(getApplicationContext(),
                                        error.getErrorMessage() + " Check your Email and Password.",
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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void loginUsingFacebook(String fbAccessToken) {
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltUser userObject  =  builtApplication.user();

            userObject.loginWithFacebookAccessToken(fbAccessToken, new BuiltResultCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                    if(error == null){
                        System.out.println("LOGGED WITH FACEBOOK");
                    }else{
                        Log.e(TAG, error.getErrorMessage());
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

    private void loginUsingTwitter(String token, String tokenSecret) {
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltUser userObject  =  builtApplication.user();

            userObject.loginWithTwitterAccessToken(token, tokenSecret, API.TWITTER_KEY, API.TWITTER_SECRET, new BuiltResultCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                    if(error == null){
                        System.out.println("LOGGED WITH TWITTER");
                    }else{
                        Log.e(TAG, error.getErrorMessage());
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
