package com.findigital.blossom.models;

import android.provider.BaseColumns;

/**
 * Created by 14-AB109LA on 8/1/2017.
 */

public class SurveyResult {

    String question;
    String response;

    public SurveyResult() {}

    public static class SurveyResultEntry implements BaseColumns {
        public static final String TABLE_NAME = "survey_results";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_RESPONSE = "response";
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
