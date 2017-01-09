package com.findigital.blossom.models;

import android.provider.BaseColumns;

/**
 * Created by 14-AB109LA on 8/1/2017.
 */

public class SurveyQuestion {

    String _id;
    String _question;

    public SurveyQuestion() {}

    public static class SurveyQuestionEntry implements BaseColumns {
        public static final String TABLE_NAME = "questions";
        public static final String COLUMN_QUESTION = "question";
    }

    public SurveyQuestion(String _id, String _question) {
        this._id = _id;
        this._question = _question;
    }

    public String getID() {
        return _id;
    }

    public void setID(String _id) {
        this._id = _id;
    }

    public String getQuestion() {
        return _question;
    }

    public void setQuestion(String _question) {
        this._question = _question;
    }

}
