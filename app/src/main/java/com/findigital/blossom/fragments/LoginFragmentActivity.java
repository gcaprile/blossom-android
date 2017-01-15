package com.findigital.blossom.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.findigital.blossom.R;
import com.findigital.blossom.helpers.API;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.utilities.BuiltConstant;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.w3c.dom.Text;

import static android.view.View.GONE;

/**
 * Created by 14-AB109LA on 11/1/2017.
 */

public class LoginFragmentActivity extends FragmentActivity {

    Integer step = 0; /* 0 = Login, 1 = Sign up (1/2), 2 = Sign up (2/2) */
    String TAG = FragmentActivity.class.getSimpleName();
    String email;
    String password;
    String passwordConfirm;

    CallbackManager callbackManager;
    TwitterLoginButton twitterLoginButton;
    LoginButton fbLoginButton;

    Button btnLoginSubmit;

    TextView txtNavLogin;
    TextView txtNavSignUp;
    TextView txtForgotPassword;
    TextView txtLoginPager;
    TextView txtLoginTitle;

    EditText editLoginEmail;
    EditText editLoginPassword;
    EditText editUserPasswordConfirm;
    EditText editUserFirstName;
    EditText editUserLastName;

    LinearLayout llLoginFooter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.fragment_login);

        callbackManager = CallbackManager.Factory.create();

        txtNavSignUp = (TextView) findViewById(R.id.txtNavSignUp);
        txtNavLogin = (TextView) findViewById(R.id.txtNavLogin);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        txtLoginPager = (TextView) findViewById(R.id.txtLoginPager);
        txtLoginTitle = (TextView) findViewById(R.id.txtLoginTitle);

        editLoginEmail = (EditText) findViewById(R.id.editLoginEmail);
        editLoginPassword = (EditText) findViewById(R.id.editLoginPassword);
        editUserPasswordConfirm = (EditText) findViewById(R.id.editUserPasswordConfirm);
        editUserFirstName = (EditText) findViewById(R.id.editUserFirstName);
        editUserLastName = (EditText) findViewById(R.id.editUserLastName);

        llLoginFooter = (LinearLayout) findViewById(R.id.llLoginFooter);

        btnLoginSubmit = (Button) findViewById(R.id.btnLoginSubmit);

        txtNavSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpUI();
            }
        });

        txtNavLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginUI();
            }
        });

        fbLoginButton = (LoginButton) findViewById(R.id.btnFacebookLogin);
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

        btnLoginSubmit = (Button) findViewById(R.id.btnLoginSubmit);
        btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (step == 0) {
                    // Login
                    loginWithEmail();
                } else if (step == 1) {
                    // Go to sign up page 2
                    email = editLoginEmail.getText().toString();
                    password = editLoginPassword.getText().toString();
                    passwordConfirm = editUserPasswordConfirm.getText().toString();

                    if (email.isEmpty()) {
                        showDialogMsg("Enter an Email Address");
                    } else if (password.isEmpty()) {
                        showDialogMsg("Enter a password");
                    } else if (passwordConfirm.isEmpty()) {
                        showDialogMsg("Enter password confirmation");
                    } else {
                        if (!password.equals(passwordConfirm)) {
                            showDialogMsg("Password do not match");
                        } else {
                            showSignUpUI2();
                        }
                    }
                } else if (step == 2) {
                    // Register new user
                    registerNewUser();
                }
            }
        });

        ImageButton btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        switch (step) {
            case 1:
                showLoginUI();
                break;
            case 2:
                showSignUpUI();
                break;
            default:
                super.onBackPressed();
                break;
        }
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

    private void loginWithEmail() {
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

    private void registerNewUser() {
        try {
            final BuiltUser userObject = Built.application(getApplicationContext(), API.API_KEY).user();

            final LinearLayout loader = (LinearLayout) findViewById(R.id.llHeaderProgress);
            loader.setVisibility(View.VISIBLE);

            btnLoginSubmit.setEnabled(false);

            EditText editUserFirstName = (EditText) findViewById(R.id.editUserFirstName);
            EditText editUserLastName = (EditText) findViewById(R.id.editUserLastName);

            String firstName = editUserFirstName.getText().toString();
            String lastName = editUserLastName.getText().toString();

            userObject.setEmail(email);
            userObject.setUserName(email);
            userObject.setPassword(password);
            userObject.setConfirmPassword(password);
            userObject.setFirstName(firstName);
            userObject.setLastName(lastName);

            userObject.register(new BuiltResultCallBack() {

                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                    if(error == null){
                        System.out.println("USER REGISTERED!");
                        Log.i(TAG, userObject.getUserUid());
                    }else{
                        System.out.println(error.getErrorMessage());
                        Toast.makeText(
                                getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    loader.setVisibility(GONE);
                    btnLoginSubmit.setEnabled(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSignUpUI() {
        fbLoginButton.setVisibility(View.VISIBLE);
        twitterLoginButton.setVisibility(View.VISIBLE);

        editLoginEmail.setVisibility(View.VISIBLE);
        editLoginPassword.setVisibility(View.VISIBLE);
        editUserFirstName.setVisibility(GONE);
        editUserLastName.setVisibility(GONE);
        editUserPasswordConfirm.setVisibility(View.VISIBLE);

        txtForgotPassword.setVisibility(GONE);
        txtNavSignUp.setVisibility(GONE);
        txtLoginPager.setText("1/2");
        txtLoginTitle.setText(getString(R.string.create_account_title));
        txtNavLogin.setVisibility(View.VISIBLE);

        llLoginFooter.setVisibility(View.VISIBLE);

        btnLoginSubmit.setText(getString(R.string.submit));

        step = 1;
    }

    private void showSignUpUI2() {
        editUserFirstName.setVisibility(View.VISIBLE);
        editUserLastName.setVisibility(View.VISIBLE);

        fbLoginButton.setVisibility(GONE);
        twitterLoginButton.setVisibility(GONE);

        editLoginEmail.setVisibility(GONE);
        editLoginPassword.setVisibility(GONE);
        editUserPasswordConfirm.setVisibility(GONE);

        txtNavLogin.setVisibility(GONE);

        txtLoginPager.setText("2/2");

        btnLoginSubmit.setText(getString(R.string.create_account));

        step = 2;
    }

    private void showLoginUI() {
        editUserPasswordConfirm.setVisibility(GONE);
        editUserFirstName.setVisibility(GONE);
        editUserLastName.setVisibility(GONE);
        editLoginEmail.setVisibility(View.VISIBLE);
        editLoginPassword.setVisibility(View.VISIBLE);

        llLoginFooter.setVisibility(GONE);

        txtForgotPassword.setVisibility(View.VISIBLE);
        txtNavSignUp.setVisibility(View.VISIBLE);
        txtLoginTitle.setText(getString(R.string.login_title));

        btnLoginSubmit.setText(getString(R.string.submit));

        step = 0;
    }

    private void showDialogMsg(String pContent) {
        new AlertDialog.Builder(LoginFragmentActivity.this)
                .setTitle(getString(R.string.create_account))
                .setMessage(pContent)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing
                    }
                })
                .show();
    }
}
