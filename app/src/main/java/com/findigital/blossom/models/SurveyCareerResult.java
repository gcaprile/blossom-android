package com.findigital.blossom.models;

import android.provider.BaseColumns;

/**
 * Created by 14-AB109LA on 8/1/2017.
 */

public class SurveyCareerResult {

    String _id;
    String career;
    String points;

    public SurveyCareerResult() {}

    public static class SurveyCareerResultEntry implements BaseColumns {
        public static final String TABLE_NAME = "survey_career_results";
        public static final String COLUMN_CAREER = "career";
        public static final String COLUMN_POINTS = "points";
    }

    public String getID() {
        return _id;
    }

    public void setID(String _id) {
        this._id = _id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

}
