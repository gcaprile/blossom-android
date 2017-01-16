package com.findigital.blossom.models;

import android.provider.BaseColumns;

/**
 * Created by 14-AB109LA on 16/1/2017.
 */

public class User {

    String _id;
    String _username;
    String _email;
    String _first_name;
    String _last_name;
    String _career_path_id;

    public User() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_CAREER_PATH_ID = "career_path_id";
    }

    public User(String _id, String _username, String _email, String _first_name, String _last_name,
                String _career_path_id) {
        this._id = _id;
        this._username = _username;
        this._email = _email;
        this._first_name = _first_name;
        this._last_name = _last_name;
        this._career_path_id = _career_path_id;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String _username) {
        this._username = _username;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

    public String getFirstName() {
        return _first_name;
    }

    public void setFirstName(String _first_name) {
        this._first_name = _first_name;
    }

    public String getLastName() {
        return _last_name;
    }

    public void setLastName(String _last_name) {
        this._last_name = _last_name;
    }

    public String getCareerPathId() {
        return _career_path_id;
    }

    public void setCareerPathId(String _career_path_id) {
        this._career_path_id = _career_path_id;
    }
}
