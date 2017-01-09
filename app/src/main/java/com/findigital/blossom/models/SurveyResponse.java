package com.findigital.blossom.models;

import android.provider.BaseColumns;

/**
 * Created by 14-AB109LA on 8/1/2017.
 */

public class SurveyResponse {

    String _id;
    String _title;
    String _question;
    String _points;
    String _careers;

    public SurveyResponse() {}

    public static class SurveyResponseEntry implements BaseColumns {
        public static final String TABLE_NAME = "survey_responses";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_CAREERS = "careers";
    }

    public String getQuestion() {
        return _question;
    }

    public void setQuestion(String _question) {
        this._question = _question;
    }

    public String getID() {
        return _id;
    }

    public void setID(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getPoints() {
        return _points;
    }

    public void setPoints(String _points) {
        this._points = _points;
    }

    public String getCareers() {
        return _careers;
    }

    public void setCareers(String _careers) {
        this._careers = _careers;
    }
}
