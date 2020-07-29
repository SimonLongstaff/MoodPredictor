package com.example.moodpredictor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        SensorEventListener,
        StepListener {

    //Drawers -----------------------------------------------------------------------------------------------------------------------------------------------------
    private DrawerLayout drawer;

    //Var -----------------------------------------------------------------------------------------------------------------------------------------------------
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    DatabaseHelper database = new DatabaseHelper(this);

    //Location-----------------------------------------------------------------------------------------------------------------------------------------------------

    GeofencingClient geofencingClient;

    //Sensors-----------------------------------------------------------------------------------------------------------------------------------------------------
    private SensorManager sensorManager;
    private StepDetector stepDetector;
    private Sensor accel;
    private int Stepsnum;
    private int shakeNum;

    //User -----------------------------------------------------------------------------------------------------------------------------------------------------
    private int loggedInUID = 1;
    private String loggedInName;
    String id = loggedInUID + date;


    //Debug User -----------------------------------------------------------------------------------------------------------------------------------------------------
    User debug = new User("Simon");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Step Sensor setup-----------------------------------------------------------------------------------------------------------------------------------------------------
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetector = new StepDetector();
        stepDetector.registerListener(this);
        Stepsnum = 0;
        sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);


        //Screen on-----------------------------------------------------------------------------------------------------------------------------------------------------

        ScreenTimeBroadcastReceiver screenTimeBroadcastReceiver = new ScreenTimeBroadcastReceiver();
        IntentFilter lockfilter = new IntentFilter();
        lockfilter.addAction(Intent.ACTION_SCREEN_ON);
        lockfilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenTimeBroadcastReceiver, lockfilter);
        screenTimeBroadcastReceiver.setLoggedinUser(getLoggedInUser());
        screenTimeBroadcastReceiver.setDatabaseHelper(database);



        //Location-----------------------------------------------------------------------------------------------------------------------------------------------------

        //Navigation Bar setup-----------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //User-----------------------------------------------------------------------------------------------------------------------------------------------------
        database.insertUser(debug);
        //database.dummyData(1);
        database.newShake(1, date,1000);
        database.newOntime(1,date,24000);
        SettingUser();

        //Create Steps database-----------------------------------------------------------------------------------------------------------------------------------------------------
        if (database.stepIDExists(date,getLoggedInUser())) {
            database.insertNewStepDay(0, loggedInUID, date);
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }


    //User-----------------------------------------------------------------------------------------------------------------------------------------------------
    public int getLoggedInUser() {
        return loggedInUID;
    }

    public void setLoggedInUser(int loggedInUser) {
        this.loggedInUID = loggedInUser;
    }

    public void SettingUser() {
        String user = database.getLoggedIn(loggedInUID);
        System.out.println(user);
        loggedInName = user;
    }

    public String getLoggedInName() {
        return loggedInName;
    }

    public String getDate() {
        return date;
    }


    //Navigation Bar-----------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_steps:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
                break;
            case R.id.nav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Location-----------------------------------------------------------------------------------------------------------------------------------------------------


    //Screen on-----------------------------------------------------------------------------------------------------------------------------------------------------

    public String getOnTime(){
        int seconds;
        int minutes = 0;
        int hours= 0;

        int onTime = Integer.parseInt(database.getOnTime(getLoggedInUser(),date));
        while (onTime>60){
            minutes++;
            onTime = onTime- 60;
            if (minutes>60){
                hours++;
                minutes = minutes -60;
            }
        }

        seconds = onTime;

        return hours + " Hours " + minutes + " minutes " + seconds + " seconds ";

    }



    //Sensor updates-----------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                stepDetector.updateAccel(
                        sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                break;

            }

            case Sensor.TYPE_STEP_COUNTER: {
                if (Stepsnum < 1)
                    Stepsnum = (int) sensorEvent.values[0];

                Stepsnum = (int) sensorEvent.values[0] - Stepsnum;
                database.updateSteps(date,getLoggedInUser(), Stepsnum);
                System.out.println("New Steps registered");
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }



    @Override
    public void step(long timeNs) {
        Stepsnum++;
        System.out.println("Old Step registered");
        database.updateSteps(date,getLoggedInUser(),Stepsnum);

    }

    public String getSteps() {
        return database.getSteps(date,getLoggedInUser());
    }

    public String getShake() {
        return database.getShake(loggedInUID, date);
    }

    @Override
    public void shake(long timeNs) {
        System.out.println("New Shake");
        if (database.shakeExists(getLoggedInUser(), date)) {
            shakeNum++;
            database.updateShake(getLoggedInUser(), date, shakeNum);
        } else {
            database.newShake(loggedInUID, date,1);
            shakeNum = 1;
        }
    }

    //Prediction-----------------------------------------

    //Build the step matrix for Bayesian prediction
    public int[][] stepsMatrixBuilder() {

        ArrayList<StepMoodObject> matrixBuilder = database.getStepsMood(getLoggedInUser());
        System.out.println(matrixBuilder.size());

        int[][] matrix = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            //System.out.println(i);
            int steps = matrixBuilder.get(i).getSteps();
            int mood = matrixBuilder.get(i).getMood() - 1;
            int stepbucket = BayesHelper.stepBucket(steps);


            matrix[mood][stepbucket]++;
        }

        System.out.println("Steps: " + Arrays.deepToString(matrix));
        return matrix;
    }

    public int[][] shakeMatrixBuilder(){
        ArrayList<ShakeMoodObject> matrixBuilder = database.getShakeMood(getLoggedInUser());

        int[][] matrix = {{1,1,1,1,1,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            int shakes = matrixBuilder.get(i).getShakes();
            int mood = matrixBuilder.get(i).getMood() - 1;
            int shakeBucket = BayesHelper.shakeBucket(shakes);


            matrix[mood][shakeBucket]++;
        }
        System.out.println("Shake: " + Arrays.deepToString(matrix));
        return matrix;

    }

    public int[][] onTimeMAtrixBuilder(){

        ArrayList<onTimeMoodObject> matrixBuilder = database.getonTimeMood(getLoggedInUser());

        int[][] matrix = {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},};

        for (int i = 0; i< matrixBuilder.size(); i++){
            System.out.println(i);
            System.out.println(matrixBuilder.get(i).toString());
            int onTime = matrixBuilder.get(i).getOnTime();
            int mood = matrixBuilder.get(i).getMood()-1;
            int onTimeBucket = BayesHelper.onTimeBucket(onTime);

            matrix[mood][onTimeBucket]++;

        }
        return matrix;
    }


    public int prediction(){
        int[][] matrixSteps = stepsMatrixBuilder();
        int[][] matrixShake = shakeMatrixBuilder();
        int[][] matrixOnTime = onTimeMAtrixBuilder();

        int steps = Integer.parseInt(database.getSteps(date,getLoggedInUser()));
        int shakes = Integer.parseInt(database.getShake(getLoggedInUser(),date));
        int onTime = Integer.parseInt(database.getOnTime(getLoggedInUser(),date));

        return BayesHelper.predictMoodShakeOnTime(matrixSteps,matrixShake,matrixOnTime,shakes,steps,onTime);


    }

}