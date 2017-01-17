package com.findigital.blossom.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.findigital.blossom.R;
import com.findigital.blossom.adapters.LinksPageAdapter;
import com.findigital.blossom.adapters.MyProgressListAdapter;
import com.findigital.blossom.helpers.API;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.MyCareer;
import com.findigital.blossom.models.User;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.utilities.BuiltConstant;
import com.raweng.twitter4j.internal.org.json.JSONArray;
import com.raweng.twitter4j.internal.org.json.JSONException;
import com.raweng.twitter4j.internal.org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by 14-AB109LA on 16/1/2017.
 */

public class MyProgressFragmentActivity extends FragmentActivity {

    Integer totalCompleted;

    String careerId;
    String[] userResponsesArray;

    ListView lvTasks;
    LinearLayout llHeaderProgress;

    DbHelper dbHelper;

    User user;
    MyCareer myCareer;

    List<BuiltObject> userResponses;

    TextView txtProgressStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_my_progress);

        dbHelper = new DbHelper(getApplicationContext());
        user = dbHelper.getUser();

        lvTasks = (ListView) findViewById(R.id.lvTasks);

        txtProgressStatus = (TextView) findViewById(R.id.txtProgressStatus);

        LinearLayout llProgressLayout = (LinearLayout) findViewById(R.id.llMyProgressLayout);
        llHeaderProgress = (LinearLayout) findViewById(R.id.llHeaderProgress);
        llHeaderProgress.setVisibility(View.VISIBLE);

        DbHelper dbHelper = new DbHelper(getApplicationContext());
        myCareer = dbHelper.getMyCareer();

        careerId = myCareer.getId();

        llProgressLayout.setBackgroundColor(Color.parseColor("#" + myCareer.getColor()));

        ImageButton btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuFragmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        getUserResponses();
    }

    private void getProgress() {
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("tasks").query();

            queryCareers.ascending("created_at");

            queryCareers.where("career_reference", careerId).exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        // the queryResultObject will contain the objects of the class
                        List<BuiltObject> tasks = queryResultObject.getResultObjects();

                        MyProgressListAdapter listAdapter =
                                new MyProgressListAdapter(getApplicationContext(),
                                        MyProgressFragmentActivity.this,
                                        tasks,
                                        userResponses);
                        lvTasks.setAdapter(listAdapter);

                        Integer totalTasks = tasks.size();

                        updateProgressStatus(totalTasks, totalCompleted);

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
                    llHeaderProgress.setVisibility(GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserResponses() {
        try {
            BuiltApplication builtApplication = Built.application(getApplicationContext(), API.API_KEY);
            final BuiltUser userObject = builtApplication.user(user.getId());

            userObject.fetch(new BuiltResultCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltError error) {
                    if (error == null) {
                        try {
                            JSONArray jsonArray = new JSONArray(userObject.get("completed_responses").toString());
                            userResponsesArray = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                userResponsesArray[i] = jsonArray.getString(i);
                            }
                            totalCompleted = userResponsesArray.length;
                            getUserTasks();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(error);
                        Toast.makeText(getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserTasks() {
        try {
            BuiltApplication builtApplication = Built.application(getApplicationContext(), API.API_KEY);
            final BuiltQuery tasksQuery = builtApplication.classWithUid("task_response").query();

            tasksQuery.containedIn("uid", userResponsesArray);

            tasksQuery.exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType builtResponseType, BuiltQueryResult builtQueryResult, BuiltError error) {
                    if (error == null) {
                        userResponses = builtQueryResult.getResultObjects();
                        getProgress();
                    } else {
                        System.out.println(error);
                        Toast.makeText(getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProgressStatus(Integer totalTasks, Integer totalCompleted) {
        String progressMsg;

        if (totalCompleted > 0) {
            progressMsg =
                    "Nice! You have completed " + String.valueOf(totalCompleted) +
                            " out of " + String.valueOf(totalTasks) + " tasks " +
                            "towards your " + myCareer.getName() + " career." +
                            "\n\nKeep it going!";
        } else {
            progressMsg =
                    "You have completed " + String.valueOf(totalCompleted) +
                            " out of " + String.valueOf(totalTasks) + " tasks " +
                            "towards your " + myCareer.getName() + " career." +
                            "\n\nLet's get started!";
        }

        txtProgressStatus.setText(progressMsg);
    }
}
