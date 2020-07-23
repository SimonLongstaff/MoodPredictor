package com.example.moodpredictor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
    private static final String SHAKE_TABLE = "userShakeTable";

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
            " INTEGER PRIMARY KEY AUTOINCREMENT ," + MOODDATE + " TEXT," + MOOD + " INTEGER," + UID + " INTEGER" + ")";

    //Step Table
    private static final String CREATE_TABLE_STEPS = "CREATE TABLE " + STEP_TABLE + " (" + STEPS_ID +
            " TEXT PRIMARY KEY ," + DATE + " TEXT," + STEPS + " INTEGER," + UID + " INTEGER" + ")";

    //Shake Table
    private static final String CREATE_TABLE_SHAKE = "CREATE TABLE " + SHAKE_TABLE + " (" + SHAKE_ID +
            " TEXT PRIMARY KEY ," + SHAKE_DATE + " TEXT," + UID + " INTEGER" + ")";


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
        db.execSQL("DROP TABLE IF EXISTS " + SHAKE_TABLE);

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
        cv.put(STEPS, steps);
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
        if (c.getCount() > 0) {
            c.close();
            System.out.println("StepID found");
            return true;
        } else {
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

    public void newMood(String mID, int uID, int mood, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MID, mID);
        cv.put(UID, uID);
        cv.put(MOOD, mood);
        cv.put(MOODDATE, date);
        db.insert(MOOD_TABLE, null, cv);

    }

    public void updateMood(String mID, int mood) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MOOD, mood);
        db.update(MOOD_TABLE, cv, MID + "= '" + mID + "'", null);
        System.out.println("Mood Updated");
        db.close();
    }

    public boolean moodIDExists(String mID) {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Checking Mood Id");
        String selectQuery = "SELECT * FROM " + MOOD_TABLE + " WHERE " + MID + " = '" + mID + "'";
        Log.e("idExists", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount() > 0) {
            c.close();
            System.out.println("MID found");
            return true;
        } else {
            c.close();
            System.out.println("MID not found");
            return false;
        }

    }

    public void newShake(int uID, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(UID, uID);
        cv.put(SHAKE_DATE, date);
        db.insert(SHAKE_TABLE, null, cv);

    }

    public String getShake(int uId, String date) {
        SQLiteDatabase db = getReadableDatabase();

        String rv = "Not found";
        String selectQuery = "SELECT * FROM " + SHAKE_TABLE + " WHERE " + UID + " = '" + uId + "' AND " + SHAKE_DATE + " = '" + date + "'";

        Log.e("getSteps", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {

            rv = String.valueOf(c.getCount());
        }

        return rv;

    }

    public ArrayList<StepMoodObject> getStepsMood(int uid) {

        SQLiteDatabase db = getReadableDatabase();
        ArrayList<StepMoodObject> retval = new ArrayList<>();

        String selectQuery = "SELECT userMoodTable.mood, userStepsTable.Steps, userStepsTable.Date FROM " + USER_TABLE +
                " JOIN " + MOOD_TABLE + " ON userMoodTable.UID=userTable.UID " +
                "JOIN " + STEP_TABLE + "  ON userStepsTable.UID=userTable.UID " +
                "WHERE userTable.UID=" + uid;

        Log.e("getStepsAndMood", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {

            for (int i =1; i < 356; i++) {
                c.move(i);
                StepMoodObject rv = new StepMoodObject(c.getInt(c.getColumnIndex(STEPS)), c.getInt(c.getColumnIndex(MOOD)), c.getString(c.getColumnIndex(DATE)));
                retval.add(rv);

            }
        }

        System.out.println(Arrays.toString(retval.toArray()));
        return retval;


    }

    public void dummyData() {
        SQLiteDatabase db = getWritableDatabase();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2000);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date start = c.getTime();

        Calendar d = Calendar.getInstance();
        d.set(Calendar.YEAR, 2019);
        d.set(Calendar.MONTH, 11);
        d.set(Calendar.DAY_OF_MONTH, 31);
        Date end = d.getTime();

        for (int i = 0; i < 1000; i++) {

            String date = randDate(start, end);
            String sid = "1" + date;
            if (!stepIDExists(sid)) {
                ContentValues cv = new ContentValues();
                int steps = new Random().nextInt(11000) + 1;
                cv.put(STEPS_ID, sid);
                cv.put(STEPS, steps);
                cv.put(UID, 1);
                cv.put(DATE, date);
                db.insert(STEP_TABLE, null, cv);

                if (!moodIDExists(sid)) {
                    ContentValues contentValues = new ContentValues();
                    int mood = new Random().nextInt(4) + 1;
                    contentValues.put(MOOD, mood);
                    contentValues.put(MOODDATE,date);
                    contentValues.put(UID, 1);
                    db.insert(MOOD_TABLE, null, contentValues);
                }
            }


        }

    }

    public String randDate(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(randomMillisSinceEpoch));
    }
}
