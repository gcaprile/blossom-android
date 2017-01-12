package com.findigital.blossom.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;

import com.findigital.blossom.R;
import com.findigital.blossom.fragments.CareersFragmentActivity;
import com.findigital.blossom.fragments.IntroFragmentActivity;
import com.findigital.blossom.fragments.MenuFragmentActivity;
import com.findigital.blossom.fragments.SurveyFragmentActivity;
import com.findigital.blossom.helpers.API;
import com.findigital.blossom.helpers.DbHelper;
import com.findigital.blossom.models.Career;
import com.findigital.blossom.models.Setting;
import com.findigital.blossom.models.SurveyQuestion;
import com.findigital.blossom.models.SurveyResponse;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltQueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.raweng.built.utilities.BuiltConstant;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class MainActivity extends Activity {

    DbHelper dbHelper;
    Animation slideLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(getApplicationContext());

        downloadQuestions();
        downloadSurveyResponses();
        downloadCareers();

        Integer displayIntro = 1;
        Setting setting = new Setting();
        List<Setting> settings = dbHelper.getAllSettings();

        for (Setting s : settings) {
            displayIntro = s.getDisplayIntro();
        }

        if (displayIntro == 1) {
            // Update settings to not display intro anymore
            setting.setDisplayIntro(0);
            dbHelper.updateSettings(setting);

            // Display intro view
            Intent intent = new Intent(this, IntroFragmentActivity.class);
            startActivity(intent);
        }

        final Intent careers = new Intent(this, CareersFragmentActivity.class);
        final Intent survey = new Intent(this, SurveyFragmentActivity.class);

        Button btnNavCareers = (Button) findViewById(R.id.btnNavCareers);
        btnNavCareers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(careers);
            }
        });

        // Navigate to Survey
        Button btnNavFindCareer = (Button) findViewById(R.id.btnNavFindCareer);
        btnNavFindCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(survey);
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
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    private void downloadCareers() {
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryCareers = builtApplication.classWithUid("career_content").query();

            queryCareers.ascending("career_name");

            queryCareers.exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        List<BuiltObject> careersList = queryResultObject.getResultObjects();

                        dbHelper.deleteAllCareers();

                        for (int i = 0; i < careersList.size(); i++) {
                            BuiltObject item = careersList.get(i);

                            Career career = new Career();
                            career.setId(item.get("uid").toString());
                            career.setCareerName(item.get("career_name").toString());
                            career.setCareerIntro(item.get("career_intro").toString());

                            dbHelper.addCareer(career);
                        }

                    }else{
                        System.out.println(error.getErrorMessage());
                        // query failed
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadQuestions() {
        // Get list of questions available
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            final BuiltQuery queryQuestions = builtApplication.classWithUid("career_survey").query();

            queryQuestions.ascending("order");

            queryQuestions.exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        List<BuiltObject> questionsList = queryResultObject.getResultObjects();

                        dbHelper.deleteAllQuestions();

                        for (int i = 0; i < questionsList.size(); i++) {
                            BuiltObject item = questionsList.get(i);

                            SurveyQuestion surveyQuestion = new SurveyQuestion();
                            surveyQuestion.setID(item.get("uid").toString());
                            surveyQuestion.setQuestion(item.get("survey_question").toString());

                            dbHelper.addQuestion(surveyQuestion);
                        }

                    }else{
                        System.out.println(error.getErrorMessage());
                        // query failed
                        // refer to the 'error' object for more details
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadSurveyResponses() {
        // Get list of all survey responses available
        try {
            BuiltApplication builtApplication  = Built.application(getApplicationContext(), API.API_KEY);
            BuiltQuery queryQuestions = builtApplication.classWithUid("survey_response").query();

            queryQuestions.exec(new QueryResultsCallBack() {
                @Override
                public void onCompletion(BuiltConstant.BuiltResponseType responseType, BuiltQueryResult queryResultObject, BuiltError error) {
                    if(error == null){
                        List<BuiltObject> responsesList = queryResultObject.getResultObjects();

                        dbHelper.deleteAllSurveyResponses();

                        for (int i = 0; i < responsesList.size(); i++) {
                            BuiltObject item = responsesList.get(i);

                            JSONArray questionsList;
                            questionsList = (JSONArray) item.get("references_survey_question");

                            JSONArray careersList;
                            careersList = (JSONArray) item.get("response_career_reference");

                            String responseQuestions = "";
                            String careers = "";

                            try {
                                responseQuestions = questionsList.get(0).toString();
                                careers = careersList.join(",");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            SurveyResponse surveyResponse = new SurveyResponse();
                            surveyResponse.setID(item.get("uid").toString());
                            surveyResponse.setTitle(item.get("response_title").toString());
                            surveyResponse.setQuestion(responseQuestions);
                            surveyResponse.setCareers(careers);
                            surveyResponse.setPoints(item.get("points").toString());

                            dbHelper.addResponse(surveyResponse);
                        }

                    }else{
                        System.out.println(error.getErrorMessage());
                        // query failed
                        // refer to the 'error' object for more details
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
