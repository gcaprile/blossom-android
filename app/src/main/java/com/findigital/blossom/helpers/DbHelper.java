package com.findigital.blossom.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.findigital.blossom.models.Career;
import com.findigital.blossom.models.MyCareer;
import com.findigital.blossom.models.Setting;
import com.findigital.blossom.models.SurveyCareerResult;
import com.findigital.blossom.models.SurveyQuestion;
import com.findigital.blossom.models.SurveyResponse;
import com.findigital.blossom.models.SurveyResult;
import com.findigital.blossom.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 14-AB109LA on 8/1/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 17;
    public static final String DATABASE_NAME = "Blossom.db";

    /**
     * USERS TABLE
     */
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + User.UserEntry.TABLE_NAME + " (" +
                    User.UserEntry._ID + " STRING PRIMARY KEY," +
                    User.UserEntry.COLUMN_USERNAME + " STRING," +
                    User.UserEntry.COLUMN_EMAIL + " STRING," +
                    User.UserEntry.COLUMN_FIRST_NAME + " STRING," +
                    User.UserEntry.COLUMN_LAST_NAME + " STRING," +
                    User.UserEntry.COLUMN_CAREER_PATH_ID + " STRING)";

    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + User.UserEntry.TABLE_NAME;

    /**
     * MY CAREER TABLE
     */
    private static final String SQL_CREATE_MY_CAREER =
            "CREATE TABLE " + MyCareer.MyCareerEntry.TABLE_NAME + " (" +
                    MyCareer.MyCareerEntry._ID + " STRING PRIMARY KEY," +
                    MyCareer.MyCareerEntry.COLUMN_NAME + " STRING," +
                    MyCareer.MyCareerEntry.COLUMN_COLOR + " STRING)";

    private static final String SQL_DELETE_MY_CAREER =
            "DROP TABLE IF EXISTS " + MyCareer.MyCareerEntry.TABLE_NAME;

    /**
     * CAREERS TABLE
     */
    private static final String SQL_CREATE_CAREERS =
            "CREATE TABLE " + Career.CareerEntry.TABLE_NAME + " (" +
                    Career.CareerEntry._ID + " STRING PRIMARY KEY," +
                    Career.CareerEntry.COLUMN_CAREER_NAME + " STRING," +
                    Career.CareerEntry.COLUMN_CAREER_INTRO + " STRING," +
                    Career.CareerEntry.COLUMN_CAREER_COLOR + " STRING)";

    private static final String SQL_DELETE_CAREERS =
            "DROP TABLE IF EXISTS " + Career.CareerEntry.TABLE_NAME;

    /**
     * SETTINGS TABLE
     */
    private static final String SQL_CREATE_SETTINGS =
            "CREATE TABLE " + Setting.SettingsEntry.TABLE_NAME + " (" +
                    Setting.SettingsEntry._ID + " INTEGER PRIMARY KEY," +
                    Setting.SettingsEntry.COLUMN_NAME_DISPLAY_INTRO + " INT DEFAULT 1)";

    private static final String SQL_DELETE_SETTINGS =
            "DROP TABLE IF EXISTS " + Setting.SettingsEntry.TABLE_NAME;

    /**
     * SURVEY QUESTIONS TABLE
     */

    private static final String SQL_CREATE_SURVEY_QUESTIONS =
            "CREATE TABLE " + SurveyQuestion.SurveyQuestionEntry.TABLE_NAME + " (" +
                    SurveyQuestion.SurveyQuestionEntry._ID + " STRING PRIMARY KEY," +
                    SurveyQuestion.SurveyQuestionEntry.COLUMN_QUESTION + " STRING)";

    private static final String SQL_DELETE_SURVEY_QUESTIONS =
            "DROP TABLE IF EXISTS " + SurveyQuestion.SurveyQuestionEntry.TABLE_NAME;

    /**
     * SURVEY RESPONSES TABLE
     */
    private static final String SQL_CREATE_SURVEY_RESPONSES = 
            "CREATE TABLE " + SurveyResponse.SurveyResponseEntry.TABLE_NAME + " (" +
                    SurveyResponse.SurveyResponseEntry._ID + " STRING PRIMARY KEY," +
                    SurveyResponse.SurveyResponseEntry.COLUMN_TITLE + " STRING," +
                    SurveyResponse.SurveyResponseEntry.COLUMN_QUESTION + " STRING," +
                    SurveyResponse.SurveyResponseEntry.COLUMN_CAREERS + " STRING," +
                    SurveyResponse.SurveyResponseEntry.COLUMN_POINTS + " INT)";

    private static final String SQL_DELETE_SURVEY_RESPONSES =
            "DROP TABLE IF EXISTS " + SurveyResponse.SurveyResponseEntry.TABLE_NAME;

    /**
     * SURVEY RESULT TABLE
     */
    private static final String SQL_CREATE_SURVEY_RESULTS =
            "CREATE TABLE " + SurveyResult.SurveyResultEntry.TABLE_NAME + " (" +
                    SurveyResult.SurveyResultEntry.COLUMN_QUESTION + " STRING PRIMARY KEY," +
                    SurveyResult.SurveyResultEntry.COLUMN_RESPONSE + " STRING)";

    private static final String SQL_DELETE_SURVEY_RESULTS =
            "DROP TABLE IF EXISTS " + SurveyResult.SurveyResultEntry.TABLE_NAME;

    /**
     * SURVEY CAREER RESULT TABLE
     */
    private static final String SQL_CREATE_SURVEY_CAREER_RESULTS =
            "CREATE TABLE " + SurveyCareerResult.SurveyCareerResultEntry.TABLE_NAME + " (" +
                    SurveyCareerResult.SurveyCareerResultEntry._ID + " INTEGER PRIMARY KEY," +
                    SurveyCareerResult.SurveyCareerResultEntry.COLUMN_CAREER + " STRING," +
                    SurveyCareerResult.SurveyCareerResultEntry.COLUMN_POINTS + " INTEGER)";

    private static final String SQL_DELETE_SURVEY_CAREER_RESULTS =
            "DROP TABLE IF EXISTS " + SurveyCareerResult.SurveyCareerResultEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SETTINGS);
        db.execSQL(SQL_CREATE_SURVEY_QUESTIONS);
        db.execSQL(SQL_CREATE_SURVEY_RESPONSES);
        db.execSQL(SQL_CREATE_SURVEY_RESULTS);
        db.execSQL(SQL_CREATE_SURVEY_CAREER_RESULTS);
        db.execSQL(SQL_CREATE_CAREERS);
        db.execSQL(SQL_CREATE_MY_CAREER);
        db.execSQL(SQL_CREATE_USERS);

        // Initialize settings
        Setting setting = new Setting();
        setting.setDisplayIntro(1);

        // Add default settings
        ContentValues values = new ContentValues();
        values.put(Setting.SettingsEntry.COLUMN_NAME_DISPLAY_INTRO, setting.getDisplayIntro());

        db.insert(Setting.SettingsEntry.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_SETTINGS);
        db.execSQL(SQL_DELETE_SURVEY_QUESTIONS);
        db.execSQL(SQL_DELETE_SURVEY_RESPONSES);
        db.execSQL(SQL_DELETE_SURVEY_RESULTS);
        db.execSQL(SQL_DELETE_SURVEY_CAREER_RESULTS);
        db.execSQL(SQL_DELETE_CAREERS);
        db.execSQL(SQL_DELETE_MY_CAREER);
        db.execSQL(SQL_DELETE_USERS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    /*********************************************/
    /** CAREER CRUD **/
    /*********************************************/

    // Add new setting
    public void addCareer(Career career) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Career.CareerEntry._ID, career.getId());
        values.put(Career.CareerEntry.COLUMN_CAREER_NAME, career.getCareerName());
        values.put(Career.CareerEntry.COLUMN_CAREER_INTRO, career.getCareerIntro());
        values.put(Career.CareerEntry.COLUMN_CAREER_COLOR, career.getCareerColor());

        db.insert(Career.CareerEntry.TABLE_NAME, null, values);
        db.close();
    }

    public Career getCareer(String id) {

        String selectQuery =
                "SELECT * FROM " +
                        Career.CareerEntry.TABLE_NAME + " WHERE " +
                        Career.CareerEntry._ID + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Career career = new Career(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));

        cursor.close();

        return career;
    }

    public List<Career> getAllCareers() {
        List<Career> careerList = new ArrayList<Career>();

        String selectQuery = "SELECT * FROM " + Career.CareerEntry.TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Career career = new Career();
                career.setId(cursor.getString(0));
                career.setCareerName(cursor.getString(1));
                career.setCareerIntro(cursor.getString(2));
                career.setCareerColor(cursor.getString(3));
                careerList.add(career);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return careerList;
    }

    public void deleteAllCareers() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM " + Career.CareerEntry.TABLE_NAME;

        db.execSQL(sql);
        db.close();
    }

    /*********************************************/
    /** MY CAREER CRUD **/
    /*********************************************/

    public void addMyCareer(MyCareer myCareer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MyCareer.MyCareerEntry._ID, myCareer.getId());
        values.put(MyCareer.MyCareerEntry.COLUMN_NAME, myCareer.getName());
        values.put(MyCareer.MyCareerEntry.COLUMN_COLOR, myCareer.getColor());

        db.insert(MyCareer.MyCareerEntry.TABLE_NAME, null, values);
        db.close();
    }

    public MyCareer getMyCareer() {

        String selectQuery =
                "SELECT * FROM " +
                        MyCareer.MyCareerEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        MyCareer myCareer = new MyCareer();

        if (cursor.getCount() > 0) {
            myCareer = new MyCareer(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2));
        }

        cursor.close();

        return myCareer;
    }

    public void deleteMyCareer() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM " + MyCareer.MyCareerEntry.TABLE_NAME;

        db.execSQL(sql);
        db.close();
    }


    /*********************************************/
    /** SETTINGS CRUD **/
    /*********************************************/

    // Add new setting
    public void addSetting(Setting setting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Setting.SettingsEntry.COLUMN_NAME_DISPLAY_INTRO, setting.getDisplayIntro());

        db.insert(Setting.SettingsEntry.TABLE_NAME, null, values);
        db.close();
    }

    // Get ALL Settings
    public List<Setting> getAllSettings() {
        List<Setting> settingsList = new ArrayList<Setting>();

        String selectQuery = "SELECT * FROM " + Setting.SettingsEntry.TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Setting setting = new Setting();
                setting.setID(Integer.parseInt(cursor.getString(0)));
                setting.setDisplayIntro(Integer.parseInt(cursor.getString(1)));
                settingsList.add(setting);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return settingsList;
    }

    // Update setting
    public int updateSettings(Setting setting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Setting.SettingsEntry.COLUMN_NAME_DISPLAY_INTRO, setting.getDisplayIntro());

        // update row
        return db.update(Setting.SettingsEntry.TABLE_NAME, values, null, null);
    }

    /*********************************************/
    /** SURVEY QUESTIONS CRUD **/
    /*********************************************/

    // Add new survey question
    public void addQuestion(SurveyQuestion surveyQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(com.findigital.blossom.models.SurveyQuestion.SurveyQuestionEntry._ID, surveyQuestion.getID());
        values.put(com.findigital.blossom.models.SurveyQuestion.SurveyQuestionEntry.COLUMN_QUESTION, surveyQuestion.getQuestion());

        db.insert(com.findigital.blossom.models.SurveyQuestion.SurveyQuestionEntry.TABLE_NAME, null, values);
        db.close();
    }

    // Get ALL questions
    public List<SurveyQuestion> getAllQuestions() {
        List<SurveyQuestion> questionsList = new ArrayList<SurveyQuestion>();

        String selectQuery = "SELECT * FROM " + com.findigital.blossom.models.SurveyQuestion.SurveyQuestionEntry.TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SurveyQuestion surveyQuestion = new SurveyQuestion();
                surveyQuestion.setID(cursor.getString(0));
                surveyQuestion.setQuestion(cursor.getString(1));
                questionsList.add(surveyQuestion);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return questionsList;
    }

    public void deleteAllQuestions() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM " + com.findigital.blossom.models.SurveyQuestion.SurveyQuestionEntry.TABLE_NAME;

        db.execSQL(sql);
        db.close();
    }

    /*********************************************/
    /** SURVEY RESPONSES CRUD **/
    /*********************************************/
    // Add new survey response
    public void addResponse(SurveyResponse response) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SurveyResponse.SurveyResponseEntry._ID, response.getID());
        values.put(SurveyResponse.SurveyResponseEntry.COLUMN_TITLE, response.getTitle());
        values.put(SurveyResponse.SurveyResponseEntry.COLUMN_QUESTION, response.getQuestion());
        values.put(SurveyResponse.SurveyResponseEntry.COLUMN_CAREERS, response.getCareers());
        values.put(SurveyResponse.SurveyResponseEntry.COLUMN_POINTS, response.getPoints());

        db.insert(SurveyResponse.SurveyResponseEntry.TABLE_NAME, null, values);
        db.close();
    }

    // Get ALL responses
    public List<SurveyResponse> getAllResponses(String questionID, String responseID) {
        List<SurveyResponse> responsesList = new ArrayList<SurveyResponse>();

        // Get all responses available
        String selectQuery = "SELECT * FROM " + SurveyResponse.SurveyResponseEntry.TABLE_NAME;

        // Get all responses by question ID
        if (!questionID.equals("")) {
            selectQuery =
                    "SELECT * FROM " +
                            SurveyResponse.SurveyResponseEntry.TABLE_NAME + " WHERE " +
                            SurveyResponse.SurveyResponseEntry.COLUMN_QUESTION + " = '" + questionID + "'";
        }

        if (!responseID.equals("")) {
            selectQuery =
                    "SELECT * FROM " +
                            SurveyResponse.SurveyResponseEntry.TABLE_NAME + " WHERE " +
                            SurveyResponse.SurveyResponseEntry._ID + " = '" + responseID + "'";
        }

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SurveyResponse surveyResponse = new SurveyResponse();
                surveyResponse.setID(cursor.getString(0));
                surveyResponse.setTitle(cursor.getString(1));
                surveyResponse.setQuestion(cursor.getString(2));
                surveyResponse.setCareers(cursor.getString(3));
                surveyResponse.setPoints(cursor.getString(4));
                responsesList.add(surveyResponse);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return responsesList;
    }

    public void deleteAllSurveyResponses() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM " + SurveyResponse.SurveyResponseEntry.TABLE_NAME;

        db.execSQL(sql);
        db.close();
    }


    /*********************************************/
    /** SURVEY RESULTS CRUD **/
    /*********************************************/
    // Add new survey question
    public void addResult(SurveyResult result) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SurveyResult.SurveyResultEntry.COLUMN_RESPONSE, result.getResponse());
        values.put(SurveyResult.SurveyResultEntry.COLUMN_QUESTION, result.getQuestion());

        db.replace(SurveyResult.SurveyResultEntry.TABLE_NAME, null, values);
        db.close();
    }

    // Get ALL results
    public List<SurveyResult> getAllResults() {
        List<SurveyResult> resultsList = new ArrayList<SurveyResult>();

        // Get all responses available
        String selectQuery = "SELECT * FROM " + SurveyResult.SurveyResultEntry.TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SurveyResult surveyResult = new SurveyResult();
                surveyResult.setQuestion(cursor.getString(0));
                surveyResult.setResponse(cursor.getString(1));
                resultsList.add(surveyResult);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return resultsList;
    }

    public void deleteAllSurveyResults() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM " + SurveyResult.SurveyResultEntry.TABLE_NAME;

        db.execSQL(sql);
        db.close();
    }

    /*********************************************/
    /** SURVEY CAREER RESULTS CRUD **/
    /*********************************************/
    // Add new survey question
    public void addCareerResult(SurveyCareerResult result) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SurveyCareerResult.SurveyCareerResultEntry.COLUMN_CAREER, result.getCareer());
        values.put(SurveyCareerResult.SurveyCareerResultEntry.COLUMN_POINTS, result.getPoints());

        db.replace(SurveyCareerResult.SurveyCareerResultEntry.TABLE_NAME, null, values);
        db.close();
    }

    // Get ALL results
    public List<SurveyCareerResult> getTopCareers() {
        List<SurveyCareerResult> careerResultList = new ArrayList<SurveyCareerResult>();

        // Get all responses available
        String selectQuery2 =
                "SELECT  career, points FROM ( SELECT " +
                        SurveyCareerResult.SurveyCareerResultEntry.COLUMN_CAREER + ", SUM(" +
                        SurveyCareerResult.SurveyCareerResultEntry.COLUMN_POINTS + ") as points " + "FROM " +
                        SurveyCareerResult.SurveyCareerResultEntry.TABLE_NAME + " GROUP BY career) ORDER BY points DESC LIMIT 3";

        String TABLE = SurveyCareerResult.SurveyCareerResultEntry.TABLE_NAME;
        String selectQuery = "SELECT career, SUM(points) as points FROM " + TABLE;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SurveyCareerResult careerResult = new SurveyCareerResult();
                careerResult.setCareer(cursor.getString(0));
                careerResult.setPoints(cursor.getString(1));
                careerResultList.add(careerResult);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return careerResultList;
    }

    public void deleteAllSurveyCareerResults() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM " + SurveyCareerResult.SurveyCareerResultEntry.TABLE_NAME;

        db.execSQL(sql);
        db.close();
    }

    /*********************************************/
    /** USERS CRUD **/
    /*********************************************/

    // Add new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(User.UserEntry._ID, user.getId());
        values.put(User.UserEntry.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(User.UserEntry.COLUMN_LAST_NAME, user.getLastName());
        values.put(User.UserEntry.COLUMN_USERNAME, user.getUsername());
        values.put(User.UserEntry.COLUMN_EMAIL, user.getEmail());
        values.put(User.UserEntry.COLUMN_CAREER_PATH_ID, user.getCareerPathId());

        db.insert(User.UserEntry.TABLE_NAME, null, values);
        db.close();
    }

    public User getUser() {

        String selectQuery =
                "SELECT * FROM " +
                        User.UserEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User();

        if (cursor.getCount() > 0) {
            user = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
        }

        cursor.close();

        return user;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "DELETE FROM " + User.UserEntry.TABLE_NAME;

        db.execSQL(sql);
        db.close();
    }
}
