package com.findigital.blossom.models;

import android.provider.BaseColumns;

/**
 * Created by 14-AB109LA on 9/1/2017.
 */

public class Career {

    String _id;
    String _career_name;
    String _career_intro;
    String _career_color;

    public Career() {}

    public static class CareerEntry implements BaseColumns {
        public static final String TABLE_NAME = "careers";
        public static final String COLUMN_CAREER_NAME = "career_name";
        public static final String COLUMN_CAREER_INTRO = "career_intro";
        public static final String COLUMN_CAREER_COLOR = "career_color";
    }

    public Career(String _id, String _career_name, String _career_intro, String _career_color) {
        this._id = _id;
        this._career_name = _career_name;
        this._career_intro = _career_intro;
        this._career_color = _career_color;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getCareerName() {
        return _career_name;
    }

    public void setCareerName(String _career_name) {
        this._career_name = _career_name;
    }

    public String getCareerIntro() {
        return _career_intro;
    }

    public void setCareerIntro(String _career_intro) {
        this._career_intro = _career_intro;
    }

    public String getCareerColor() {
        return _career_color;
    }

    public void setCareerColor(String _career_color) {
        this._career_color = _career_color;
    }
}
