package com.example.moodpredictor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";


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
    private static final String ONTIME_TABLE = "userOntimeTable";
    private static final String LOCATION_TABLE = "userLocationTable";
    private static final String VISIT_TABLE = "userVisitTable";

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
    private static final String SHAKES = "shakes";

    //Ontime table Columns
    private static final String ONTIME_ID = "ontimeID";
    private static final String ONTIME_DATE = "ontimeDate";
    private static final String ONTIME_TIME = "ontimeTime";

    //location Table Columns
    private static final String LOCATION_ID = "locationID";
    private static final String LOCATION_NAME = "locationName";
    private static final String LOCATION_LAT = "locationLatitude";
    private static final String LOCATION_LONG = "locationLongitude";

    //Visit Table Columns
    private static final String VISIT_ID = "visitID";
    private static final String VISIT_TIME = "visitTime";
    private static final String VISIT_DATE = "visitDate";

    //Table Creation
    //User Table
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + USER_TABLE + "(" + UID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT" + ")";

    //Mood Table
    private static final String CREATE_TABLE_MOOD = "CREATE TABLE " + MOOD_TABLE + " (" + MID +
            " INTEGER PRIMARY KEY AUTOINCREMENT ," + MOODDATE + " TEXT," + MOOD + " INTEGER," + UID + " INTEGER" + ")";

    //Step Table
    private static final String CREATE_TABLE_STEPS = "CREATE TABLE " + STEP_TABLE + " (" + STEPS_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT ," + DATE + " TEXT," + STEPS + " INTEGER," + UID + " INTEGER" + ")";

    //Shake Table
    private static final String CREATE_TABLE_SHAKE = "CREATE TABLE " + SHAKE_TABLE + " (" + SHAKE_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT ," + SHAKES + " TEXT," + SHAKE_DATE + " TEXT," + UID + " INTEGER" + ")";

    //On Time Table
    private static final String CREATE_TABLE_ONTIME = "CREATE TABLE " + ONTIME_TABLE + " (" + ONTIME_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT ," + ONTIME_DATE + " TEXT," + ONTIME_TIME + " INTEGER," +
            UID + " INTEGER" + ")";

    //Location Table
    private static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + LOCATION_TABLE + " (" + LOCATION_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT ," + LOCATION_NAME + " TEXT," + LOCATION_LAT + " FLOAT," +
            LOCATION_LONG + " FLOAT," + UID + " INTEGER" + ")";

    //Visit Table
    private static final String CREATE_TABLE_VISIT = "CREATE TABLE " + VISIT_TABLE + " (" + VISIT_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT ," + VISIT_DATE + " TEXT," + VISIT_TIME + " INTEGER," +
            LOCATION_ID + " INTEGER" + ")";


    //Debug User
    User debug = new User(1, "Simon");

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_MOOD);
        db.execSQL(CREATE_TABLE_STEPS);
        db.execSQL(CREATE_TABLE_SHAKE);
        db.execSQL(CREATE_TABLE_ONTIME);
        db.execSQL(CREATE_TABLE_LOCATION);
        db.execSQL(CREATE_TABLE_VISIT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STEP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MOOD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SHAKE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ONTIME_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + VISIT_TABLE);

        onCreate(db);

    }

    //USERS -----------------------------------------------------------------------------------------------------------------------------------------------------

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

    //STEPS -----------------------------------------------------------------------------------------------------------------------------------------------------

    public void insertNewStepDay(int steps, int uid, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STEPS, steps);
        cv.put(UID, uid);
        cv.put(DATE, date);
        db.insert(STEP_TABLE, null, cv);

        System.out.println("Finished insert");
        db.close();

    }

    public void updateSteps(String date, int uID, int steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STEPS, steps);
        db.update(STEP_TABLE, cv, UID + " = '" + uID + "' AND " + DATE + " = '" + date + "'", null);
        db.close();

    }


    public boolean stepIDExists(String date, int uID) {
        Cursor c = null;
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Checking Step Id");
        String selectQuery = "SELECT * FROM " + STEP_TABLE + " WHERE " + UID + " = '" + uID + "' AND " + DATE + " = '" + date + "'";
        Log.e("idExists", selectQuery);
        try {
            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                c.close();
                return true;
            } else {
                c.close();
                return false;
            }
        } catch (Exception ex) {
            c.close();
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public String getSteps(String date, int uID) throws NullPointerException {
        SQLiteDatabase db = this.getReadableDatabase();
        String rv;
        String selectQuery = "SELECT * FROM " + STEP_TABLE + " WHERE " + UID + " = '" + uID + "' AND " + DATE + " = '" + date + "'";
        Cursor c = null;
        Log.e("getSteps", selectQuery);

        try {
            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                rv = String.valueOf(c.getInt(c.getColumnIndex(STEPS)));
                c.close();
                return rv;
            } else {
                c.close();
                insertNewStepDay(0, uID, date);
                return rv = "0";
            }
        } catch (Exception ex) {
            c.close();
            System.out.println(ex.getMessage());
            return "0";
        }

    }

    //MOOD -----------------------------------------------------------------------------------------------------------------------------------------------------

    public void newMood(int uID, int mood, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(UID, uID);
        cv.put(MOOD, mood);
        cv.put(MOODDATE, date);
        db.insert(MOOD_TABLE, null, cv);

    }

    public void updateMood(int uid, String date, int mood) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MOOD, mood);
        db.update(MOOD_TABLE, cv, UID + " = '" + uid + "' AND " + MOODDATE + " = '" + date + "'", null);
        System.out.println("Mood Updated");
        db.close();
    }

    public boolean moodIDExists(int uid, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Checking Mood Id");
        String selectQuery = "SELECT * FROM " + MOOD_TABLE + " WHERE " + UID + " = '" + uid + "' AND " + MOODDATE + " = '" + date + "'";
        Log.e("idExists", selectQuery);
        Cursor c = null;
        try {
            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                c.close();
                return true;
            } else {
                c.close();
                return false;
            }
        } catch (Exception ex) {
            c.close();
            System.out.println(ex.getMessage());
            return false;
        }

    }

    //Shake -----------------------------------------------------------------------------------------------------------------------------------------------------

    public void newShake(int uID, String date, int shakes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(UID, uID);
        cv.put(SHAKE_DATE, date);
        cv.put(SHAKES, shakes);
        db.insert(SHAKE_TABLE, null, cv);
        db.close();
    }

    public void updateShake(int uid, String date, int shakes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SHAKES, shakes);
        db.update(SHAKE_TABLE, cv, UID + " = '" + uid + "' AND " + SHAKE_DATE + " = '" + date + "'", null);
        db.close();
    }

    public String getShake(int uID, String date) {
        SQLiteDatabase db = getReadableDatabase();

        String rv = "Not found";
        String selectQuery = "SELECT * FROM " + SHAKE_TABLE + " WHERE " + UID + " = '" + uID + "' AND " + SHAKE_DATE + " = '" + date + "'";

        Log.e("getShake", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
            rv = String.valueOf(c.getString(c.getColumnIndex(SHAKES)));
            c.close();
        }
        db.close();
        return rv;
    }

    public boolean shakeExists(int uID, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Checking Shake Id");
        String selectQuery = "SELECT * FROM " + SHAKE_TABLE + " WHERE " + UID + " = '" + uID + "' AND " + SHAKE_DATE + " = '" + date + "'";
        Log.e("idExists", selectQuery);
        Cursor c = null;
        try {
            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                c.close();
                return true;
            } else {
                c.close();
                return false;
            }
        } catch (Exception ex) {
            c.close();
            System.out.println(ex.getMessage());
            return false;
        }
    }

    //On time-----------------------------------------------------------------------------------------------------------------------------------------------------

    public void newOntime(int uID, String date, int seconds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(UID, uID);
        cv.put(ONTIME_DATE, date);
        cv.put(ONTIME_TIME, seconds);
        db.insert(ONTIME_TABLE, null, cv);

    }

    public void updateOnTime(int uID, String date, int seconds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ONTIME_TIME, seconds);
        db.update(ONTIME_TABLE, cv, UID + " = '" + uID + "' AND " + ONTIME_DATE + " = '" + date + "'", null);
        db.close();
    }

    public boolean onTimeExists(int uID, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("Checking Mood Id");
        String selectQuery = "SELECT * FROM " + ONTIME_TABLE + " WHERE " + UID + " = '" + uID + "' AND " + ONTIME_DATE + " = '" + date + "'";
        Log.e("idExists", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    public String getOnTime(int uID, String date) {
        SQLiteDatabase db = getReadableDatabase();

        String rv = "Not found";
        String selectQuery = "SELECT * FROM " + ONTIME_TABLE + " WHERE " + UID + " = '" + uID + "' AND " + ONTIME_DATE + " = '" + date + "'";

        Log.e("getOntime", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
            rv = String.valueOf(c.getInt(c.getColumnIndex(ONTIME_TIME)));
            c.close();
        }
        db.close();
        return rv;
    }

    //location -----------------------------------------------------------------------------------------------------------------------------------------------------

    public void newLocation(int uID, String name, double longitude, double lattitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(LOCATION_NAME, name);
        cv.put(LOCATION_LAT, lattitude);
        cv.put(LOCATION_LONG, longitude);
        cv.put(UID, uID);
        db.insert(LOCATION_TABLE, null, cv);
    }

    public ArrayList<Location> getLocations(int uID) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Location> retval = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + LOCATION_TABLE + " WHERE " + UID + " = '" + uID + "'";
        Log.e(TAG, "getLocations");
        Cursor c = null;
        try {
            c = db.rawQuery(selectQuery, null);
            while (c.moveToNext()) {
                Location rv = new Location(c.getString(c.getColumnIndex(LOCATION_NAME)));
                rv.setLongitude(c.getDouble(c.getColumnIndex(LOCATION_LONG)));
                rv.setLatitude(c.getDouble(c.getColumnIndex(LOCATION_LAT)));
                retval.add(rv);
            }

            return retval;

        } catch (Exception ex) {
            c.close();
            System.out.println(ex.getMessage());
            return retval;
        }

    }

    public int getLocID(int uID, String name) {
        SQLiteDatabase db = getReadableDatabase();
        int retval = -1;
        String selectQuery = "SELECT * FROM " + LOCATION_TABLE + " WHERE " + UID + " = '" + uID + "' AND " +
                LOCATION_NAME + " = '" + name + "'";
        Log.e(TAG, "GettingLocID");
        Cursor c = null;
        try {
            c = db.rawQuery(selectQuery, null);
            if (c != null) {
                c.moveToNext();
                retval = c.getInt(c.getColumnIndex(LOCATION_ID));
                c.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            retval = -1;
        }
        return retval;
    }

    public void deleteLoc(int lID){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(LOCATION_TABLE,LOCATION_ID + "=" + lID, null );
        db.delete(VISIT_TABLE, LOCATION_ID + "=" + lID, null);
    }

    //Visit
    public void newVisit(int lID, int time, String date) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues newCv = new ContentValues();
        newCv.put(VISIT_TIME, time);
        newCv.put(VISIT_DATE, date);
        newCv.put(LOCATION_ID, lID);
        db.insert(VISIT_TABLE, null, newCv);
        db.close();

    }

    public void updateVisit(int lID, int time, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues update = new ContentValues();
        int initval = getVisitTime(lID, date);
        int finval = initval + time;
        update.put(VISIT_TIME, finval);
        System.out.println("Init: " + initval);
        System.out.println("Final: " + finval);
        db.update(VISIT_TABLE, update, LOCATION_ID + " = " + lID + " AND " + VISIT_DATE + " = '" +
                date + "'", null);
        Log.e(TAG, "Visit Updated");
        db.close();
    }


    public boolean visitExists(int lid, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + VISIT_TABLE + " WHERE " + LOCATION_ID + " = '" + lid + "' AND " + VISIT_DATE + " = '" + date + "'";
        Log.e(TAG, "Check Visit id");
        Cursor c;
        try {
            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst()) {
                c.close();
                return true;
            } else {
                c.close();
                return false;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public int getVisitTime(int lID, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        int retval = 0;
        String selectQuery = "SELECT * FROM " + VISIT_TABLE + " WHERE " + LOCATION_ID + " = '" + lID + "' AND " + VISIT_DATE + " = '" + date + "'";
        Log.e(TAG, "Getting Visit time");
        Cursor c;

        try {
            c = db.rawQuery(selectQuery, null);
            if (c.moveToFirst())
                retval = c.getInt(c.getColumnIndex(VISIT_TIME));
            else {
                c.close();
                retval = 0;
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
            retval = 0;
        }
        return retval;

    }

    //Predict -----------------------------------------------------------------------------------------------------------------------------------------------------

    public ArrayList<StepMoodObject> getStepsMood(int uid) {

        SQLiteDatabase db = getReadableDatabase();
        ArrayList<StepMoodObject> retval = new ArrayList<>();

        String selectQuery = "SELECT userMoodTable.mood, userStepsTable.Steps, userStepsTable.Date, userSTepsTable.stepsID FROM " + USER_TABLE +
                " JOIN " + MOOD_TABLE + " ON userMoodTable.UID=userTable.UID " +
                "JOIN " + STEP_TABLE + "  ON userStepsTable.UID=userTable.UID " +
                "AND userStepsTable.Date=userMoodTable.moodDate " +
                " WHERE userTable.UID= " + uid +
                " ORDER BY " + STEPS_ID;

        Log.e("getStepsAndMood", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext()) {
            StepMoodObject rv = new StepMoodObject(c.getInt(c.getColumnIndex(STEPS)), c.getInt(c.getColumnIndex(MOOD)), c.getString(c.getColumnIndex(DATE)), c.getInt(c.getColumnIndex(STEPS_ID)));
            retval.add(rv);

        }

        int count = 1;
        for (StepMoodObject element : retval) {
            System.out.print(count + " ");
            System.out.println(element);
            count++;

        }
        c.close();
        db.close();
        return retval;
    }

    public ArrayList<StepMoodObject> getStepsMoodDate(int uid) {

        SQLiteDatabase db = getReadableDatabase();
        ArrayList<StepMoodObject> retval = new ArrayList<>();

        String selectQuery = "SELECT userMoodTable.mood, userStepsTable.Steps, userStepsTable.Date, userSTepsTable.stepsID FROM " + USER_TABLE +
                " JOIN " + MOOD_TABLE + " ON userMoodTable.UID=userTable.UID " +
                "JOIN " + STEP_TABLE + "  ON userStepsTable.UID=userTable.UID " +
                "AND userStepsTable.Date=userMoodTable.moodDate " +
                " WHERE userTable.UID= " + uid +
                " ORDER BY " + DATE + " ASC ";

        Log.e("getStepsAndMood", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext()) {
            StepMoodObject rv = new StepMoodObject(c.getInt(c.getColumnIndex(STEPS)), c.getInt(c.getColumnIndex(MOOD)), c.getString(c.getColumnIndex(DATE)), c.getInt(c.getColumnIndex(STEPS_ID)));
            retval.add(rv);

        }

        int count = 1;
        for (StepMoodObject element : retval) {
            System.out.print(count + " ");
            System.out.println(element);
            count++;

        }
        c.close();
        db.close();
        return retval;
    }

    public ArrayList<ShakeMoodObject> getShakeMood(int uid) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ShakeMoodObject> retval = new ArrayList<>();

        String selectQuery = "SELECT userMoodTable.mood, userShakeTable.shakes, userShakeTable.shakeDate, userShakeTable.shakeID FROM " + USER_TABLE +
                " JOIN " + MOOD_TABLE + " ON userMoodTable.UID=userTable.UID " +
                "JOIN " + SHAKE_TABLE + "  ON userShakeTable.UID=userTable.UID " +
                "AND userShakeTable.shakeDate=userMoodTable.moodDate " +
                "WHERE userTable.UID= " + uid +
                " ORDER BY " + SHAKE_ID;

        Log.e("getShakeAndMood", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext()) {
            ShakeMoodObject rv = new ShakeMoodObject(c.getInt(c.getColumnIndex(SHAKES)), c.getInt(c.getColumnIndex(MOOD)), c.getString(c.getColumnIndex(SHAKE_DATE)), c.getInt(c.getColumnIndex(SHAKE_ID)));
            retval.add(rv);
        }

        c.close();
        db.close();
        return retval;

    }

    public ArrayList<ShakeMoodObject> getShakeMoodDate(int uid) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ShakeMoodObject> retval = new ArrayList<>();

        String selectQuery = "SELECT userMoodTable.mood, userShakeTable.shakes, userShakeTable.shakeDate, userShakeTable.shakeID FROM " + USER_TABLE +
                " JOIN " + MOOD_TABLE + " ON userMoodTable.UID=userTable.UID " +
                "JOIN " + SHAKE_TABLE + "  ON userShakeTable.UID=userTable.UID " +
                "AND userShakeTable.shakeDate=userMoodTable.moodDate " +
                "WHERE userTable.UID= " + uid +
                " ORDER BY " + SHAKE_DATE;

        Log.e("getShakeAndMood", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext()) {
            ShakeMoodObject rv = new ShakeMoodObject(c.getInt(c.getColumnIndex(SHAKES)), c.getInt(c.getColumnIndex(MOOD)), c.getString(c.getColumnIndex(SHAKE_DATE)), c.getInt(c.getColumnIndex(SHAKE_ID)));
            retval.add(rv);
        }

        c.close();
        db.close();
        return retval;

    }

    public ArrayList<onTimeMoodObject> getonTimeMood(int uid) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<onTimeMoodObject> retval = new ArrayList<>();

        String selectQuery = "SELECT userMoodTable.mood, userOntimeTable.ontimeTime, userOntimeTable.ontimeDate FROM " + USER_TABLE +
                " JOIN " + MOOD_TABLE + " ON userMoodTable.UID=userTable.UID " +
                "JOIN " + ONTIME_TABLE + "  ON userOntimeTable.UID=userTable.UID " +
                "AND userOntimeTable.ontimeDate=userMoodTable.moodDate " +
                "WHERE userTable.UID= " + uid;

        Log.e(TAG, "getShakeAndMood" + selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        while (c.moveToNext()) {
            onTimeMoodObject rv = new onTimeMoodObject(c.getInt(c.getColumnIndex(ONTIME_TIME)), c.getString(c.getColumnIndex(ONTIME_DATE)), c.getInt(c.getColumnIndex(MOOD)));
            retval.add(rv);
        }

        c.close();
        db.close();
        return retval;
    }

    public ArrayList<LocationMoodObject> getLocMood(int lID){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<LocationMoodObject> retval = new ArrayList<>();

        String selectQuery = "SELECT userLocationTable.locationID, userVisitTable.visitTime, userVisitTable.visitDate, userMoodTable.mood FROM " + LOCATION_TABLE +
                " JOIN " + VISIT_TABLE + " ON userLocationTable.locationID = userVisitTable.locationID " +
                " JOIN " + MOOD_TABLE + " ON userVisitTable.visitDate = userMoodTable.moodDate " +
                " WHERE userLocationTable.locationID = " + lID;

        Log.e(TAG, "GetLockMood" + selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        while (c.moveToNext()){
            LocationMoodObject rv = new LocationMoodObject(c.getInt(c.getColumnIndex(VISIT_TIME)), c.getInt(c.getColumnIndex(MOOD)));
            retval.add(rv);
        }

        c.close();
        db.close();
        return retval;
    }


    //TESTING ----------------------------------------------------------------------------------------------------------------------------------------------------------

    public void dummyData(int uid) {
        SQLiteDatabase db = getWritableDatabase();
        int insert = 0;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date start = c.getTime();

        Calendar d = Calendar.getInstance();
        d.set(Calendar.YEAR, 2019);
        d.set(Calendar.MONTH, 11);
        d.set(Calendar.DAY_OF_MONTH, 31);
        Date end = d.getTime();

        for (int i = 0; i < 500; i++) {

            String date = randDate(start, end);
            if (!stepIDExists(date, uid)) {
                int steps = new Random().nextInt(11000) + 1;
                int mood = new Random().nextInt(5) + 1;
                int shake = new Random().nextInt(11000) + 1;
                int onTime = new Random().nextInt(86000) + 1;

                newMood(uid, mood, date);
                insertNewStepDay(steps, uid, date);
                newOntime(uid, date, onTime);
                newShake(uid, date, shake);
                int time = 86500;
                int time1 = new Random().nextInt(time);
                time -= time1;
                int time2 = new Random().nextInt(time);
                time -= time2;
                int time3 = new Random().nextInt(time);
                newVisit(5, time1, date);
                newVisit(6, time2, date);
                newVisit(7, time3, date);
            }
        }

        insert++;
        System.out.println("Data Inserted: " + insert);
    }

    public String randDate(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(randomMillisSinceEpoch));
    }
}
