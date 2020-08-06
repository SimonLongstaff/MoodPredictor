package com.example.moodpredictor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MenuItem;
import android.widget.Switch;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static androidx.core.content.ContextCompat.getSystemService;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        SensorEventListener,
        StepListener {

    //Drawers -----------------------------------------------------------------------------------------------------------------------------------------------------
    private DrawerLayout drawer;

    //Var -----------------------------------------------------------------------------------------------------------------------------------------------------
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    DatabaseHelper database = new DatabaseHelper(this);

    //Location-----------------------------------------------------------------------------------------------------------------------------------------------------

    ArrayList<Location> userLocations;
    private static final int Geo_Radius = 50;
    Location currentLocation = null;
    double startTimer;
    double endTimer;

    //Sensors-----------------------------------------------------------------------------------------------------------------------------------------------------
    private SensorManager sensorManager;
    private StepDetector stepDetector;
    private Sensor accel;
    private int Stepsnum;
    private int shakeNum;

    //User -----------------------------------------------------------------------------------------------------------------------------------------------------
    private int loggedInUID = 1;
    private String loggedInName;


    //Debug User -----------------------------------------------------------------------------------------------------------------------------------------------------
    User debug = new User("Simon");


    @SuppressLint("MissingPermission")
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
        screenTimeBroadcastReceiver.setDatabaseHelper(database);;


        //Location-----------------------------------------------------------------------------------------------------------------------------------------------------

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, (android.location.LocationListener) locationListener);
        userLocations = new ArrayList<>();
        updateAreas();

        //Navigation Bar setup-----------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //User-----------------------------------------------------------------------------------------------------------------------------------------------------
        //database.insertUser(debug);
        //database.dummyData(1);
        database.newShake(1, date, 1000);
        database.newOntime(1, date, 24000);
        SettingUser();

        //Create Steps database-----------------------------------------------------------------------------------------------------------------------------------------------------
        if (database.stepIDExists(date, getLoggedInUser())) {
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
                break;
            case R.id.nav_listloc:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditLocationFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Location-----------------------------------------------------------------------------------------------------------------------------------------------------

    public void updateAreas() {
        userLocations = database.getLocations(getLoggedInUser());

    }

    public ArrayList<LatLng> getUserLocLatLan(){
        ArrayList<LatLng> retval = new ArrayList<>();
        for (int i = 0; i<userLocations.size();i++){
            retval.add(new LatLng(userLocations.get(i).getLatitude(), userLocations.get(i).getLongitude()));
        }
        return retval;
    }

    public ArrayList<String> getUserLocations() {
        ArrayList<String> retval = new ArrayList<>();
        for (int i = 0; i < userLocations.size(); i++) {
            retval.add(userLocations.get(i).getProvider());
        }
        return retval;
    }

    android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLatitude();
            System.out.println("Lat: " + latitude + " Long: " + longitude);
            if (currentLocation != null) {
                if (location.distanceTo(currentLocation) > Geo_Radius) {
                    endTimer = System.currentTimeMillis();
                    int time = (int) ((endTimer - startTimer) / 1000);
                    int lID = database.getLocID(getLoggedInUser(), currentLocation.getProvider());
                    if (database.visitExists(lID, date)) {

                        database.updateVisit(lID, time, date);
                    } else
                        database.newVisit(lID, time, date);
                    currentLocation = null;
                }
            } else {
                for (int i = 0; i < userLocations.size(); i++) {
                    if (location.distanceTo(userLocations.get(i)) < Geo_Radius) {
                        currentLocation = userLocations.get(i);
                        startTimer = System.currentTimeMillis();
                    }
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    //Screen on-----------------------------------------------------------------------------------------------------------------------------------------------------

    public String getOnTime() {
        int seconds;
        int minutes = 0;
        int hours = 0;

        int onTime = Integer.parseInt(database.getOnTime(getLoggedInUser(), date));
        while (onTime >= 60) {
            minutes++;
            onTime = onTime - 60;
            if (minutes > 60) {
                hours++;
                minutes = minutes - 60;
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
                database.updateSteps(date, getLoggedInUser(), Stepsnum);
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
        database.updateSteps(date, getLoggedInUser(), Stepsnum);

    }

    public String getSteps() {
        return database.getSteps(date, getLoggedInUser());
    }


    public int getShakeBucketed(){
        return BayesHelper.shakeBucket(Integer.parseInt(database.getShake(getLoggedInUser(),getDate())));

    }

    @Override
    public void shake(long timeNs) {
        System.out.println("New Shake");
        if (database.shakeExists(getLoggedInUser(), date)) {
            shakeNum++;
            database.updateShake(getLoggedInUser(), date, shakeNum);
        } else {
            database.newShake(loggedInUID, date, 1);
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

    public int[][] shakeMatrixBuilder() {
        ArrayList<ShakeMoodObject> matrixBuilder = database.getShakeMood(getLoggedInUser());

        int[][] matrix = {{1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            int shakes = matrixBuilder.get(i).getShakes();
            int mood = matrixBuilder.get(i).getMood() - 1;
            int shakeBucket = BayesHelper.shakeBucket(shakes);


            matrix[mood][shakeBucket]++;
        }
        System.out.println("Shake: " + Arrays.deepToString(matrix));
        return matrix;

    }

    public int[][] onTimeMAtrixBuilder() {

        ArrayList<onTimeMoodObject> matrixBuilder = database.getonTimeMood(getLoggedInUser());

        int[][] matrix = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            System.out.println(i);
            System.out.println(matrixBuilder.get(i).toString());
            int onTime = matrixBuilder.get(i).getOnTime();
            int mood = matrixBuilder.get(i).getMood() - 1;
            int onTimeBucket = BayesHelper.onTimeBucket(onTime);

            matrix[mood][onTimeBucket]++;

        }
        return matrix;
    }

    public int[][] locMatrixBuilder(int lID) {

        ArrayList<LocationMoodObject> matrixBuilder = database.getLocMood(lID);

        int[][] matrix = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

        for (int i = 0; i < matrixBuilder.size(); i++) {
            System.out.println(matrixBuilder.get(i).toString());
            int mood = matrixBuilder.get(i).getMood() - 1;
            int vTime = matrixBuilder.get(i).getTime();
            int vTimeBucket = BayesHelper.dayBucket(vTime);

            matrix[mood][vTimeBucket]++;

        }
        return matrix;
    }

    public LocMoodCalObject locMoodCalculation(String date) {
        updateAreas();
        double locMoodVs = 0;
        double locMoodS = 0;
        double locMoodN = 0;
        double locMoodH = 0;
        double locMoodVh = 0;

        double locMoodTotalVs = 0;
        double locMoodTotalS = 0;
        double locMoodTotalN = 0;
        double locMoodTotalH = 0;
        double locMoodTotalVh = 0;

        double locMoodTotal = 0;


        for (int i = 0; i < userLocations.size(); i++) {
            int lId = database.getLocID(getLoggedInUser(), userLocations.get(i).getProvider());
            int[][] matrix = locMatrixBuilder(lId);
            for (int j = 0; j < 5; j++) {
                double rv = BayesHelper.locMoodCal(matrix, j, database.getVisitTime(lId, date));
                double rvTotal = BayesHelper.totalMatrixRow(matrix, 10, j);
                switch (j) {
                    case 0:
                        locMoodVs = locMoodVs + rv;
                        locMoodTotalVs = locMoodTotalVs + rvTotal;
                        break;
                    case 1:
                        locMoodS = locMoodS + rv;
                        locMoodTotalS = locMoodTotalS + rvTotal;
                        break;
                    case 2:
                        locMoodN = locMoodN + rv;
                        locMoodTotalN = locMoodTotalN + rvTotal;
                        break;
                    case 3:
                        locMoodH = locMoodH + rv;
                        locMoodTotalH = locMoodTotalH + rvTotal;
                        break;
                    case 4:
                        locMoodVh = locMoodVh + rv;
                        locMoodTotalVh = locMoodTotalVh + rv;
                        break;
                }

            }

            locMoodTotal = locMoodTotal + BayesHelper.totalMatrix(matrix, 10, 5);

        }

        LocMoodCalObject retval = new LocMoodCalObject(locMoodVs, locMoodS, locMoodN, locMoodH, locMoodVh,
                locMoodTotalVs, locMoodTotalS, locMoodTotalN, locMoodTotalH, locMoodTotalVh, locMoodTotal);
        return retval;
    }



    public int prediction() {
        int[][] matrixSteps = stepsMatrixBuilder();
        int[][] matrixShake = shakeMatrixBuilder();
        int[][] matrixOnTime = onTimeMAtrixBuilder();

        LocMoodCalObject locMoodCalObject = locMoodCalculation(getDate());

        System.out.println(locMoodCalObject.toString());


        int steps = Integer.parseInt(database.getSteps(date, getLoggedInUser()));
        int shakes = Integer.parseInt(database.getShake(getLoggedInUser(), date));
        int onTime = Integer.parseInt(database.getOnTime(getLoggedInUser(), date));

        return BayesHelper.predictMoodShakeOnTime(matrixSteps, matrixShake, matrixOnTime, shakes, steps, onTime,
                locMoodCalObject);


    }


}

