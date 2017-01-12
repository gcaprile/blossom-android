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

import com.findigital.blossom.R;
import com.findigital.blossom.helpers.API;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.utilities.BuiltConstant;

import static com.raweng.built.utilities.BuiltConstant.LogType.error;

/**
 * Created by 14-AB109LA on 11/1/2017.
 */

public class LoginFragmentActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

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

                    Log.d("USERNAME", userEmail);
                    Log.d("PASSWORD", userPwd);

                    userObject.login(userEmail, userPwd, new BuiltResultCallBack() {

                        @Override
                        public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                            if (error == null) {
                                // user has logged in successfully
                                System.out.println("LOGGED IN");
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
        });
    }
}
