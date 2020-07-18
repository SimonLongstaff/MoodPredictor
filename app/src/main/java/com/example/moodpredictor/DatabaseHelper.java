package com.example.moodpredictor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    //Constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Moodpredictor Database", null, 1);
    }

    //Var
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

    //Table Names
    private static final String USER_TABLE = "userTable";
    private static final String STEP_TABLE = "userStepsTable";
    private static final String MOOD_TABLE = "userMoodTable";
    private static final String SHKAE_TABLE = "userShakeTable";

    //User Table columns
    private static final String UID = "UID";
    private static final String NAME = "Name";

    //Mood Table Columns
    private static final String MID = "MID";
    private static final String MOODDATE = "moodDate";
    private static final String MOOD = "mood";

    //Steps Table Columns
    private static final String STEPS_ID = "stepsID";
    private static final String DATE = "Date";
    private static final String STEPS = "Steps";

    //Shake Table Columns
    private static final String SHAKE_ID = "shakeID";
    private static final String SHAKE_DATE = "shakeDate";

    //Table Creation
    //User Table
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + USER_TABLE + "(" + UID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT" + ")";

    //Mood Table
    private static final String CREATE_TABLE_MOOD = "CREATE TABLE " + MOOD_TABLE + " (" + MID +
            " TEXT PRIMARY KEY ," + MOODDATE + " TEXT," + MOOD + " INTEGER," + UID + " INTEGER" + ")";

    //Step Table
    private static final String CREATE_TABLE_STEPS = "CREATE TABLE " + STEP_TABLE + " (" + STEPS_ID +
            " TEXT PRIMARY KEY," + DATE + " TEXT," + STEPS + " INTEGER," + UID + " INTEGER" + ")";

    //Shake Table
    private static final String CREATE_TABLE_SHAKE = "CREATE TABLE " + SHKAE_TABLE + " (" + SHAKE_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + SHAKE_DATE + " TEXT," + UID + " INTEGER" + ")";


    //Debug User
    User debug = new User(1, "Simon");

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_MOOD);
        db.execSQL(CREATE_TABLE_STEPS);
        db.execSQL(CREATE_TABLE_SHAKE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STEP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MOOD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SHKAE_TABLE);

        onCreate(db);

    }

    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NAME, user.getName());

        db.insert(USER_TABLE, null, cv);
    }

    public String getLoggedIn(int UID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + USER_TABLE + " WHERE " + "UID" + "=" + UID;

        Log.e("SelectUser", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        return c.getString(c.getColumnIndex(NAME));

    }

    public void insertNewStepDay(int steps, int uid) {
        System.out.println("Got to insert");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String id = uid + date;
        cv.put(STEPS_ID, id);
        cv.put(STEPS, steps);
        cv.put(UID, uid);
        cv.put(DATE, date);
        db.insert(STEP_TABLE, null, cv);

        System.out.println("Finished insert");
        db.close();

    }

    public void updateSteps(String step_id, int steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STEPS,steps);
        db.update(STEP_TABLE, cv, STEPS_ID + "= '" + step_id + "'", null);
        System.out.println("Update Steps");
        db.close();

    }


    public boolean stepIDExists(String stepsid) {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Checking Step Id");
        String selectQuery = "SELECT * FROM " + STEP_TABLE + " WHERE " + STEPS_ID + " = '" + stepsid + "'";
        Log.e("idExists", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount()>0) {
            c.close();
            System.out.println("StepID found");
            return true;
        }
        else {
            c.close();
            System.out.println("StepId not found");
            return false;
        }

    }

    public String getSteps(String stepID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String rv = "Not found";
        String selectQuery = "SELECT * FROM " + STEP_TABLE + " WHERE " + STEPS_ID + " = '" + stepID + "'";

        Log.e("getSteps", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
            rv = String.valueOf(c.getInt(c.getColumnIndex(STEPS)));
        }

        return rv;
    }

    public void newMood(String mID, int uID, int mood, String date ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MID, mID);
        cv.put(UID, uID);
        cv.put(MOOD, mood);
        cv.put(MOODDATE, date);
        db.insert(MOOD_TABLE, null, cv);

    }

    public void updateMood(String mID, int mood){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MOOD, mood);
        db.update(MOOD_TABLE, cv, MID + "= '" + mID + "'", null);
        System.out.println("Mood Updated");
        db.close();
    }

    public boolean moodIDExists(String mID){
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Checking Mood Id");
        String selectQuery = "SELECT * FROM " + MOOD_TABLE + " WHERE " + MID + " = '" + mID + "'";
        Log.e("idExists", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount()>0) {
            c.close();
            System.out.println("MID found");
            return true;
        }
        else {
            c.close();
            System.out.println("MID not found");
            return false;
        }

    }
}
