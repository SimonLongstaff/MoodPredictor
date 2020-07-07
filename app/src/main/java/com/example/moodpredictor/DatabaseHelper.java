package com.example.moodpredictor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    //Constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Moodpredictor Database", null, 1);
    }

    //Table Names
    private static final String USER_TABLE = "userTable";
    private static final String STEP_TABLE = "userStepsTable";
    private static final String MOOD_TABLE = "userMoodTable";
    private static final String SHKAE_TABLE = "userShakeTable";

    //User Table columns
    private static final String UID = "UID";
    private static final String NAME = "Name";
    private static final String OCCUPATION = "Occupation";
    private static final String DOB = "DOB";
    private static final String ACTIVITY_LEVEL = "Activity";

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
            " INTEGER PRIMARY KEY," + NAME + " TEXT," + OCCUPATION + " TEXT," + DOB + " TEXT," +
            ACTIVITY_LEVEL + " INTEGER" + ")";

    //Mood Table
    private static final String CREATE_TABLE_MOOD = "CREATE TABLE " + MOOD_TABLE + " (" + MID +
            " INTEGER PRIMARY KEY," + MOODDATE + " TEXT," + MOOD + " INTEGER," + UID + " INTEGER" + ")";

    //Step Table
    private static final String CREATE_TABLE_STEPS = "CREATE TABLE " + STEP_TABLE + " (" + STEPS_ID +
            " INTEGER PRIMARY KEY," + DATE + " TEXT," + STEPS + " INTEGER," + UID + " INTEGER" + ")";

    //Shake Table
    private static final String CREATE_TABLE_SHAKE = "CREATE TABLE " + SHKAE_TABLE + " (" + SHAKE_ID +
            " INTEGER PRIMARY KEY," + SHAKE_DATE + " TEXT," + UID + " INTEGER" + ")";




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
}
