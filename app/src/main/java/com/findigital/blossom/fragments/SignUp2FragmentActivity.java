package com.findigital.blossom.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.helpers.API;
import com.raweng.built.Built;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.utilities.BuiltConstant;

/**
 * Created by 14-AB109LA on 12/1/2017.
 */

public class SignUp2FragmentActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_sign_up_2);

        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");
        final String password = intent.getStringExtra("password");

        Button btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BuiltUser userObject = Built.application(getApplicationContext(), API.API_KEY).user();

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
                            }else{
                                System.out.println(error.getErrorMessage());
                                Toast.makeText(
                                        getApplicationContext(),
                                        error.getErrorMessage(),
                                        Toast.LENGTH_SHORT
                                ).show();
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
