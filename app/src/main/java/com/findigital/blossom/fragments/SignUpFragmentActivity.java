package com.findigital.blossom.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.findigital.blossom.R;

/**
 * Created by 14-AB109LA on 9/1/2017.
 */

public class SignUpFragmentActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_sign_up);

        final EditText editUserEmail = (EditText) findViewById(R.id.editUserEmail);
        final EditText editUserPassword = (EditText) findViewById(R.id.editUserPassword);
        final EditText editUserPasswordConfirm = (EditText) findViewById(R.id.editUserPasswordConfirm);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editUserEmail.getText().toString();
                String password = editUserPassword.getText().toString();
                String passwordConfirm = editUserPasswordConfirm.getText().toString();

                if (!password.equals(passwordConfirm)) {
                    showDialogMsg("Error", "Password do not match");
                } else {
                    Intent signUp2 = new Intent(getApplicationContext(), SignUp2FragmentActivity.class);
                    signUp2.putExtra("email", email);
                    signUp2.putExtra("password", password);
                    startActivity(signUp2);
                }
            }
        });
    }

    private void showDialogMsg(String pContent, String pTitle) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle(pTitle)
                .setMessage(pContent)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
