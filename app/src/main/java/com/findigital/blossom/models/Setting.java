package com.findigital.blossom.models;

import android.provider.BaseColumns;

/**
 * Created by 14-AB109LA on 8/1/2017.
 */

public class Setting {

    int _id;
    int _display_intro;

    public Setting() {}

    public static class SettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "settings";
        public static final String COLUMN_NAME_DISPLAY_INTRO = "display_intro";
    }

    public Setting(int _id, int _display_intro) {
        this._id = _id;
        this._display_intro = _display_intro;
    }

    public int getID() {
        return _id;
    }

    public void setID(int _id) {
        this._id = _id;
    }

    public int getDisplayIntro() {
        return _display_intro;
    }

    public void setDisplayIntro(int _display_intro) {
        this._display_intro = _display_intro;
    }

}
