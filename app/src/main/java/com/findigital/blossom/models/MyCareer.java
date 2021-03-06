package com.findigital.blossom.models;

import android.provider.BaseColumns;

/**
 * Created by 14-AB109LA on 15/1/2017.
 */

public class MyCareer {

    String _id;
    String _name;
    String _color;

    public MyCareer() {}

    public static class MyCareerEntry implements BaseColumns {
        public static final String TABLE_NAME = "my_career";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COLOR = "color";
    }

    public MyCareer(String _id, String _name, String _color) {
        this._id = _id;
        this._name = _name;
        this._color = _color;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getColor() {
        return _color;
    }

    public void setColor(String _color) {
        this._color = _color;
    }

}
